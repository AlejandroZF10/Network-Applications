package googledrive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private final ServerSocket server;
    private Socket client;
    private DataInputStream disNet;
    private DataOutputStream dosNet;
    public static String serverpath = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Servidor\\";
    public static String clientpath = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Cliente\\";
    
    public Servidor(int puerto)throws IOException{
        server = new ServerSocket(puerto);
        server.setReuseAddress(true);
    }

    public void aceptarConexion()throws IOException{
        client = server.accept();
	disNet = new DataInputStream(client.getInputStream());
	dosNet = new DataOutputStream(client.getOutputStream());
    }
    
    public String recibeModoOperacion()throws IOException{
        String modoOp = disNet.readUTF();
	if(!modoOp.equals("UPLOAD")&&!modoOp.equals("DOWNLOAD")&&!modoOp.equals("EXIT")&&!modoOp.equals("LIST_FILES")){
            System.out.println("MODO DE OPERACION INVALIDO");
            return "";
	}
	return modoOp;
    }
    
    public void enviaArchivo() throws IOException{
        String nombre = disNet.readUTF();
        long tam = disNet.readLong();
        String finalpath = disNet.readUTF();
        System.out.println("Preparado para recibir archivo "+nombre+" desde "+client.getInetAddress()+":"+client.getPort());
        dosNet = new DataOutputStream(new FileOutputStream(finalpath));
        int n = 0, porcentaje = 0;
        long r = 0;
        while(r<tam){
            byte[] b = new byte[3000];
            n = disNet.read(b);
            dosNet.write(b,0,n);
            dosNet.flush();
            r = r + n;
            porcentaje = (int)((r*100)/tam);
        }
        dosNet.close();
        disNet.close();
        client.close();
    }
    
    public static void main(String[] args) throws IOException{
        String modoOp;
        try{
            Servidor servidor= new Servidor(5678);
	    System.out.println("Servidor iniciado");
            while(true){
                System.out.println("Esperando conexiones");
		servidor.aceptarConexion();
		System.out.println("Conexión recibida");
                modoOp = servidor.recibeModoOperacion();
                switch(modoOp){
                    case "DOWNLOAD":
                        System.out.println("Solicitud para descargar archivo recibida");
			servidor.enviaArchivo();
			System.out.println("Archivo enviado");
                    break;
                    case "UPLOAD":
                        System.out.println("Solicitud para subir archivo recibida");
			servidor.enviaArchivo();
			System.out.println("Archivo enviado");
                    break;
                    case "CREATE":
                        System.out.println("Solicitud para subir archivo recibida");
			servidor.enviaArchivo();
			System.out.println("Archivo enviado");
                    break;
                    case "DELETE":
                        System.out.println("Solicitud para subir archivo recibida");
			servidor.enviaArchivo();
			System.out.println("Archivo enviado");
                    break;
                    
                    case "EXIT":
                        servidor.server.close();
                        servidor.client.close();
                        System.exit(1);
                    break;
                }
            }
        }catch(IOException e){
            System.err.println("Unexpected error");
	}
    }
}