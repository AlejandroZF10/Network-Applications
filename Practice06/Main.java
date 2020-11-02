import java.util.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int puerto = 1500;
        int cubeta = Integer.parseInt(JOptionPane.showInputDialog(null,"Introduce the size bucket: "));

        Servidor [] servidor = new Servidor[cubeta];
        for (Servidor i : servidor) {
            i = new Servidor(puerto);
            i.start();
            puerto = puerto + 1;
        }

        ArrayList<Integer> rand = new ArrayList<Integer>();
        Cliente [] cliente = new Cliente[cubeta];
        Bucket [] bucket = new Bucket[cubeta];
        makeList(rand);
        int div = 1000/cubeta;
        puerto = 1500;
        for(int i = 0; i<cubeta; i++){
            bucket[i] = new Bucket(div, i+1);
            cliente[i]= new Cliente(rand, bucket[i], puerto);
            cliente[i].start();
            cliente[i].join();
            puerto = puerto + 1;
        }
    }

    static void makeList(ArrayList<Integer> rand) {
        for (int i = 0; i < 4000; i++) {
            int num = (int) (Math.random()*999);
            rand.add(num);
        }
    }
}
