package Server;

import RMI.RITrigonometrics;
import java.rmi.server.UnicastRemoteObject;

public class ITrigonometrics extends UnicastRemoteObject implements RITrigonometrics{
    public ITrigonometrics()throws Exception{
        super();
    }
    
    @Override
    public double seno(double angulo){
        double anguloRadianes = Math.toRadians(angulo);
        return Math.sin(anguloRadianes);
    }
    
    @Override
    public double coseno(double angulo){
        double anguloRadianes = Math.toRadians(angulo);
        return Math.cos(anguloRadianes);
    } 
    
    @Override
    public double tangente(double angulo){
        double anguloRadianes = Math.toRadians(angulo);
        return Math.tan(anguloRadianes);
    }
    
    @Override
    public double cotangente(double angulo){
        double anguloRadianes = Math.toRadians(angulo);
        return (1/(Math.tan(anguloRadianes)));
    }
    
    @Override
    public double secante(double angulo){
        double anguloRadianes = Math.toRadians(angulo);
        return (1/(Math.cos(anguloRadianes)));
    }
    
    @Override
    public double cosecante(double angulo){
        double anguloRadianes = Math.toRadians(angulo);
        return (1/(Math.sin(anguloRadianes)));
    }
}