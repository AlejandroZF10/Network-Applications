package non_blocking;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server{
    static ArrayList<Address> client_address = new ArrayList<>();

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.println("Display name: "+netint.getDisplayName());
        System.out.println("Name: "+netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        Collections.list(inetAddresses).forEach((inetAddress) -> {
            System.out.println("InetAddress: "+inetAddress);
        });
        System.out.println();
    }
    
    public static void main(String[] args){
        int port = 10;
        String nombres = "Todos@";
        
        try {            
            NetworkInterface ni = NetworkInterface.getByIndex(1);
            
            DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            
            DatagramSocket socket = channel.socket();
            SocketAddress address = new InetSocketAddress(port);
            socket.bind(address);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            
            InetAddress group = InetAddress.getByName("230.0.0.1");
            channel.join(group, ni);
            
            System.out.println("New group "+group.getHostAddress()+" ... \nWaiting datagrams...");
            Selector selector = Selector.open();
            
            while(true){
                String accion = "";
                SocketAddress client = null;
                channel.register(selector,SelectionKey.OP_READ);
                ByteBuffer buffer_read = ByteBuffer.allocateDirect(1024);
                selector.select(10000);
                Set readyKeys = selector.selectedKeys();
                Iterator iterator = readyKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = (SelectionKey)iterator.next();
                    iterator.remove();
                    if(key.isReadable()){
                        buffer_read.clear();
                        client = channel.receive(buffer_read);
                        buffer_read.flip();
                        accion = StandardCharsets.UTF_8.decode(buffer_read).toString();
                    }
                }
                String request = accion.split("@")[0];
                switch(request){
                    case "Union":
                        String origen = accion.split("@")[1];
                        client_address.add(new Address(client,origen));
                        System.out.println(origen+"--->"+client.toString());
                        nombres = nombres + origen + "@";
                        String message = "Union@"+origen+"@Todos@"+origen+" se ha unido al grupo@---"+nombres;
                        channel.register(selector,SelectionKey.OP_WRITE);
                        ByteBuffer buffer_write = ByteBuffer.allocateDirect(1024);
                        selector.select(5000);
                        Iterator<SelectionKey>it = selector.selectedKeys().iterator();
                        while(it.hasNext()){
                            SelectionKey key = (SelectionKey)it.next();
                            it.remove();
                            if(key.isWritable()){
                                for(int i = 0; i<client_address.size() ;i++){
                                    byte[] bytes = message.getBytes();
                                    buffer_write.clear();
                                    buffer_write.put(bytes);
                                    buffer_write.flip();
                                    channel.send(buffer_write,client_address.get(i).getAddress());
                                }
                            }
                        }
                    break;
                    /********************************************************************************/
                    case "Send":
                        String origen_send = accion.split("@")[1];
                        String destino_send = accion.split("@")[2];
                        String contenido_send = accion.split("@")[3];
                        String message_send = "Send@"+origen_send+"@"+destino_send+"@"+contenido_send+"@";
                        channel.register(selector,SelectionKey.OP_WRITE);
                        ByteBuffer buffer_send = ByteBuffer.allocateDirect(1024);
                        selector.select(5000);
                        
                        Iterator<SelectionKey>it_send = selector.selectedKeys().iterator();
                        while(it_send.hasNext()){
                            SelectionKey key = (SelectionKey)it_send.next();
                            it_send.remove();
                            if(key.isWritable()){
                                if(destino_send.equals("Todos")){
                                    for(int i = 0; i<client_address.size() ;i++){
                                        byte[] bytes = message_send.getBytes();
                                        buffer_send.clear();
                                        buffer_send.put(bytes);
                                        buffer_send.flip();
                                        channel.send(buffer_send,client_address.get(i).getAddress());
                                    }
                                }else{
                                    SocketAddress aux = null;
                                    for(int i = 0; i<client_address.size() ;i++){
                                        if(client_address.get(i).getName().equals(destino_send)){
                                            aux = client_address.get(i).getAddress();
                                            break;
                                        }
                                    }
                                    byte[] bytes = message_send.getBytes();
                                    buffer_send.clear();
                                    buffer_send.put(bytes);
                                    buffer_send.flip();
                                    channel.send(buffer_send,aux);
                                }
                            }
                        }
                    break;
                    /********************************************************************************/
                    case "Close":
                        String origen_close = accion.split("@")[1];
                        for(int i = 0; i<client_address.size() ;i++)
                            if(origen_close.equals(client_address.get(i).getName()))
                                client_address.remove(i);                   
                        System.out.println(origen_close+"---><"+client.toString());
                        if(nombres.contains(origen_close))
                            nombres = nombres.replace("@"+origen_close,"");
                        String message_close = "Close@"+origen_close+"@Todos@"+origen_close+" ha abandonado el grupo@---"+nombres;
                        channel.register(selector,SelectionKey.OP_WRITE);
                        ByteBuffer buffer_close = ByteBuffer.allocateDirect(1024);
                        selector.select(5000);
                        Iterator<SelectionKey>it_close = selector.selectedKeys().iterator();
                        while(it_close.hasNext()){
                            SelectionKey key = (SelectionKey)it_close.next();
                            it_close.remove();
                            if(key.isWritable()){
                                for(int i = 0; i<client_address.size() ;i++){
                                    byte[] bytes = message_close.getBytes();
                                    buffer_close.clear();
                                    buffer_close.put(bytes);
                                    buffer_close.flip();
                                    channel.send(buffer_close,client_address.get(i).getAddress());
                                }
                            }
                        }
                    break;
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}