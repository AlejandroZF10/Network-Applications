package Datagram;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    
    
    public static void main(String[] args){
        final int puerto = 5000;
        String pathFinal = "C:\\Users\\alexz\\Desktop\\";
        try {
            System.out.println("Servidor UDP iniciado");
            DatagramSocket socketUDP = new DatagramSocket(puerto);           
            while(true){
                long size = 0;
                byte[] buffer = new byte[1500];
                DatagramPacket name = new DatagramPacket(buffer,buffer.length);
                socketUDP.receive(name);
                String aux_name = new String(name.getData());
                String[] split_name = aux_name.split("@");
                String nombre = split_name[0];
                System.out.println(nombre);
                /*--------------------------------------*/
                int puerto_cliente = name.getPort();
                InetAddress direccion = name.getAddress();
                /*--------------------------------------*/
                String respuesta_name = "Nombre recibido@";
                buffer = respuesta_name.getBytes();
                DatagramPacket request_name = new DatagramPacket(buffer,buffer.length,direccion,puerto_cliente);
                socketUDP.send(request_name);
                /*--------------------------------------*/
                buffer = new byte[1500];
                DatagramPacket address = new DatagramPacket(buffer,buffer.length);
                socketUDP.receive(address);
                String aux_path = new String(address.getData());
                String[] split_path = aux_path.split("@");
                String path = split_path[0];
                System.out.println(path);
                /*--------------------------------------*/
                String respuesta_path = "Ruta recibida@";
                buffer = respuesta_path.getBytes();
                DatagramPacket request_path = new DatagramPacket(buffer,buffer.length,direccion,puerto_cliente);
                socketUDP.send(request_path);
                /*--------------------------------------*/
                File archivo = new File(path);
                size = archivo.length();
                int contador = 1, inicio_ciclo = 0;
                int num_packets = (int)(size/1024);
                byte[] file = new byte[(int)size];
                while(contador<=num_packets){
                    byte[] aux_Array;
                    if(contador == num_packets)
                        aux_Array = new byte[(int)size-inicio_ciclo];
                    else
                        aux_Array = new byte[1024];
                    DatagramPacket aux_File = new DatagramPacket(aux_Array,aux_Array.length);
                    socketUDP.receive(aux_File);
                    byte[] datos = aux_File.getData();
                    int index = inicio_ciclo;
                    for(int i = 0; i<datos.length ;i = i + 1)
                        file[index++] = datos[i];
                    inicio_ciclo = inicio_ciclo + 1024;
                    String request = "Paquete recibido";
                    byte[] buffer_aux = request.getBytes();
                    DatagramPacket request_packet = new DatagramPacket(buffer_aux,buffer_aux.length,direccion,puerto_cliente);
                    socketUDP.send(request_packet);
                    contador = contador + 1;
                }
                System.out.println(file[0]+" "+file[(int)size-1]+" "+file.length);
                File file_aux = new File(pathFinal+nombre);
                try {
                    OutputStream os = new FileOutputStream(file_aux); 
                    os.write(file); 
                    System.out.println("Successfully byte inserted");
                    os.close(); 
                } catch (IOException e) { 
                    System.out.println("Exception: " + e); 
                } 
            }
            
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
