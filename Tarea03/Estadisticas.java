import java.io.Serializable;

public class Estadisticas implements Serializable{
    private final String nombre;
    private final String tiempo;
    private final String status;

    public Estadisticas(String nombre, String tiempo, String status){
        this.nombre = nombre;
        this.tiempo = tiempo;
        this.status = status;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getTiempo(){
        return this.tiempo;
    }

    public String getStatus(){
        return this.status;
    }
}
