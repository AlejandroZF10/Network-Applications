package non_blocking;

import java.io.Serializable;
import java.net.SocketAddress;

public class Address implements Serializable{
    private SocketAddress address = null;
    private String username = "";

    public Address(SocketAddress address,String username){
        this.address = address;
        this.username = username;
    }

    public SocketAddress getAddress(){
        return this.address;
    }

    public String getName(){
        return this.username;
    }
}