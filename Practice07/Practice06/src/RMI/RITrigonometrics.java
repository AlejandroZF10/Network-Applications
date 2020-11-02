package RMI;

import java.rmi.Remote;

public interface RITrigonometrics extends Remote{
    public double seno(double angulo)throws Exception;
    public double coseno(double angulo)throws Exception;
    public double tangente(double angulo)throws Exception;
    public double cotangente(double angulo)throws Exception;
    public double secante(double angulo)throws Exception;
    public double cosecante(double angulo)throws Exception;
}