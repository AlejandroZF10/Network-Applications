package clases;

import java.io.Serializable;

public class DatosArchivo implements Serializable{
    private String direccionHost;
    private final int puerto;
    private final String nombre;
    private final String MD5;
    private final long tamanio;
    private final String ruta; 
    
    public DatosArchivo(String nombre, String MD5, long tamanio, String ruta, String direccionHost, int puerto){
        this.nombre = nombre;
        this.MD5 = MD5;
        this.tamanio = tamanio;
        this.ruta = ruta;
        this.direccionHost = direccionHost;
        this.puerto = puerto;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getMD5() {
        return MD5;
    }

    public long getTamanio() {
        return tamanio;
    }

    public String getDireccionHost() {
        return direccionHost;
    }

    public void setDireccionHost(String direccionHost) {
        this.direccionHost = direccionHost;
    }

    public int getPuerto() {
        return puerto;
    }

    public String getRuta() {
        return ruta;
    }
}
