package RMI;

import java.rmi.Remote;

public interface RIArithmetics extends Remote{
    public double suma(double x,double y) throws Exception;
    public double resta(double x,double y) throws Exception;
    public double multiplica(double x,double y) throws Exception;
    public double division(double x,double y) throws Exception;
}