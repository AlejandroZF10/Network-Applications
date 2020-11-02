import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Cliente extends Thread{
    private ArrayList<Integer> rand;
    private Bucket cubeta;
    private int puerto;

    Cliente(ArrayList<Integer> rand, Bucket cubeta, int puerto){
        this.rand = rand;
        this.cubeta = cubeta;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        System.out.println("\n-----> Cubeta: " + cubeta);
        for (int i = 0; i < rand.size(); i++) {
            if(rand.get(i)>=cubeta.getInferior() && rand.get(i)<=cubeta.getSuperior())
                cubeta.setIngresaNum(rand.get(i));
        }

        try{
            String host = "localhost";
            Socket client = new Socket(host,puerto);
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            oos.writeObject(cubeta); oos.flush();
            System.out.println("-----> Cubeta "+String.valueOf(cubeta)+" enviada al servidor");
            Bucket bucket_aux = (Bucket) ois.readObject();
            System.out.println("-----> Cubeta "+String.valueOf(bucket_aux)+" recibida del servidor con "+bucket_aux.getArray().size());
            System.out.println("----- FIN DEL PROGRAMA -----\n");
            this.cubeta = bucket_aux;
            for (Integer i : cubeta.getArray())
                System.out.println("-----> "+i);
            ois.close();
            oos.close();
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
