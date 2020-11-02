package amazon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {
    
    public static void sendObject(Object objeto, DataOutputStream output) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(objeto);
        oos.flush();
    }

    public static Object receiveObject(DataInputStream input) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(input);
        Object objeto = ois.readObject();
        return objeto;
    }
}