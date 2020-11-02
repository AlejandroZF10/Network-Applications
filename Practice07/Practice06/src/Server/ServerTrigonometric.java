package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerTrigonometric{
    public static void main(String[] args){
        try{
            Registry registry = LocateRegistry.createRegistry(8010);
            registry.rebind("CalculadoraT",new ITrigonometrics());
            System.out.println("Server RMI Trigonometrics started");
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
