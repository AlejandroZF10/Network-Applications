package Clases;

import java.io.Serializable;

public class Estadistica implements Serializable {
    private final String nombre;
    private final int minutos;
    private final int segundos;
    private final String status;
    private final String modalidad;
    private final String dificultad;
    
    public Estadistica(String nombre, int minutos, int segundos, String status, String modalidad, String dificultad){
        this.nombre = nombre;
        this.minutos = minutos;
        this.segundos = segundos;
        this.status = status;
        this.modalidad = modalidad;
        this.dificultad = dificultad;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public int getMinutos(){
        return this.minutos;
    }
    
    public int getSegundos(){
        return this.segundos;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public String getModalidad(){
        return this.modalidad;
    }
    
    public String getDificultad(){
        return this.dificultad;
    }
}