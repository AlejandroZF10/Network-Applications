package componentes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import clases.*;
import java.io.IOException;
import org.apache.commons.lang.SerializationUtils;

public class ServidorMulticast extends Thread{
    public static final String MCAST_ADDR = "230.0.0.1";
    public static final int MCAST_PORT = 9013;
    public static final int DGRAM_BUF_LEN = 512;
    private final Info infoAEnviar;
    
    public ServidorMulticast(Info infoAEnviar){
        this.infoAEnviar = infoAEnviar;
    }
    
    @Override
    public void run(){
        InetAddress group = null;
        try{
            group = InetAddress.getByName(MCAST_ADDR);
        }catch(UnknownHostException e){
            System.exit(1);
        }
        try{
            MulticastSocket socket = new MulticastSocket(MCAST_PORT);
            socket.joinGroup(group);
            byte[] data = SerializationUtils.serialize(infoAEnviar);
            System.out.println("--->Longitud enviada: " + data.length);
            DatagramPacket packet = new DatagramPacket(data,data.length,group,MCAST_PORT);
            socket.send(packet);
            socket.close();    		
        }catch(IOException e){
            System.exit(2);
        }
        try{
            Thread.sleep(1000*5);
        }catch(InterruptedException ie){}
    }   
}