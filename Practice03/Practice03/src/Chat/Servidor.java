package Chat;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread{
    public static DatagramPacket packet;
    public static String nombres;
    
    @Override
    public void run(){
        try{
            ServerSocket serversocket = new ServerSocket(8000);
            while(true){
                Socket cliente = serversocket.accept();
                System.out.println("Conectado");
                try{
                    ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
                    if(ois.readBoolean())
                        getFile(cliente,ois, oos);
                }catch(IOException ex){
                    System.out.println("Error en las conexiones");
                }catch (Exception ex){
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        catch(IOException e){}
    }
    
    public void getFile(Socket client, ObjectInputStream ois, ObjectOutputStream oos) throws Exception{
        String destino = ois.readUTF();
        String filename = ois.readUTF();
        String path = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Practice03\\";
        if(destino.equals("Todos"))
            path = path+"Servidor\\"+filename;
        else
            path = path+destino+"\\"+filename;
        File file = new File(path);
        long tam = ois.readLong();
        long r = 0;
        int n = 0;
        FileOutputStream fos = new  FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] buffer = new byte [1500];
        while (r<tam){
            n = ois.read(buffer);
            bos.write(buffer,0,n);
            bos.flush();
            r = r + n;
        }
        fos.close(); bos.close();
        ois.close(); oos.close();
        client.close();
        if(destino.equals("Todos")){
            String[] usuarios = nombres.split("@");
            for(int i = 1; i<usuarios.length ;i=i+1){
                String nuevaRuta = path.replace("Servidor",usuarios[i]);
                File fichero = new File(nuevaRuta);
                try {
                    InputStream in = new FileInputStream(file);
                    OutputStream out = new FileOutputStream(fichero);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0)
                        out.write(buf, 0, len);
                   in.close(); out.close();
                }catch (IOException ioe){}
            }
        }
    }
    
    public static void main(String args[]){
        try{
            Servidor server = new Servidor();
            server.start();
            nombres = "Todos";
            String message = "";
            //MulticastSocket socketmulticast = new MulticastSocket(7778);
            MulticastSocket socketmulticast = new MulticastSocket(8000);
            socketmulticast.setReuseAddress(true);
            socketmulticast.setTimeToLive(255);
            InetAddress gpo = InetAddress.getByName("232.0.0.10");
            socketmulticast.joinGroup(gpo);
            System.out.println("Servicio iniciado.. comienza envio de anuncios");
            while(true){
                packet = new DatagramPacket(new byte[1500],1500);
                socketmulticast.receive(packet);
                message = new String(packet.getData());
                System.out.println(message);
                String parts[];
                parts = message.split("@" , 4);
                if(parts[0].equalsIgnoreCase("Close")){
                    if(nombres.contains(parts[1]))
                        nombres = nombres.replace("@"+parts[1],"@****"+parts[1]);
                    message = message + "---" + nombres + "@";
                    System.out.println("" + message);
                    byte[] buffer = message.getBytes();
                    packet = new DatagramPacket(buffer,buffer.length,gpo,7777);
                    socketmulticast.send(packet);
                    if(nombres.contains(parts[1]))
                        nombres = nombres.replace("@****"+parts[1],"");
                }else{
                    if(parts[0].equalsIgnoreCase("union")){
                        nombres = nombres+ "@" + parts[1] ;
                        message = message + "---" + nombres;
                        System.out.println("" + message);
                        byte[] buffer = message.getBytes();
                        packet = new DatagramPacket(buffer,buffer.length,gpo,7777);
                        socketmulticast.send(packet);
                    }else{
                        byte[] buffer = packet.getData();
                        packet = new DatagramPacket(buffer,buffer.length,gpo,7777);
                        socketmulticast.send(packet);
                    }
                }
            }
        }catch(IOException e){}
    }
}