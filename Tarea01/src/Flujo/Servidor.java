package Flujo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private Socket client;
    private final ServerSocket server;
    private DataInputStream disNet;
    private DataOutputStream dosNet;
    
    public Servidor(int puerto)throws IOException{
        server = new ServerSocket(puerto);
        server.setReuseAddress(true);
    }

    public void aceptarConexion()throws IOException{
        client = server.accept();
	disNet = new DataInputStream(client.getInputStream());
	dosNet = new DataOutputStream(client.getOutputStream());
    }
    
    public void enviaArchivo() throws IOException{
        String nombre = disNet.readUTF();
        long tam = disNet.readLong();
        String finalpath = disNet.readUTF();
        System.out.println("Preparado para recibir archivo "+nombre+" desde "+client.getInetAddress()+":"+client.getPort());
        dosNet = new DataOutputStream(new FileOutputStream(finalpath));
        int n = 0, porcentaje = 0;
        long r = 0;
        while(r<tam){
            byte[] b = new byte[3000];
            n = disNet.read(b);
            dosNet.write(b,0,n);
            dosNet.flush();
            r = r + n;
            porcentaje = (int)((r*100)/tam);
            System.out.println("\rPorcentaje: "+porcentaje+"%");
        }
        dosNet.close();
        disNet.close();
        client.close();
    }
    
    public static void main(String[] args){
        try {
            Servidor servidor= new Servidor(5678);
            System.out.println("Servidor iniciado");
            while(true){
                System.out.println("Esperando conexiones");
                servidor.aceptarConexion();
                System.out.println("Conexión recibida");
                servidor.enviaArchivo();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}