import java.io.*;
import java.net.*;
import java.util.Collections;

public class Servidor extends Thread {
    private int puerto;

    Servidor(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(puerto);
            System.out.println("Servidor iniciado en el puerto" + puerto + " ESPERANDO AL CLIENTE");
            while(true) {
                Socket client = server.accept();
                ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                Bucket buck = (Bucket) ois.readObject();
                System.out.println("-----> Cubeta "+String.valueOf(buck)+" en servidor con "+buck.getArray().size()+" elementos");
                Collections.sort(buck.getArray());
                oos.writeObject(buck); oos.flush();
                System.out.println("-----> Cubeta "+String.valueOf(buck)+" ordenada con "+buck.getArray().size()+" elementos");
                ois.close(); oos.close();
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
