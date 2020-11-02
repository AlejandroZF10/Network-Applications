package clases;

import java.io.File;
import java.util.ArrayList;

public class InfoServidor extends Info{
    private int puerto;
    private String direccion;
    private ArrayList<InfoArchivo> infoArchivos;
    
    public InfoServidor(String direccion, int puerto){
        this.direccion = direccion;
        this.puerto = puerto;
        this.tipo = 2;
        infoArchivos = new ArrayList<>();
        
        File directorio = new File("archivos");
        directorio = directorio.getAbsoluteFile();
        File[] archivos = directorio.listFiles();
        
        for(int i = 0; i < archivos.length; ++i){
            if(archivos[i].isFile()){
                try{
                    String nombre = archivos[i].getName();
                    long tamanio = archivos[i].length();
                    String md5 = MD5Checksum.getMD5Checksum(directorio + "/" + nombre);
                    InfoArchivo infArch = new InfoArchivo(nombre,md5,tamanio);
                    this.infoArchivos.add(infArch);
                }
                catch(Exception e){}      
            }
        }
    }
    
    public void enlistarArchivos(){
        System.out.println("--->Direccion del servidor: " + direccion);
        System.out.println("--->Puerto del servidor: " + puerto);
        infoArchivos.stream().map((infArch) -> {
            System.out.println("-----");
            return infArch;
        }).map((infArch) -> {
            System.out.println("Nombre: " + infArch.getNombre());
            return infArch;
        }).map((infArch) -> {
            System.out.println("MD5: " + infArch.getMD5());
            return infArch;
        }).forEachOrdered((infArch) -> {
            System.out.println("Tamanio: " + infArch.getTamanio());
        });
    }
}