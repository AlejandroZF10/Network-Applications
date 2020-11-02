import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends Thread{
    protected Mime mime;
    protected Socket client;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    
    public Handler(Socket client) throws Exception{
        this.client = client;
        this.mime = new Mime();
        this.dis = new DataInputStream(this.client.getInputStream());
        this.dos = new DataOutputStream(this.client.getOutputStream());
    }
    
    public void deleteSource(String args, String headers){
        try{
            File file = new File(args);
            if(file.exists()){
                if(file.delete()){
                    System.out.println("-----> Archivo "+args+" eliminados exitosamente");
                    String deleteOK = headers +
                        "<html><head><meta charset='UTF-8'><title>202 OK Recurso eliminado</title></head>" +
                        "<body><h1>202 OK Recurso eliminado exitosamente.</h1>" +
                        "<p>El recurso " + args + " ha sido eliminado permanentemente del servidor." + 
                        "Ya no se podra acceder más a él.</p></body></html>";
                    dos.write(deleteOK.getBytes()); dos.flush();
                    System.out.println("Respuesta DELETE: \n" + deleteOK);  
                }else{
                    System.out.println("El archivo " + args + " no pudo ser borrado\n");
                    String error404 = "HTTP/1.1 404 Not Found\n" +
                        "Date: " + new Date() + " \n" +
                        "Server: webServer/1.0 \n" +
                        "Content-Type: text/html \n\n" +
                        "<html><head><meta charset='UTF-8'><title>404 Not found</title></head>" +
                        "<body><h1>404 Not found</h1>" +
                        "<p>Archivo " + args + " no encontrado.</p>" +
                        "</body></html>";
                    dos.write(error404.getBytes()); dos.flush();
                    System.out.println("Respuesta DELETE - ERROR 404: \n" + error404);
                }
            }
        } catch (IOException ex) {}
    }
    
    public void sendSource(String args, int flag){
        try{
            File file = new File(args);
            String sb = "HTTP/1.1 200 OK\n";
            
            if(!file.exists()){
                args = "html/404.html"; //Recurso no encontrado
        	sb = "HTTP/1.1 404 Not Found\n";
            }else if(file.isDirectory()){
                args = "html/403.html"; //Recurso privado
        	sb = "HTTP/1.1 403 Forbidden\n";
            }
            
            DataInputStream dis_in = new DataInputStream(new FileInputStream(args));
            int size = dis_in.available();
            int position = args.indexOf(".");
            String extension = args.substring(position+1,args.length());
                sb = sb + "Date: " + new Date() + " \n" +
        		"Server: webServer/1.0 \n" +
        		"Content-Type: " + mime.get(extension) + " \n" + 
        		"Content-Length: " + size + " \n\n";
            dos.write(sb.getBytes()); dos.flush();
            
            String metodo = "HEAD";
            if(flag == 1){
                metodo = "GET";
                byte[] buffer = new byte[1024];
                long enviados = 0;
                int n = 0;
                while(enviados<size){
                    n = dis_in.read(buffer);
                    dos.write(buffer,0,n);
                    dos.flush();
                    enviados += n;
                }
            }
            System.out.println("Respuesta " + metodo + ": \n" + sb);
            dis_in.close(); 	
        } catch (FileNotFoundException ex) {} catch (IOException ex) {}
    }
    
    public String getNameSource(String line){
        int i = line.indexOf("/");
        int f = line.indexOf(" ",i);
        String resourceName = line.substring(i+1,f);
        if(resourceName.compareTo("")==0)
            resourceName = "html/index.html";
        return resourceName;
    }
    
    public String getParameters(String line, String headers, int flag){
        String metodo = "POST";
        String request2 = line;
        if(flag == 0){
            metodo = "GET";
            System.out.println(line);
            StringTokenizer tokens = new StringTokenizer(line, "?");
            String request = tokens.nextToken();
            request = tokens.nextToken();
            StringTokenizer tokens2 = new StringTokenizer(request, " ");
            request2 = tokens2.nextToken();
        }
        System.out.println(request2);
        StringTokenizer paramsTokens = new StringTokenizer(request2, "&");
        String html = headers +
            "<html><head><meta charset='UTF-8'><title>Metodo " + metodo + "\n" +
            "</title></head><body><center><h2>Parametros obtenidos por medio de " + metodo + "</h2><br>\n" +
            "<table border='2'><tr><th>Parametro</th><th>Valor</th></tr>";
    
        while(paramsTokens.hasMoreTokens()) {
        	String parametros = paramsTokens.nextToken();
        	// Separamos el nombre del parametro de su valor
        	StringTokenizer paramValue = new StringTokenizer(parametros, "=");
        	String param = ""; //Nombre del parametro
        	String value = ""; //Valor del parametro

        	// Hay que revisar si existen o si se enviaron parametros vacios
        	if(paramValue.hasMoreTokens())
                    param = paramValue.nextToken();

        	if(paramValue.hasMoreTokens())
                    value = paramValue.nextToken();

        	html = html + "<tr><td><b>" + param + "</b></td><td>" + value + "</td></tr>\n";
        }
        
        html = html + "</table></center></body></html>";
        return html;
    }
    
    @Override
    public void run() {
    	String headers = "HTTP/1.1 200 OK\n" +
            "Date: " + new Date() + " \n" +
            "Server: webServer/1.0 \n" +
            "Content-Type: text/html \n\n";
        try{
            String line = dis.readLine();
            if(line == null) {
                String vacia = "<html><head><title>Servidor WEB</title><body bgcolor='#AACCFF'>Linea Vacia</body></html>";
                dos.write(vacia.getBytes());
                dos.flush();
            }
            else{
                System.out.println("\n------> Cliente Conectado desde: " + client.getInetAddress());
                System.out.println("Por el puerto: " + client.getPort());
                System.out.println("Datos: " + line + "\r\n\r\n");

                if(line.toUpperCase().startsWith("GET")) { // Metodo GET
                    if(!line.contains("?")) {
                        String fileName = getNameSource(line);
                        // Bandera HEAD = 0, GET = 1
	                sendSource(fileName, 1);
	            }else{
                        // Bandera GET = 0, POST = 1
	                String respuesta = getParameters(line, headers, 0);
	                // Respuesta GET, devolvemos un HTML con los parametros del formulario
	                dos.write(respuesta.getBytes());
	                dos.flush();
	                System.out.println("Respuesta GET: \n" + respuesta);
	            }
                }else if(line.toUpperCase().startsWith("HEAD")) { // Metodo HEAD
                    if(!line.contains("?")) {
                        String fileName = getNameSource(line);
                        // Bandera HEAD = 0, GET = 1
                        sendSource(fileName, 0);
                    }else{
                        dos.write(headers.getBytes());
                        dos.flush();
                        System.out.println("Respuesta HEAD: \n" + headers);       
                    }
                } else if(line.toUpperCase().startsWith("POST")){ // Metodo POST
                    int tam = dis.available();
                    byte[] buffer = new byte[tam];
                    dis.read(buffer);
                    String request = new String(buffer,0,tam);
                    String[] reqLineas = request.split("\n");
                    int ult = reqLineas.length - 1;
                    // Bandera GET = 0, POST = 1
                    String respuesta = getParameters(reqLineas[ult], headers, 1);
                    // Respuesta POST, devolvemos un HTML con los parametros del formulario
                    dos.write(respuesta.getBytes());
                    dos.flush();
                    System.out.println("Respuesta POST: \n" + respuesta);
                } else if(line.toUpperCase().startsWith("DELETE")) { // Metodo DELETE
                    String fileName = getNameSource(line);
                    deleteSource(fileName, headers);
                } else {
                    //Metodos no implementados en el servidor
                    String error501 = "HTTP/1.1 501 Not Implemented\n" +
                        "Date: " + new Date() + " \n" +
        		        "Server: webServer/1.0 \n" +
        		        "Content-Type: text/html \n\n" +
                        "<html><head><meta charset='UTF-8'><title>Error 501</title></head>" +
        	        	"<body><h1>Error 501: No implementado.</h1>" +
        	        	"<p>El método HTTP o funcionalidad solicitada no está implementada en el servidor.</p>" +
        	        	"</body></html>";
                    dos.write(error501.getBytes());
                    dos.flush();
                    System.out.println("Respuesta ERROR 501: \n" + error501);
                }
            }
            dis.close();
            dos.close();
            client.close();
        } catch(IOException e){}
    }
}