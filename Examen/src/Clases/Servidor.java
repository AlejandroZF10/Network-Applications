package Clases;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Servidor {
    public static int port = 5678;
    public static DataInputStream input = null;
    public static DataOutputStream output = null;
    public static String inventario = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Examenes\\Palabras.txt";
    public static ArrayList<String> palabras = new ArrayList<String>();
    public static ArrayList<Estadistica> estadistica = new ArrayList<Estadistica>();
    
    private static void readFile(String path) throws IOException{
        int i = 0; String cadena = "";
        FileReader file = new FileReader(path);
        BufferedReader buffer = new BufferedReader(file);
        while ((cadena = buffer.readLine())!=null){
            String toUpperCase = cadena.toUpperCase();
            palabras.add(toUpperCase);
        }
    }
    
    private static void validateFile(){
        for(int i = 1; i<palabras.size() ;i++){
            if(palabras.get(i).length()>15){
                JOptionPane.showMessageDialog(null,"Longitud excedida: "+palabras.get(i));
                System.exit(0);
            }
        }
    }
    
    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(port);
        System.out.println("Conexión establecida, esperando clientes...\n");
        readFile(inventario); validateFile();
        while(true){
            Socket client = server.accept();
            System.out.println("Llegó un cliente desde "+client.getInetAddress()+":"+client.getPort()+" esperando...\n");
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            while(true){
                try{
                    int action = input.readInt();
                    switch(action){
                        case 0:
                            Utils.sendObject(palabras,output);
                            System.out.println("Enviando lista de palabras disponibles...");
                        break;
                        case 1:
                            Utils.sendObject(estadistica,output);
                            System.out.println("Enviando estadisticas disponibles...");
                        break;
                        case 2:
                            estadistica = (ArrayList<Estadistica>)Utils.receiveObject(input);
                        break;
                    }
                }catch (IOException e) {
                    System.out.println("Ocurrió un error con el cliente, desconectando...");
                    break;
                }
            }
        }
    }
}