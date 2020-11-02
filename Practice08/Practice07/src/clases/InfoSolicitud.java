package clases;

import java.io.Serializable;

public class InfoSolicitud extends Info implements Serializable{
    private final int numHosts;
    private final String direccionHost;
    private final String MD5Solicitado;
    private String nomUsuario;
    
    public InfoSolicitud(String MD5Solicitado, String dirHost, int numHosts){
        this.tipo = 1;
        this.MD5Solicitado = MD5Solicitado;
        this.direccionHost = dirHost;
        this.numHosts = numHosts;
    }

    public String getDireccionHost() {
        return direccionHost;
    }

    public String getMD5Solicitado() {
        return MD5Solicitado;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public int getNumHosts() {
        return numHosts;
    }  
}