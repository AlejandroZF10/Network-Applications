package componentes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorFlujo extends Thread{
    int npuerto;
    File archivo;
    int nhosts;
    
    public ServidorFlujo(int npuerto, File archivo, int nhosts){
        this.npuerto = npuerto;
        this.archivo = archivo;
        this.nhosts = nhosts;
       
    }
    
    @Override
    public void run(){
        try {
            ServerSocket s = new ServerSocket(npuerto);
            Socket cl = s.accept();
            int nparte;
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            nparte = dis.readInt();
            System.out.println("--->Imprimiendo el archivo " + archivo.getName() + " con longitud " + archivo.length());
            int sizeOfFiles = (int) archivo.length()/nhosts;
            int partCounter = 0;
            byte [][]partes = new byte[nhosts][];
            byte[] parte = new byte[sizeOfFiles];

            try (FileInputStream fis = new FileInputStream(archivo);
                BufferedInputStream bis = new BufferedInputStream(fis)) {
                int bytesAmount = 0;
                while ((bytesAmount = bis.read(parte)) > 0) {
                    partes[partCounter] = parte;
                    partCounter++; 
                    if(partCounter==nhosts-1&&archivo.length()%nhosts!=0)                        
                        parte = new byte[(int)sizeOfFiles+(int)archivo.length()%nhosts];
                }
            }            
            parte = partes[nparte];
            System.out.println("--->Enviando parte " + nparte + " con longitud " + parte.length);
            dos.writeInt(parte.length);
            dos.write(parte);
            dos.flush();
            cl.close();
            s.close();
        } catch (IOException ex) {}
    } 
}