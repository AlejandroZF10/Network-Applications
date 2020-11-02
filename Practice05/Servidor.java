import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.logging.*;
import java.util.concurrent.*;

public class Servidor {
    public static void main(String[] args){
        int puerto,size;

        try{
            puerto = Integer.parseInt(JOptionPane.showInputDialog(null,"Introduce the port: "));
            size = Integer.parseInt(JOptionPane.showInputDialog(null,"Connection pool size: "));

            //Pool de conexiones
            ExecutorService pool = Executors.newFixedThreadPool(size);
            System.out.println("Servidor iniciado\nPool de conexiones = "+size);
            ServerSocket server = new ServerSocket(puerto);
            System.out.println("Servidor iniciado http://localhost:"+puerto);
            System.out.println("Esperando clientes...");

            while(true){
                Socket client = server.accept();
                Handler manejador = new Handler(client);
                pool.execute(manejador);
            }
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
