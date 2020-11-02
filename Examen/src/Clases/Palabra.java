package Clases;

import java.io.Serializable;

public class Palabra implements Serializable {
    private final String palabra;
    private final int xInicial;
    private final int yInicial;
    private final int xFinal;
    private final int yFinal;
    private boolean status;
    
    public Palabra(String palabra, int xInicial, int yInicial, int xFinal, int yFinal, boolean status){
        this.palabra = palabra;
        this.xInicial = xInicial;
        this.yInicial = yInicial;
        this.xFinal = xFinal;
        this.yFinal = yFinal;
        this.status = status;
    }
    
    public String getPalabra(){
        return this.palabra;
    }
    
    public int getxInicial(){
        return this.xInicial;
    }
    
    public int getyInicial(){
        return this.yInicial;
    }
    
    public int getxFinal(){
        return this.xFinal;
    }
    
    public int getyFinal(){
        return this.yFinal;
    }
    
    public boolean getStatus(){
        return this.status;
    }
    
    public void setStatus(boolean status){
        this.status = status;
    }
}