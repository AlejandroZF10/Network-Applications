package googledrive;

import java.awt.Dimension;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Cliente extends javax.swing.JFrame {
    String[] direcciones;
    String host = "127.0.0.1";
    int x = 10, y = 10, pto = 5678, aux = 0;
    public static String serverpath = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Practice01\\Servidor\\";
    public static String clientpath = "C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Practice01\\Cliente\\";
    
    public Cliente() throws IOException {
        initComponents();
        setLocationRelativeTo(null);
        Update.setVisible(false);
        showFiles(serverpath,Servidor);
        showFiles(clientpath,Cliente);    
    }
    
    private void showFiles(String path,JPanel user){
        File carpeta = new File(path);
        File[] lista = carpeta.listFiles();
        int nofiles = 0, nodirectories = 0;
        int auxfiles = 0, auxdirectories = 0, tamPanel;
        int tamServer = lista.length;
        ImageIcon imagen1 = null; Icon fondo1;
        for(int i = 0; i<lista.length ;i = i + 1){
            if(lista[i].isDirectory())
                nodirectories++;
            else
                nofiles++;
        }
        if(tamServer<6)
            tamPanel = 250;
        else{
            if(tamServer%3 == 0)
                tamPanel = (tamServer/3) * 115;
            else
                tamPanel = ((tamServer/3)+1) * 115;      
        }
        user.setPreferredSize(new Dimension(200,tamPanel));
        JLabel[] label1 = new JLabel[nofiles];
        JLabel[] label2 = new JLabel[tamServer];
        JLabel[] label3 = new JLabel[nodirectories];
        for(int i = 0; i<tamServer ;i = i + 1){
            if(lista[i].isDirectory()){
                label3[auxdirectories] = new JLabel();
                label3[auxdirectories].setBounds(x,y,75,75);
                imagen1 = new ImageIcon(getClass().getResource("/images/carpeta.png"));
                fondo1 = new ImageIcon(imagen1.getImage().getScaledInstance(label3[auxdirectories].getWidth(),label3[auxdirectories].getHeight(),Image.SCALE_DEFAULT));
                label3[auxdirectories].setIcon(fondo1);
                user.add(label3[auxdirectories++]);  
            }else{
                switch(getFileExtension(lista[i].getName())){
                    case "docx":
                        imagen1 = new ImageIcon(getClass().getResource("/images/doc.png"));
                    break;
                    case "xlsx":
                        imagen1 = new ImageIcon(getClass().getResource("/images/xls.png"));
                    break;
                    case "pptx":
                        imagen1 = new ImageIcon(getClass().getResource("/images/ppt.png"));
                    break;
                    case "pdf":
                        imagen1 = new ImageIcon(getClass().getResource("/images/pdf.png"));
                    break;
                    case "jpg":
                        imagen1 = new ImageIcon(getClass().getResource("/images/jpg.png"));
                    break;
                    case "png":
                        imagen1 = new ImageIcon(getClass().getResource("/images/png.png"));
                    break;
                    case "mp3":
                        imagen1 = new ImageIcon(getClass().getResource("/images/mp3.png"));
                    break;
                    case "mp4":
                        imagen1 = new ImageIcon(getClass().getResource("/images/mp4.png"));
                    break;
                    case "txt":
                        imagen1 = new ImageIcon(getClass().getResource("/images/txt.png"));
                    break;
                    case "iso":
                        imagen1 = new ImageIcon(getClass().getResource("/images/iso.png"));
                    break;
                    default:
                        imagen1 = new ImageIcon(getClass().getResource("/images/file.png"));
                }
                label1[auxfiles] = new JLabel();
                label1[auxfiles].setBounds(x,y,75,80);
                fondo1 = new ImageIcon(imagen1.getImage().getScaledInstance(label1[auxfiles].getWidth(),label1[auxfiles].getHeight(),Image.SCALE_DEFAULT));
                label1[auxfiles].setIcon(fondo1);
                user.add(label1[auxfiles++]);
            }
            label2[i] = new JLabel();
            label2[i].setBounds(x,y+50,75,75);
            label2[i].setText(lista[i].getName());
            label2[i].setHorizontalAlignment(SwingConstants.CENTER);
            user.add(label2[i]);
            if(x == 210){
                x = 10;
                y = y + 115;
            }
            else
                x = x + 100;
        }
        x = 10; y = 10;
    }
    
    public String getFileExtension(String fileName){
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
    
    public void enviaModoOperacion(String modoOp,DataOutputStream dosNet)throws Exception{
        if(modoOp.equals("UPLOAD")||modoOp.equals("DOWNLOAD")||modoOp.equals("CREATE")||modoOp.equals("DELETE")||modoOp.equals("EXIT")){
            dosNet.writeUTF(modoOp);
            dosNet.flush();
	}else{
            System.out.println("MODO DE OPERACION INVALIDO");
            System.exit(1);
	}
    }
    
    public void createDirectory(String pathorigen,String mode) throws IOException{
        String newPath = null;
        File dOrigen = new File(pathorigen);
        File[] lista = dOrigen.listFiles();
        for (File lista1 : lista) {
            switch (mode) {
                case "DOWNLOAD":
                    newPath = lista1.getAbsolutePath().replaceAll("Servidor", "Cliente");
                    break;
                case "UPLOAD":
                    newPath = lista1.getAbsolutePath().replaceAll("Cliente", "Servidor");
                    break;
            }
            if (lista1.isDirectory()) {
                File file = new File(newPath);
                file.mkdirs();
                createDirectory(lista1.getAbsolutePath(), mode);
            }
        }           
    }
    
    public void sendFileFromDirectory(String pathorigen,String mode) throws IOException{
        String pathfinal = null;
        File dOrigen = new File(pathorigen); 
        File[] lista = dOrigen.listFiles();
        for (File lista1 : lista) {
            switch (mode) {
                case "DOWNLOAD":
                    pathfinal = lista1.getAbsolutePath().replaceAll("Servidor", "Cliente");
                    break;
                case "UPLOAD":
                    pathfinal = lista1.getAbsolutePath().replaceAll("Cliente", "Servidor");
                    break;
            }
            if (lista1.isFile()) {
                sendFile(lista1, pathfinal, mode);
            } else {
                sendFileFromDirectory(lista1.getAbsolutePath(), mode);
            }      
        }
    }
    
    public void sendFile(File file, String finalpath, String mode) throws IOException{
        Socket client = new Socket(host,pto);
        DataOutputStream dosNet = new DataOutputStream(client.getOutputStream());
        try {
            enviaModoOperacion(mode,dosNet);
            String nombre = file.getName();
            long tam = file.length();
            String path = file.getAbsolutePath();
            dosNet = new DataOutputStream(client.getOutputStream());
            try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
                long env = 0;
                int n = 0;
                dosNet.writeUTF(nombre);
                dosNet.flush();
                dosNet.writeLong(tam);
                dosNet.flush();
                dosNet.writeUTF(finalpath);
                dosNet.flush();                
                while(env<tam){
                    byte[]b = new byte[3000];
                    n = dis.read(b);
                    dosNet.write(b,0,n);
                    dosNet.flush();
                    env = env+n;
                }//while
            }
            dosNet.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScrollPaneClient = new javax.swing.JScrollPane();
        Cliente = new javax.swing.JPanel();
        ScrollPaneServer = new javax.swing.JScrollPane();
        Servidor = new javax.swing.JPanel();
        Extension = new javax.swing.JComboBox<>();
        Client = new javax.swing.JRadioButton();
        Server = new javax.swing.JRadioButton();
        Exit = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Create = new javax.swing.JButton();
        Upload = new javax.swing.JButton();
        Download = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Update = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Practice 1 - Google Drive");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Cliente.setPreferredSize(new java.awt.Dimension(300, 260));

        javax.swing.GroupLayout ClienteLayout = new javax.swing.GroupLayout(Cliente);
        Cliente.setLayout(ClienteLayout);
        ClienteLayout.setHorizontalGroup(
            ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        ClienteLayout.setVerticalGroup(
            ClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        ScrollPaneClient.setViewportView(Cliente);

        getContentPane().add(ScrollPaneClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, 320, 260));

        Servidor.setPreferredSize(new java.awt.Dimension(300, 260));

        javax.swing.GroupLayout ServidorLayout = new javax.swing.GroupLayout(Servidor);
        Servidor.setLayout(ServidorLayout);
        ServidorLayout.setHorizontalGroup(
            ServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        ServidorLayout.setVerticalGroup(
            ServidorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        ScrollPaneServer.setViewportView(Servidor);

        getContentPane().add(ScrollPaneServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 320, 260));

        Extension.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Extension.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Directory", "File" }));
        Extension.setPreferredSize(new java.awt.Dimension(90, 25));
        getContentPane().add(Extension, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 100, 30));

        Client.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Client.setForeground(new java.awt.Color(255, 255, 255));
        Client.setText("Client");
        Client.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Client.setOpaque(false);
        Client.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClientActionPerformed(evt);
            }
        });
        getContentPane().add(Client, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 320, -1));

        Server.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Server.setForeground(new java.awt.Color(255, 255, 255));
        Server.setText("Server");
        Server.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Server.setOpaque(false);
        Server.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServerActionPerformed(evt);
            }
        });
        getContentPane().add(Server, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 320, -1));

        Exit.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        getContentPane().add(Exit, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 370, 100, 30));

        Delete.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        getContentPane().add(Delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 370, 100, 30));

        Create.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Create.setText("Create");
        Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateActionPerformed(evt);
            }
        });
        getContentPane().add(Create, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, 100, 30));

        Upload.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Upload.setText("Upload");
        Upload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UploadActionPerformed(evt);
            }
        });
        getContentPane().add(Upload, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, 100, 30));

        Download.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Download.setText("Download");
        Download.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownloadActionPerformed(evt);
            }
        });
        getContentPane().add(Download, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 370, 100, 30));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Alejandro de Jesús Zepeda Flores");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 660, -1));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Practice 1 - Google Drive");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 660, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fondo.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 420));

        Update.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Update.setText("Update");
        Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateActionPerformed(evt);
            }
        });
        getContentPane().add(Update, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 100, 30));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void DownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownloadActionPerformed
        if(Extension.getSelectedIndex() == 0){ //Directory
            JFileChooser fileChooser = new JFileChooser(serverpath);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int r = fileChooser.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                try {
                    File file = fileChooser.getSelectedFile();
                    createDirectory(file.getAbsolutePath(),"DOWNLOAD");
                    sendFileFromDirectory(file.getAbsolutePath(),"DOWNLOAD");
                    Update.doClick();
                    JOptionPane.showMessageDialog(null,"Directory "+file.getName()+" sent successfully");
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }else{ //File
            JFileChooser fileChooser = new JFileChooser(serverpath);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int r = fileChooser.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                try {
                    File file = fileChooser.getSelectedFile();
                    String pathfinal = file.getAbsolutePath().replaceAll("Servidor","Cliente");
                    sendFile(file,pathfinal,"DOWNLOAD");
                    Update.doClick();
                    JOptionPane.showMessageDialog(null,"File "+file.getName()+" sent successfully");
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_DownloadActionPerformed

    private void UploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UploadActionPerformed
        if(Extension.getSelectedIndex() == 0){ //Directory
            JFileChooser fileChooser = new JFileChooser(clientpath);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int r = fileChooser.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                try {
                    File file = fileChooser.getSelectedFile();
                    createDirectory(file.getAbsolutePath(),"UPLOAD");
                    sendFileFromDirectory(file.getAbsolutePath(),"UPLOAD");
                    Update.doClick();
                    JOptionPane.showMessageDialog(null,"Directory "+file.getName()+" sent successfully");
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{ //File
            JFileChooser fileChooser = new JFileChooser(clientpath);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int r = fileChooser.showOpenDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                try {
                    File file = fileChooser.getSelectedFile();
                    String pathfinal = file.getAbsolutePath().replaceAll("Cliente","Servidor");
                    sendFile(file,pathfinal,"UPLOAD");
                    Update.doClick();
                    JOptionPane.showMessageDialog(null,"File "+file.getName()+" sent successfully");
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_UploadActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        Servidor.removeAll();
        showFiles(serverpath,Servidor);
        Servidor.updateUI();
        Cliente.removeAll();
        showFiles(clientpath,Cliente);
        Cliente.updateUI();
    }//GEN-LAST:event_UpdateActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        try {
            Socket client = new Socket(host,pto);
            DataOutputStream dosNet = new DataOutputStream(client.getOutputStream());
            enviaModoOperacion("EXIT",dosNet);
            System.exit(1);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ExitActionPerformed

    private void CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateActionPerformed
        File file;
        if(Server.isSelected()){ //Create Server
            if(Extension.getSelectedIndex() == 0){ //Directory
                String name = JOptionPane.showInputDialog("Introduce the name of Directory");
                file = new File(serverpath+name);
                if(file.exists())
                    JOptionPane.showMessageDialog(null, "Directory "+name+" is already exists");
                else
                    if (file.mkdir())
                        JOptionPane.showMessageDialog(null, "Directory "+name+" created");  
                    else 
                        JOptionPane.showMessageDialog(null, "Directory "+name+" cannot be created"); 
            }else{ //File
                String name = JOptionPane.showInputDialog("Introduce the name of File");
                file = new File(serverpath+name);
                try {
                    if(!file.exists())
                        if (file.createNewFile())
                           JOptionPane.showMessageDialog(null, "File "+name+" created"); 
                        else 
                           JOptionPane.showMessageDialog(null, "File "+name+" cannot be created"); 
                    else
                        JOptionPane.showMessageDialog(null, "File "+name+" is already exists"); 
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            if(Extension.getSelectedIndex() == 0){ //Directory
                String name = JOptionPane.showInputDialog("Introduce the name of Directory");
                file = new File(clientpath+name);
                if(file.exists())
                    JOptionPane.showMessageDialog(null, "Directory "+name+" is already exists");
                else
                    if (file.mkdir())
                        JOptionPane.showMessageDialog(null, "Directory "+name+" created"); 
                    else 
                        JOptionPane.showMessageDialog(null, "Directory "+name+" cannot be created"); 
            }else{ //File
                String name = JOptionPane.showInputDialog("Introduce the name of File");
                file = new File(clientpath+name);
                try {
                    if(!file.exists())
                        if (file.createNewFile())
                           JOptionPane.showMessageDialog(null, "File "+name+" created"); 
                        else 
                           JOptionPane.showMessageDialog(null, "File "+name+" cannot be created"); 
                    else
                        JOptionPane.showMessageDialog(null, "File "+name+" is already exists"); 
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Update.doClick();
    }//GEN-LAST:event_CreateActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        File file;
        if(Server.isSelected()){ //Create Server
            if(Extension.getSelectedIndex() == 0){ //Directory
                String name = JOptionPane.showInputDialog("Introduce the name of Directory");
                file = new File(serverpath+name);
                if(file.delete())
                    JOptionPane.showMessageDialog(null, "Directory "+name+" deleted");
            }else{ //File
                String name = JOptionPane.showInputDialog("Introduce the name of File");
                file = new File(serverpath+name);
                if(file.delete())
                    JOptionPane.showMessageDialog(null, "File "+name+" deleted");
            }
        }else{
            if(Extension.getSelectedIndex() == 0){ //Directory
                String name = JOptionPane.showInputDialog("Introduce the name of Directory");
                file = new File(clientpath+name);
                if(file.delete())
                    JOptionPane.showMessageDialog(null, "Directory "+name+" deleted");
                }else{ //File
                   String name = JOptionPane.showInputDialog("Introduce the name of File");
                   file = new File(clientpath+name);
                   if(file.delete())
                       JOptionPane.showMessageDialog(null, "File "+name+" deleted");
                }
        }
        Update.doClick();
    }//GEN-LAST:event_DeleteActionPerformed

    private void ServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServerActionPerformed
        Client.setSelected(false);
    }//GEN-LAST:event_ServerActionPerformed

    private void ClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClientActionPerformed
        Server.setSelected(false);
    }//GEN-LAST:event_ClientActionPerformed
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Cliente().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Client;
    private javax.swing.JPanel Cliente;
    private javax.swing.JButton Create;
    private javax.swing.JButton Delete;
    private javax.swing.JButton Download;
    private javax.swing.JButton Exit;
    private javax.swing.JComboBox<String> Extension;
    private javax.swing.JScrollPane ScrollPaneClient;
    private javax.swing.JScrollPane ScrollPaneServer;
    private javax.swing.JRadioButton Server;
    private javax.swing.JPanel Servidor;
    private javax.swing.JButton Update;
    private javax.swing.JButton Upload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}