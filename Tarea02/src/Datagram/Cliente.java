package Datagram;

import java.io.*;
import java.net.*;
import java.nio.*;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }
    
    public static void main(String[] args){
        final int puerto = 5000;
        try {
            long size = 0;
            String name = "";
            File archivo = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = fileChooser.showOpenDialog(fileChooser);
            if (result != JFileChooser.CANCEL_OPTION) {
                /*--------------------------------------*/
                archivo = fileChooser.getSelectedFile();
                name = archivo.getName()+"@";
                size = archivo.length();
                /*--------------------------------------*/
                InetAddress direccion = InetAddress.getByName("localhost");
                DatagramSocket socketUDP = new DatagramSocket();
                byte[] buffer_name = name.getBytes();
                DatagramPacket nombre = new DatagramPacket(buffer_name,buffer_name.length,direccion,puerto);
                socketUDP.send(nombre);
                /*--------------------------------------*/
                DatagramPacket request_name = new DatagramPacket(buffer_name,buffer_name.length);
                socketUDP.receive(request_name);
                String respuesta_name = new String(request_name.getData());
                String[] split_name = respuesta_name.split("@");
                System.out.println(split_name[0]);
                /*--------------------------------------*/
                String path = archivo.getCanonicalPath()+"@";
                byte[] buffer_address = path.getBytes();
                DatagramPacket address = new DatagramPacket(buffer_address,buffer_address.length,direccion,puerto);
                socketUDP.send(address);
                /*--------------------------------------*/
                DatagramPacket request_path = new DatagramPacket(buffer_name,buffer_name.length);
                socketUDP.receive(request_path);
                String respuesta_path = new String(request_path.getData());
                String[] split_path = respuesta_path.split("@");
                System.out.println(split_path[0]);
                /*--------------------------------------*/
                byte[] bytesArray = new byte[(int) archivo.length()];
                FileInputStream fis = new FileInputStream(archivo);
                fis.read(bytesArray); fis.close();
                int contador = 1, inicio_ciclo = 0;
                int num_packets = (int)(size/1024);
                while(contador<=num_packets){
                    int index = 0;
                    byte[] aux_Array = null;
                    if(contador == num_packets){
                        aux_Array = new byte[(int)size - inicio_ciclo];
                        for(int i = inicio_ciclo; i<(int)size ;i = i + 1)
                            aux_Array[index++] = bytesArray[i];
                    }else{
                        aux_Array = new byte[1024];
                        for(int i = inicio_ciclo; i<(inicio_ciclo+1024) ;i = i + 1)
                            aux_Array[index++] = bytesArray[i];
                        inicio_ciclo = inicio_ciclo + 1024;
                    }
                    DatagramPacket packet = new DatagramPacket(aux_Array,aux_Array.length,direccion,puerto);
                    socketUDP.send(packet);
                    byte[] aux_Array_request = new byte[1024];
                    DatagramPacket request_packet = new DatagramPacket(aux_Array_request,aux_Array_request.length);
                    socketUDP.receive(request_packet);
                    contador = contador + 1;   
                }
                System.out.println("File sent successfully!");
                /*--------------------------------------*/
                socketUDP.close();
            }
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
