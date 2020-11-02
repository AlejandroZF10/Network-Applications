package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerArithmetics {
    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.createRegistry(8008);
            registry.rebind("CalculadoraA",new IArithmetics());
            System.out.println("Server RMI Arithmetics started");
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}