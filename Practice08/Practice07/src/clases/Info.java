package clases;

import java.io.Serializable;

public class Info implements Serializable{
    protected int tipo;
    
    public int getTipo(){
        return tipo;
    }
    
    public void setTipo(int tipo){
        this.tipo = tipo;
    }
}