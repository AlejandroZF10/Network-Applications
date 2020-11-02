package clases;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class InfoHost extends Info implements Serializable{
    
    private String direccionHost;
    private String rutaDirectorio;
    private String nomUsuario;
    private ArrayList<DatosArchivo> infoArchivos;
    private int puerto;
    
    public InfoHost(String direccionHost, int puerto, String nomUsuario){
        
        this.nomUsuario = nomUsuario;
        this.direccionHost = direccionHost;
        this.puerto = puerto;
        
        this.tipo = 2;
        infoArchivos = new ArrayList<>();
        
        File directorio = new File("archivos");
        this.rutaDirectorio = directorio.getAbsolutePath();
        File[] archivos = directorio.listFiles();
        
        for(int i = 0; i < archivos.length; ++i){
            if(archivos[i].isFile()){
                try{
                    String nombre = archivos[i].getName();
                    long tamanio = archivos[i].length();
                    String md5 = MD5Checksum.getMD5Checksum(this.rutaDirectorio + "/" + nombre);
                    DatosArchivo infArch = new DatosArchivo(nombre,md5,tamanio,this.getRutaDirectorio() + "/" + nombre, this.getDireccionHost(), this.getPuerto());
                    this.infoArchivos.add(infArch);
                }
                catch(Exception e){
                    e.printStackTrace();
                }     
                
            }
        }
    }
    
    public void enlistarArchivos(){
        System.out.println("Direccion del servidor: " + this.getDireccionHost());
        for(DatosArchivo infArch : this.getInfoArchivos()){
            System.out.println("-----");
            System.out.println("Nombre: " + infArch.getNombre());
            System.out.println("MD5: " + infArch.getMD5());
            System.out.println("Tamanio: " + infArch.getTamanio());
        }
    }

    public String getDireccionHost() {
        return direccionHost;
    }

    public String getRutaDirectorio() {
        return rutaDirectorio;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public ArrayList<DatosArchivo> getInfoArchivos() {
        return infoArchivos;
    }

    public int getPuerto() {
        return puerto;
    }
}
