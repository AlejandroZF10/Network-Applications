import java.io.Serializable;
import java.util.ArrayList;

public class Bucket implements Serializable{
    private int div,indice,inferior,superior ;
    private ArrayList<Integer>cubeta = new ArrayList<Integer>();

    Bucket(int div, int indice){
        this.div = div;
        this.indice = indice;
        this.superior = this.div * this.indice-1;
        this.inferior = (this.div*this.indice)-this.div;
    }

    int getSuperior(){
        return superior;
    }
    int getInferior(){
        return inferior;
    }
    void setIngresaNum(int A){
        cubeta.add(A);
    }
    ArrayList<Integer> getArray(){
        return cubeta;
    }

    public String toString(){
        return String.valueOf(indice);
    }
}
