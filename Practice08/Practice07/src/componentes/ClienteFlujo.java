package componentes;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClienteFlujo extends Thread{
    private final String ipServidor;
    private final int puertoServidor;
    private final int parteADescargar;
    private byte[] parteDescargada;
    
    public ClienteFlujo(String ipServidor, int puertoServidor, int parteADescargar){
        this.ipServidor = ipServidor;
        this.puertoServidor = puertoServidor;
        this.parteADescargar = parteADescargar;
    }
    
    @Override
    public void run(){
        try{
            Socket cl = new Socket(ipServidor,puertoServidor);
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            dos.writeInt(parteADescargar);
            dos.flush();
            System.out.println("--->Descargando parte " + parteADescargar + " del host con direccion " + ipServidor + " del puerto " + puertoServidor);
            
            int longitud = dis.readInt();
            parteDescargada = new byte[longitud];
            dis.readFully(parteDescargada);
            cl.close();
            System.out.println("--->Parte " + parteADescargar + " descargada con una longitud de " + longitud);
        }
        catch(IOException e){}
    }

    public byte[] getParteDescargada() {
        return parteDescargada;
    }
}