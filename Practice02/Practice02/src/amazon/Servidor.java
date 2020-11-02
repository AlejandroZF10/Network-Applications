package amazon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import ticket.GenerateTicket;

public class Servidor { 
    public static int port = 5678;
    public static DataInputStream input = null;
    public static DataOutputStream output = null;
    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<Product> carrito = new ArrayList<Product>();
    public static final String USER = "root";
    public static final String URL = "jdbc:mysql://localhost:3306/amazon";
    public static String inventario = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Practice02\\Inventario.txt";
    
        
    private static void readFile(String path) throws IOException{
        String cadena = "";
        FileReader file = new FileReader(path);
        BufferedReader buffer = new BufferedReader(file);
        while ((cadena = buffer.readLine())!=null){
            String[] aux = cadena.split(",");
            int id = Integer.parseInt(aux[0]);
            String name = aux[1];
            String description = aux[2];
            float price = Float.parseFloat(aux[3]);
            int stock = Integer.parseInt(aux[4]);
            int offer = Integer.parseInt(aux[5]);
            products.add(new Product(id,name,description,price,stock,offer));
        }
    }
    
    private static void updateStock(){
        for(int i = 0; i<carrito.size() ;i++){
            int idCarrito = carrito.get(i).getID();
            for(int j = 0; j<products.size() ;j++){
                int idProducto = products.get(j).getID();
                if(idCarrito == idProducto)
                   products.get(j).setStock(carrito.get(i).getStock());
            }
        }
    }
    
    public static Connection getConnection() throws SQLException{
        Connection connect = null;
        try{            
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(URL,USER,"");
        }catch(ClassNotFoundException | SQLException e){
            System.err.println("Error: "+e);
        }
        return connect;
    }
    
    public static void insert(){
        int total = 0, superTotal = 0;
        PreparedStatement ps = null;
        Connection conexion = null;
        try{
            conexion = getConnection();
            ps = conexion.prepareStatement("INSERT INTO productos (ID,Nombre,Descripcion,Precio,Cantidad,Descuento,Total,SuperTotal) VALUES (?,?,?,?,?,?,?,?)");
            for(int i = 0; i<carrito.size() ;i++){
                ps.setInt(1,carrito.get(i).getID());
                ps.setString(2,carrito.get(i).getName());
                ps.setString(3,carrito.get(i).getDescription());
                ps.setInt(4, (int) carrito.get(i).getPrice());
                ps.setInt(5,carrito.get(i).getStock());
                ps.setInt(6,carrito.get(i).getOffer());
                total =  (int) (carrito.get(i).getStock()*carrito.get(i).getPrice());
                superTotal = superTotal + total;
                ps.setInt(7,total);
                ps.setInt(8,superTotal);
                ps.executeUpdate();
            }
            conexion.close();
        }catch(SQLException e){
            System.err.println(e);
        }
    }
    
    public static void delete(){
        PreparedStatement ps = null;
        Connection conexion = null;
        try{
            conexion = getConnection();
            ps = conexion.prepareStatement("DELETE FROM productos");
            ps.executeUpdate();
            conexion.close();
        }catch(SQLException e){
            System.err.println(e);
        }
    }

    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(port);
        System.out.println("Conexión establecida, esperando clientes...\n");
        readFile(inventario);
        while(true){
            Socket client = server.accept();
            System.out.println("Llegó un cliente desde "+client.getInetAddress()+":"+client.getPort()+" esperando...\n");
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            while(true){
                try{
                    int action = input.readInt();
                    switch(action){
                        case 0:
                            Utils.sendObject(products,output);
                            System.out.println("Enviando lista de los productos disponibles...");
                        break;
                        case 1:
                            System.out.println("Validando la compra del cliente...");
                            insert();
                            updateStock();
                            GenerateTicket gt = new GenerateTicket();
                            gt.ticket(5);
                            delete();
                        break;
                        case 2:
                            input.close();
                            output.close();
                            System.out.println("Se desconectó el cliente "+client.getInetAddress()+":"+client.getPort()+"\n");
                            products.clear();
                            client.close();
                        break;
                    }
                    carrito = (ArrayList<Product>) Utils.receiveObject(input);
                    output.writeInt(1); output.flush();
                }catch (Exception e) {
                    System.out.println("Ocurrió un error con el cliente, desconectando...");
                    break;
                }
            }
        }
    }
}