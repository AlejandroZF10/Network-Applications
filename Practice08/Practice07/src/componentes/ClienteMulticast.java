package componentes;

import clases.*;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import org.apache.commons.lang.SerializationUtils;

public class ClienteMulticast extends Thread{
    public static final String MCAST_ADDR  = "230.0.0.1"; 
    public static final int MCAST_PORT = 9013;
    public static final int DGRAM_BUF_LEN=512;

    private final String direccionHost;
    private final int puerto;
    private final String nomUsuario;
    
    private ArrayList<resultadoBusqueda> listaBusqueda;
    
    private ArrayList<DatosArchivo> todosLosArchivos;
    
    private InfoBusqueda ultimaBusqueda;
    
    public ClienteMulticast(String direccionHost, int puerto, String nomUsuario){
        this.direccionHost = direccionHost;
        this.puerto = puerto;
        this.nomUsuario = nomUsuario;  
    }
    
    @Override
    public void run(){  
        InetAddress group =null;
        try{
            group = InetAddress.getByName(MCAST_ADDR);
        }catch(UnknownHostException e){
            System.exit(1);
        }
        boolean salta = true;	
        try{
            MulticastSocket socket = new MulticastSocket(MCAST_PORT);
            socket.joinGroup(group);
            while(salta){
                byte[] buffer = new byte[DGRAM_BUF_LEN + 1024]; 
                DatagramPacket recv = new DatagramPacket(buffer,buffer.length);
                socket.receive(recv);
                byte [] data = recv.getData();
                Info info = (Info) SerializationUtils.deserialize(data);
                traducirInfoRecibir(info);
            } 
        }catch(IOException e){
            System.exit(2);
        }
    }
    
    public void traducirInfoRecibir(Info info){
        int tipo = info.getTipo();
        switch(tipo){
            case 1:
                InfoSolicitud infoSol = (InfoSolicitud) info;
                String md5 = infoSol.getMD5Solicitado();
                System.out.println("--->MD5 Solicitado: " + md5);
                if(existeArchivoPorMD5(md5)){
                    File f = obtenerArchivoPorMD5(md5);
                    System.out.println("--->Abriendo servidor de archivos..");
                    int nhosts = infoSol.getNumHosts();
                    ServidorFlujo sf = new ServidorFlujo(this.puerto, f, nhosts);
                    sf.start();
                }
            break;
            case 2:
                InfoHost infoServ = (InfoHost) info;
                if(this.ultimaBusqueda.getNomUsuario().equals(this.nomUsuario) && this.ultimaBusqueda.getDireccion().equals(this.direccionHost)){
                    this.adicionarATodosLosArchivos(infoServ);
                    this.adicionarListaResultadoBusqueda(infoServ);
                }
            break;
            case 3:
                ultimaBusqueda = (InfoBusqueda) info;
                if(this.ultimaBusqueda.getNomUsuario().equals(this.nomUsuario) && this.ultimaBusqueda.getDireccion().equals(this.direccionHost)){
                    this.listaBusqueda = new ArrayList<>();
                    this.todosLosArchivos = new ArrayList<>();
                }
                System.out.println("\n--->El usuario " + ultimaBusqueda.getNomUsuario() + " esta buscando desde " + ultimaBusqueda.getDireccion());
                InfoHost infoHost = new InfoHost(this.direccionHost, this.puerto, this.nomUsuario);
                ServidorMulticast sm = new ServidorMulticast(infoHost);
                sm.start();
            break;
            default:
                System.out.println("Mensaje default del cliente");
            break;
        }
    }
    
    private void adicionarATodosLosArchivos(InfoHost infoH){
        infoH.getInfoArchivos().forEach((archivo) -> {
            todosLosArchivos.add(archivo);
        });
    }

