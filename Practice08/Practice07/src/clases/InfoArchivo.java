package clases;

public class InfoArchivo extends Info{
    private final String nombre;
    private final String MD5;
    private final long size;
    
    public InfoArchivo(String nombre, String MD5, long size){
        this.tipo = 0;
        this.nombre = nombre;
        this.MD5 = MD5;
        this.size = size;
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getMD5() {
        return MD5;
    }

    public long getTamanio() {
        return size;
    }
}