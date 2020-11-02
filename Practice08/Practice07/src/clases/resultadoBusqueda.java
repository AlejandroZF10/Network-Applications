package clases;

public class resultadoBusqueda {  
    private final DatosArchivo archivo;
    private int numHostsPoseedores;
    
    public resultadoBusqueda(DatosArchivo archivo, int numHostsPoseedores){
        this.archivo = archivo;
        this.numHostsPoseedores = numHostsPoseedores;
    }

    public DatosArchivo getArchivo() {
        return archivo;
    }

    public int getNumHostsPoseedores() {
        return numHostsPoseedores;
    }

    public void setNumHostsPoseedores(int numHostsPoseedores) {
        this.numHostsPoseedores = numHostsPoseedores;
    }
    
    @Override
    public String toString(){
        return "Nombre: " + archivo.getNombre() + " Numero de hosts que lo tienen: " + numHostsPoseedores;
    }
    
    public void incrementarPoseedores(){
        this.numHostsPoseedores += 1;
    }
}