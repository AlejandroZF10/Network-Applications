package clases;

import java.io.Serializable;

public class InfoCliente extends Info implements Serializable{
    private final String ip;
    private final String nomUsuario;
    
    public InfoCliente(String nomUsuario, String ip){
        this.nomUsuario = nomUsuario;
        this.ip = ip;
    }
    
    public String getNomUsuario() {
        return nomUsuario;
    }

    public String getIp() {
        return ip;
    }
}