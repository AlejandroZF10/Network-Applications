package Server;

import RMI.RIArithmetics;
import java.rmi.server.UnicastRemoteObject;

public class IArithmetics extends UnicastRemoteObject implements RIArithmetics{
    
    public IArithmetics()throws Exception{
        super();
    }
    
    @Override
    public double suma(double x,double y){
        return (x+y);
    }
    
    @Override
    public double resta(double x,double y){
        return (x-y);
    }
    
    @Override
    public double multiplica(double x,double y){
        return (x*y);
    }
    
    @Override
    public double division(double x,double y){
        return (x/y);
    }
}