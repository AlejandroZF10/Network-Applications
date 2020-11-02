package cliente;

import clases.*;
import componentes.ClienteMulticast;
import componentes.ServidorMulticast;
import java.util.Scanner;
import java.net.InetAddress;
import javax.swing.JOptionPane;

public class Cliente {
    public static void main(String[] args) throws Exception {
        int option;
        Scanner leer = new Scanner(System.in);
        int puerto = Integer.parseInt(JOptionPane.showInputDialog(null,"Introduce el puerto a utilizar"));
        String nomUsuario = JOptionPane.showInputDialog(null,"Introduce tu nombre");
        InetAddress inetAddress = InetAddress.getLocalHost();
        
        System.out.println("Conectandose a la red desde " + inetAddress.getHostAddress() + "...");
        ClienteMulticast clientM = new ClienteMulticast(inetAddress.getHostAddress(), puerto,nomUsuario);
        clientM.start();
        Thread.sleep(1000);
        System.out.println(nomUsuario+" esta conectado!");
        do{
            System.out.println("------");
            System.out.println("Operacion a realizar");
            System.out.println("1-. Solicitar un archivo");
            System.out.println("0-. Salir");
            System.out.println("------");
            System.out.print("Opcion: ");
            option = leer.nextInt();
            if(option == 1){
                InfoBusqueda infoBus = new InfoBusqueda(inetAddress.getHostAddress(), nomUsuario);
                ServidorMulticast sm = new ServidorMulticast((Info) infoBus);
                sm.start();
                Thread.sleep(1000);
                System.out.println("------");
                System.out.println("Imprimiendo lista...");
                clientM.imprimirListaBusqueda();
                System.out.println("------");
                System.out.print("Introduce el ID del archivo: ");
                clientM.enviarBusqueda(leer.nextInt());
            }
        }while(option != 0);       
        clientM.interrupt();
    }
}