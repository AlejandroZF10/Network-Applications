package Flujo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Cliente {
    public static String finalpath = "C:\\Users\\alexz\\Desktop\\";
    public static String serverpath = "C:\\Users\\alexz\\Desktop\\Archivos\\";
        
    public static void sendFile(File file) throws IOException{
        Socket client = null;
        client = new Socket("127.0.0.1",5678);
        DataOutputStream dosNet = new DataOutputStream(client.getOutputStream());
        try {
            String nombre = file.getName();
            long tam = file.length();
            String path = file.getAbsolutePath();
            dosNet = new DataOutputStream(client.getOutputStream());
            try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
                long env = 0;
                int n = 0;
                dosNet.writeUTF(nombre);
                dosNet.flush();
                dosNet.writeLong(tam);
                dosNet.flush();
                dosNet.writeUTF(finalpath+nombre);
                dosNet.flush();
                while(env<tam){
                    byte[]b = new byte[3000];
                    n = dis.read(b);
                    dosNet.write(b,0,n);
                    dosNet.flush();
                    env = env + n;
                }//while
            }
            dosNet.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args){
        JFileChooser fileChooser = new JFileChooser(serverpath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int r = fileChooser.showOpenDialog(null);
        if(r == JFileChooser.APPROVE_OPTION){
            try {
                File file = fileChooser.getSelectedFile();
                sendFile(file);
                JOptionPane.showMessageDialog(null,"Directory "+file.getName()+" sent successfully");
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}