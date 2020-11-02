package clases;

import java.io.Serializable;

public class InfoBusqueda extends Info implements Serializable{
    String nomUsuario;
    String direccionSolicitud;     
    
    public InfoBusqueda(String direccionSolicitud, String nomUsuario){
        this.direccionSolicitud = direccionSolicitud;
        this.nomUsuario = nomUsuario;
        this.tipo = 3;
    }
    
    public String getNomUsuario(){
        return nomUsuario;
    }
    
    public String getDireccion(){
        return direccionSolicitud;
    }
}