    private void adicionarListaResultadoBusqueda(InfoHost infoH){
        infoH.getInfoArchivos().forEach((archivo) -> {
            if(!estaEnLista(archivo)){
                resultadoBusqueda raux = new resultadoBusqueda(archivo,1);
                listaBusqueda.add(raux);
            }
            else
                listaBusqueda.get(indiceEnLista(archivo)).incrementarPoseedores();
        });
    }
    
    public void imprimirListaBusqueda(){
        for(int i = 0; i < listaBusqueda.size(); ++i)
            System.out.println("--->" + i + " - " + listaBusqueda.get(i).toString());
    }
    
    public void enviarBusqueda(int indice){
        String nomArchivo = listaBusqueda.get(indice).getArchivo().getNombre();
        String md5 = listaBusqueda.get(indice).getArchivo().getMD5();
        int numHosts = listaBusqueda.get(indice).getNumHostsPoseedores();
        System.out.println("------");
        System.out.println("Solicitando el archivo "+nomArchivo+"con MD5 "+md5+" a " + numHosts + " hosts");
        System.out.println("------");
        InfoSolicitud infoSol = new InfoSolicitud(md5, this.direccionHost, numHosts);
        
        ServidorMulticast sm = new ServidorMulticast(infoSol);
        sm.start();
        try {
            Thread.sleep(2000);
            int numParte = 0;
            ArrayList<ClienteFlujo> clientes = new ArrayList<>();
            for(DatosArchivo archivo : todosLosArchivos){
                if(archivo.getMD5().equals(md5)){
                    int puerto = archivo.getPuerto();
                    String dir = archivo.getDireccionHost();
                    ClienteFlujo cm = new ClienteFlujo(dir,puerto,numParte);
                    cm.start();
                    clientes.add(cm);
                    numParte++;
                }
            }
            for(ClienteFlujo cliente : clientes)
                cliente.join();
            ArrayList<byte[]> partes = new ArrayList<>();
            clientes.forEach((cliente) -> {
                partes.add(cliente.getParteDescargada());
            });
            guardarArchivo(partes,nomArchivo);
            System.out.println("--->El archivo " + nomArchivo + " fue descargado con exito");
        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean estaEnLista(DatosArchivo archivo){
        return listaBusqueda.stream().anyMatch((resultado) -> (resultado.getArchivo().getMD5().equals(archivo.getMD5())));
    }
    
    private int indiceEnLista(DatosArchivo archivo){
        for(int i = 0; i < listaBusqueda.size(); ++i)
            if(listaBusqueda.get(i).getArchivo().getMD5().equals(archivo.getMD5()))
                return i;
        return -1;
    }
    
    public boolean existeArchivoPorMD5(String md5){
        InfoHost infoHost = new InfoHost(this.direccionHost, this.puerto, this.nomUsuario);
        return infoHost.getInfoArchivos().stream().anyMatch((datos) -> (datos.getMD5().equals(md5)));
    }
    
    public File obtenerArchivoPorMD5(String md5){
        InfoHost infoHost = new InfoHost(this.direccionHost, this.puerto, this.nomUsuario);
        for(DatosArchivo datos : infoHost.getInfoArchivos())
            if(datos.getMD5().equals(md5))
                return new File(infoHost.getRutaDirectorio() + "/" + datos.getNombre());
        return null;
    }
    
    public void guardarArchivo(ArrayList<byte[]> partes, String nomArchivo){
        byte []resultado;
        int longitudTotal = 0;
        longitudTotal = partes.stream().map((parte) -> parte.length).reduce(longitudTotal, Integer::sum);
        System.out.println("--->Long total "+longitudTotal);
        resultado = new byte[longitudTotal];
        int index=0;
        int i;
        for(byte[] parte : partes){
            for(i=0;i<parte.length;i++)
                resultado[index+i] = parte[i];
            index+=i;
        }
        File archivo = new File("archivos/" + nomArchivo);
        String ruta = archivo.getAbsolutePath();
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(ruta);
            fos.write(resultado);
            fos.close();
        } catch (IOException ex) {}  
    }
}
