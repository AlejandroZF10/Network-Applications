package Chat;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.*;


public class Cliente extends javax.swing.JFrame implements Runnable{
    private int positionY = 0;
    private String username,auxiliarText,cadena,host;
    private DatagramPacket packet;
    private MulticastSocket cliente;
    private InetAddress grupo;
    private JList contactos;
    private DefaultListModel nombres;
    
    public Cliente() {
        initComponents();
        this.setLocationRelativeTo(null);
        configuracion();
    }
    
    public final void configuracion(){
        try {
            username = JOptionPane.showInputDialog(null,"Introduce tu nombre!");
            this.setTitle(username);
            Nombre.setText(username);
            Miembros.addItem(username);
            EmoticonPanel.setVisible(false);
            Texto.setBackground(new Color(10,27,37));
            cadena = "union@"+username+"@Todos@"+username+"";
            joinMe(cadena);
            nombres = new DefaultListModel();
            nombres.addElement("Todos");
            contactos = new JList(nombres);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addName(String name){
        Miembros.addItem(name);
        nombres.addElement(name);
    }
    
    private void joinMe(String message) throws IOException {
        cliente = new MulticastSocket(7777);
        cliente.setReuseAddress(true);
        cliente.setTimeToLive(255);
        grupo = InetAddress.getByName("232.0.0.10");
        cliente.joinGroup(grupo);
        host = "localhost";
        byte[] buffer = message.getBytes();
        packet = new DatagramPacket(buffer,buffer.length,grupo,8000);
        try{
            cliente.send(packet);
        }catch(IOException ex){
            System.err.println(ex);
        }
    }
    
    public void nuevoMiembro(String mensaje){
        int width = mensaje.length()*8;
        int x = 195-(width/2); 
        positionY = positionY + 10;
        JLabel nuevoMiembro = new JLabel();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        nuevoMiembro.setBounds(x,positionY,width,20);
        nuevoMiembro.setBorder(blackline);
        nuevoMiembro.setText(mensaje);
        nuevoMiembro.setFont(new Font("Arial", Font.BOLD, 15));
        nuevoMiembro.setOpaque(true);
        nuevoMiembro.setForeground(Color.BLACK);
        nuevoMiembro.setBackground(Color.LIGHT_GRAY);
        nuevoMiembro.setHorizontalAlignment(SwingConstants.CENTER);
        if(Texto.getHeight()<positionY+25)
            Texto.setPreferredSize(new Dimension(350,Texto.getHeight()+35));
        positionY = positionY + 25;
        Texto.add(nuevoMiembro);
        Texto.updateUI();
    }
    
    public void mostrarMensaje(String usuario, String mensaje, boolean emoji, int cantidad, boolean mode){
        positionY = positionY + 10;
        String mostrar = "";
        int positionX = 0, width = 0, heigth = 0;
        JLabel nuevoMensaje = new JLabel();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        if(emoji){ //Tiene emoticones
            int longName = usuario.length()*8;
            int longMessage = (mensaje.length()*8)-(48*cantidad)+(cantidad*36);
            if(longMessage<=200){ //Mensaje de una línea
                if(longMessage<=longName)
                    width = longName+15;
                else
                    width = longMessage+15;
                if(width>200)
                    width = 200;
                heigth = 50;
                if(mensaje.contains(":e1:"))
                    mensaje = mensaje.replaceAll(":e1:"," <img src='"+getClass().getResource("/ImageSend/LikeSend.png")+"' width='20' heigth='20'> ");
                if(mensaje.contains(":e2:"))
                    mensaje = mensaje.replaceAll(":e2:"," <img src='"+getClass().getResource("/ImageSend/LoveSend.png")+"' width='20' heigth='20'> ");
                if(mensaje.contains(":e3:"))
                    mensaje = mensaje.replaceAll(":e3:"," <img src='"+getClass().getResource("/ImageSend/ImportSend.png")+"' width='20' heigth='20'> ");
                if(mensaje.contains(":e4:"))
                    mensaje = mensaje.replaceAll(":e4:"," <img src='"+getClass().getResource("/ImageSend/HappySend.png")+"' width='20' heigth='20'> ");
                if(mensaje.contains(":e5:"))
                    mensaje = mensaje.replaceAll(":e5:"," <img src='"+getClass().getResource("/ImageSend/WowSend.png")+"' width='20' heigth='20'> ");
                if(mensaje.contains(":e6:"))
                    mensaje = mensaje.replaceAll(":e6:"," <img src='"+getClass().getResource("/ImageSend/SadSend.png")+"' width='20' heigth='20'> ");
                if(mensaje.contains(":e7:"))
                    mensaje = mensaje.replaceAll(":e7:"," <img src='"+getClass().getResource("/ImageSend/AngrySend.png")+"' width='20' heigth='20'> ");
                mostrar = "<html><div style='text-align: left;'> "+usuario+"<br>"+mensaje+"</div></html>";
            }else{ //Mensaje de varias lineas
                String[] palabras = mensaje.split(" "); //Split para cada palabra del texto
                int[] sizePalabras = new int[palabras.length]; //Longitud de cada palabra del texto
                for(int i = 0; i<sizePalabras.length ;i = i + 1){
                    if((palabras[i].length() == 4)&&(palabras[i].contains(":e")))
                        sizePalabras[i] = 20;
                    else
                        sizePalabras[i] = palabras[i].length()*8;
                }
                int spaceDisp = 200, index = 0, nolineas = 2;
                for(int i = 0; i<sizePalabras.length ;i = i + 1){ //Numero de lineas para imprimir
                    if(spaceDisp>sizePalabras[i])
                        spaceDisp = spaceDisp - sizePalabras[i] - 8;    
                    else{
                        i = i - 1;
                        nolineas = nolineas + 1;
                        spaceDisp = 200;
                    }
                }
                String[] cadena = new String[nolineas]; //Inicialización del arreglo final
                for(int i = 0; i<cadena.length ;i = i + 1)
                    cadena[i] = "";
                spaceDisp = 200;
                cadena[index++] = usuario;
                for(int i = 0; i<sizePalabras.length ;i = i + 1){ //Numero de lineas para imprimir
                    if(spaceDisp>sizePalabras[i]){
                        spaceDisp = spaceDisp - sizePalabras[i] - 8;
                        cadena[index] = cadena[index]+palabras[i]+" ";
                    }else{
                        i = i - 1;
                        index = index + 1;
                        spaceDisp = 200;
                    }
                }
                width = 200;
                heigth = 25*nolineas;
                String cadenaFinal = "";
                for(int i = 0; i<cadena.length ;i = i + 1)
                    cadenaFinal = cadenaFinal + cadena[i]+"<br>";
                if(cadenaFinal.contains(":e1:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e1:"," <img src='"+getClass().getResource("/ImageSend/LikeSend.png")+"' width='20' heigth='20'> ");
                if(cadenaFinal.contains(":e2:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e2:"," <img src='"+getClass().getResource("/ImageSend/LoveSend.png")+"' width='20' heigth='20'> ");
                if(cadenaFinal.contains(":e3:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e3:"," <img src='"+getClass().getResource("/ImageSend/ImportSend.png")+"' width='20' heigth='20'> ");
                if(cadenaFinal.contains(":e4:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e4:"," <img src='"+getClass().getResource("/ImageSend/HappySend.png")+"' width='20' heigth='20'> ");
                if(cadenaFinal.contains(":e5:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e5:"," <img src='"+getClass().getResource("/ImageSend/SadSend.png")+"' width='20' heigth='20'> ");
                if(cadenaFinal.contains(":e6:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e6:"," <img src='"+getClass().getResource("/ImageSend/WowSend.png")+"' width='20' heigth='20'> ");
                if(cadenaFinal.contains(":e7:"))
                    cadenaFinal = cadenaFinal.replaceAll(":e7:"," <img src='"+getClass().getResource("/ImageSend/AngrySend.png")+"' width='20' heigth='20'> ");
                mostrar = "<html><div style='text-align: left;'>"+cadenaFinal+"</div></html>";
            }
        }else{ //No tiene emoticones
            int longName = usuario.length()*8;
            int longMessage = mensaje.length()*8;
            if(longMessage<200){ //Mensaje de una línea
                if(longMessage<=longName)
                    width = longName+15;
                else
                    width = longMessage+15;
                if(width>200)
                    width = 200;
                heigth = 40;
                mostrar = "<html><div style='text-align: left;'>"+usuario+"<br>"+mensaje+"</div></html>";
            }else{ //Mensaje de varias lineas
                String[] palabras = mensaje.split(" "); //Split para cada palabra del texto
                int[] sizePalabras = new int[palabras.length]; //Longitud de cada palabra del texto
                for(int i = 0; i<sizePalabras.length ;i = i + 1)
                    sizePalabras[i] = palabras[i].length();
                int spaceDisp = 25, index = 0, nolineas = 2;
                for(int i = 0; i<sizePalabras.length ;i = i + 1){ //Numero de lineas para imprimir
                    if(spaceDisp>sizePalabras[i])
                        spaceDisp = spaceDisp - sizePalabras[i] - 1;    
                    else{
                        i = i - 1;
                        nolineas = nolineas + 1;
                        spaceDisp = 25;
                    }
                }
                
                String[] cadena = new String[nolineas]; //Inicialización del arreglo final
                for(int i = 0; i<cadena.length ;i = i + 1)
                    cadena[i] = "";
                spaceDisp = 25;
                cadena[index++] = usuario;
                for(int i = 0; i<sizePalabras.length ;i = i + 1){ //Numero de lineas para imprimir
                    if(spaceDisp>sizePalabras[i]){
                        spaceDisp = spaceDisp - sizePalabras[i] - 1;
                        cadena[index] = cadena[index]+palabras[i]+" ";
                    }else{
                        i = i - 1;
                        index = index + 1;
                        spaceDisp = 25;
                    }
                }

                width = 200;
                heigth = 20*nolineas;
                String cadenaFinal = "";
                for(int i = 0; i<cadena.length ;i = i + 1)
                    cadenaFinal = cadenaFinal + cadena[i]+"<br>";
                mostrar = "<html><div style='text-align: left;'>"+cadenaFinal+"</div></html>";
            } 
        }
        Color color;
        if(mode){
            positionX = 380-width;
            color = new Color(2,69,60);
        }else{
            positionX = 10;
            color = new Color(17,40,46);
        }
        nuevoMensaje.setBackground(color);
        nuevoMensaje.setText(mostrar);
        nuevoMensaje.setForeground(Color.WHITE);
        nuevoMensaje.setBounds(positionX,positionY,width,heigth);
        nuevoMensaje.setBorder(blackline);
        nuevoMensaje.setFont(new Font("Arial", Font.BOLD, 15));
        nuevoMensaje.setOpaque(true);
        nuevoMensaje.setHorizontalAlignment(SwingConstants.LEFT);
        if(Texto.getHeight()<(positionY+heigth))
            Texto.setPreferredSize(new Dimension(350,positionY+heigth+10));
        positionY = positionY + heigth;
        Texto.add(nuevoMensaje);
        Texto.updateUI();
    }
    
    public void manejoCarpeta(String usuario, boolean mode){
        File carpeta = new File("C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Practice03\\"+usuario);
        if(mode) //Crear carpeta    
            carpeta.mkdir();
        else //Borrar carpeta
            carpeta.deleteOnExit();
    }
    
    @Override
    public void run(){
        while(true){
            packet = new DatagramPacket(new byte[4096],4096);
            try{
                manejoCarpeta(username,true);
                cliente.receive(packet);
                String recibir = new String(packet.getData());
                String[] partsAux = recibir.split("@");
                String remitente = partsAux[1];
                String destinatario = partsAux[2];
                System.out.println("Remitente: " + remitente);
                System.out.println("Destinatario: " + destinatario);
                System.out.println("Acción: " + partsAux[0]);
                if(partsAux[0].equalsIgnoreCase("union")){ //Agregar nuevo miembro
                    String[] parts = recibir.split("---");
                    String[] partMessage = parts[0].split("@");
                    String[] partName = parts[1].split("@");
                    if(partMessage[0].equalsIgnoreCase("union")){
                        nuevoMiembro(partsAux[1]+" se ha unido al grupo");
                        nombres.removeAllElements();
                        Miembros.removeAllItems();
                        for(int i=0; i<partName.length; i = i + 1){
                            byte[] array = partName[i].getBytes();
                            String cadena = new String(array,"UTF-8");
                            if(i == partName.length-1)
                                addName(partsAux[1]);
                            else
                                addName(cadena);
                        }
                        contactos.setSelectedIndex(0);
                    }
                }else if(partsAux[0].equalsIgnoreCase("mensaje")){ //Envío de mensaje
                    if(remitente.equalsIgnoreCase(username)){ //Envío un mensaje
                        String message = partsAux[3]+" ";
                        int cantidad = 0;
                        boolean flag = false;
                        for(int i = 1; i<8 ;i = i + 1){
                            if(message.contains(":e"+i+":")){
                                flag = true;
                                cantidad = cantidad + 1;
                            }
                        }
                        if(flag)
                            mostrarMensaje(username,message,true,cantidad,true);
                        else
                            mostrarMensaje(username,message,false,cantidad,true);
                    }else{ //Recibo un mensaje
                        String messageRecibe = new String(packet.getData());
                        String[] getMessage = messageRecibe.split("@");
                        String message = getMessage[3];
                        if(destinatario.equalsIgnoreCase(username)||destinatario.equalsIgnoreCase("Todos")){
                            int cantidad = 0;
                            boolean flag = false;
                            for(int i = 1; i<8 ;i = i + 1){
                                if(message.contains(":e"+i+":")){
                                    flag = true;
                                    cantidad = cantidad + 1;
                                }
                            }
                            if(flag)
                                mostrarMensaje(remitente,message,true,cantidad,false);
                            else
                                mostrarMensaje(remitente,message,false,cantidad,false);
                            
                        }
                    }  
                }else if(partsAux[0].equalsIgnoreCase("Archivo")){ //Descarga de archivo
                    String namefile = partsAux[1];
                    String origen = partsAux[2];
                    String destino = partsAux[3];
                    if(destino.equalsIgnoreCase("Todos")) //Archivo para todos
                        if(!destino.equals(username))
                            mostrarMensaje(origen,namefile+" recibido.",false,0,false);
                    else if((destino.equalsIgnoreCase(username))) //Archivo individual
                        mostrarMensaje(origen,namefile+" recibido.",false,0,false);
                }else if(partsAux[0].equalsIgnoreCase("Close")){ //Abandona un usuario
                    String[] parts = recibir.split("---");
                    String[] partMessage = parts[0].split("@");
                    String usuarioEliminado = partMessage[1];
                    if(!usuarioEliminado.equals(username)){
                        String[] partName = parts[1].split("@");
                        if(partMessage[0].equalsIgnoreCase("Close")){
                            nombres.removeAllElements();
                            Miembros.removeAllItems();
                            for(int i = 0; i<partName.length ;i = i + 1){
                                byte[] array = partName[i].getBytes();
                                String cadena = new String(array,"UTF-8");
                                if((!cadena.contains("****"))&&(i != partName.length-1))
                                    addName(cadena);
                            }
                            contactos.setSelectedIndex(0);
                            nuevoMiembro(usuarioEliminado+" abandonó el grupo");
                            manejoCarpeta(username,false);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        EmoticonPanel = new javax.swing.JPanel();
        Angry = new javax.swing.JButton();
        Sad = new javax.swing.JButton();
        Wow = new javax.swing.JButton();
        Happy = new javax.swing.JButton();
        Import = new javax.swing.JButton();
        Love = new javax.swing.JButton();
        Like = new javax.swing.JButton();
        FondoEmoticones = new javax.swing.JLabel();
        JScrollPane = new javax.swing.JScrollPane();
        Texto = new javax.swing.JPanel();
        Miembros = new javax.swing.JComboBox<>();
        MessageTextField = new javax.swing.JTextField();
        Archivo = new javax.swing.JButton();
        Enviar = new javax.swing.JButton();
        Emoticonos = new javax.swing.JButton();
        Online = new javax.swing.JLabel();
        Nombre = new javax.swing.JLabel();
        Usuario = new javax.swing.JLabel();
        Fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EmoticonPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Angry.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Angry.png"))); // NOI18N
        Angry.setToolTipText("");
        Angry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AngryActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Angry, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 5, 40, 40));

        Sad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Sad.png"))); // NOI18N
        Sad.setToolTipText("");
        Sad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SadActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Sad, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 5, 40, 40));

        Wow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Wow.png"))); // NOI18N
        Wow.setToolTipText("");
        Wow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WowActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Wow, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 5, 40, 40));

        Happy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Happy.png"))); // NOI18N
        Happy.setToolTipText("");
        Happy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HappyActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Happy, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 5, 40, 40));

        Import.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Import.png"))); // NOI18N
        Import.setToolTipText("");
        Import.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ImportActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Import, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 5, 40, 40));

        Love.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Love.png"))); // NOI18N
        Love.setToolTipText("");
        Love.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoveActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Love, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 5, 40, 40));

        Like.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Like.png"))); // NOI18N
        Like.setToolTipText("");
        Like.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LikeActionPerformed(evt);
            }
        });
        EmoticonPanel.add(Like, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 40, 40));

        FondoEmoticones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Modo Oscuro.jpg"))); // NOI18N
        EmoticonPanel.add(FondoEmoticones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 50));

        getContentPane().add(EmoticonPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 505, 390, 50));

        Texto.setPreferredSize(new java.awt.Dimension(350, 490));

        javax.swing.GroupLayout TextoLayout = new javax.swing.GroupLayout(Texto);
        Texto.setLayout(TextoLayout);
        TextoLayout.setHorizontalGroup(
            TextoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        TextoLayout.setVerticalGroup(
            TextoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );

        JScrollPane.setViewportView(Texto);

        getContentPane().add(JScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 400, 500));

        Miembros.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Miembros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        getContentPane().add(Miembros, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 140, 25));

        MessageTextField.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        getContentPane().add(MessageTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 565, 260, 30));

        Archivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Archivo.png"))); // NOI18N
        Archivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(Archivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 565, 35, 30));

        Enviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Enviar.jpg"))); // NOI18N
        Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnviarActionPerformed(evt);
            }
        });
        getContentPane().add(Enviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 565, 35, 30));

        Emoticonos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Emoticon.jpg"))); // NOI18N
        Emoticonos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmoticonosActionPerformed(evt);
            }
        });
        getContentPane().add(Emoticonos, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 565, 35, 30));

        Online.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Online.setForeground(new java.awt.Color(255, 255, 255));
        Online.setText("Online");
        Online.setToolTipText("");
        getContentPane().add(Online, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 50, 20));

        Nombre.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        Nombre.setForeground(new java.awt.Color(255, 255, 255));
        Nombre.setText("Alejandro Zepeda");
        Nombre.setToolTipText("");
        getContentPane().add(Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 40));

        Usuario.setBackground(new java.awt.Color(0, 0, 0));
        Usuario.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Modo Oscuro.jpg"))); // NOI18N
        getContentPane().add(Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 60));

        Fondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Modo Oscuro.jpg"))); // NOI18N
        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 400, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void EmoticonosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmoticonosActionPerformed
        EmoticonPanel.setVisible(true);
    }//GEN-LAST:event_EmoticonosActionPerformed

    private void EnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnviarActionPerformed
        String texto = MessageTextField.getText();
        if(texto.length() == 0)
            JOptionPane.showMessageDialog(null,"Mensaje vacío, escribe algo!");
        else{
            String destino = (String)Miembros.getSelectedItem();
            cadena = "mensaje@"+username+"@"+destino+"@"+texto+"@";
            byte[] buffer = cadena.getBytes();
            MessageTextField.setText("");
            packet = new DatagramPacket(buffer,buffer.length,grupo,8000);
            try{
                cliente.send(packet);
            }catch(IOException ex){
                System.err.println(ex);
            } 
        }
    }//GEN-LAST:event_EnviarActionPerformed

    private void ArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArchivoActionPerformed
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\alexz\\Desktop\\Archivos\\ESCOM\\8° Semestre\\Redes\\Prácticas\\Practice03\\"+username);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            try{
                File file = fileChooser.getSelectedFile();
                Socket clt = new Socket(host,8000);
                ObjectOutputStream oos = new ObjectOutputStream(clt.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clt.getInputStream());
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                String destino = (String)Miembros.getSelectedItem();
                oos.writeBoolean(true); oos.flush();
                oos.writeUTF(destino); oos.flush();
                oos.writeUTF(file.getName()); oos.flush();
                oos.writeLong(file.length()); oos.flush();
                int n = 0;
                byte[] buffer = new byte[1500];
                while((n = bis.read(buffer))>0){
                    oos.write(buffer,0,n);
                    oos.flush();
                }
                mostrarMensaje(username,file.getName()+" enviado.",false,0,true);
                fis.close(); bis.close();
                ois.close(); oos.close();
                clt.close();
                Thread.sleep(1000);
                n = contactos.getSelectedIndex();
                cadena = "Archivo@"+file.getName()+"@"+username+"@"+destino+"@";
                byte[] second_buffer = cadena.getBytes();
                packet = new DatagramPacket(second_buffer,second_buffer.length,grupo,8000);
                cliente.send(packet);
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }//GEN-LAST:event_ArchivoActionPerformed

    private void LikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LikeActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e1: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_LikeActionPerformed

    private void LoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoveActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e2: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_LoveActionPerformed

    private void ImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e3: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_ImportActionPerformed

    private void HappyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HappyActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e4: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_HappyActionPerformed

    private void WowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WowActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e5: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_WowActionPerformed

    private void SadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SadActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e6: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_SadActionPerformed

    private void AngryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AngryActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e7: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_AngryActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cadena = "Close@"+username+"@Todos";
        byte[] buffer = cadena.getBytes();
        packet = new DatagramPacket(buffer,buffer.length,grupo,8000);
        try{
            cliente.send(packet);
        }catch(IOException ex){
            System.err.println(ex);
        }
    }//GEN-LAST:event_formWindowClosing
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            Cliente client = new Cliente();
            client.setVisible(true);
            Thread t = new Thread(client);
            t.start();
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Angry;
    private javax.swing.JButton Archivo;
    private javax.swing.JPanel EmoticonPanel;
    private javax.swing.JButton Emoticonos;
    private javax.swing.JButton Enviar;
    private javax.swing.JLabel Fondo;
    private javax.swing.JLabel FondoEmoticones;
    private javax.swing.JButton Happy;
    private javax.swing.JButton Import;
    private javax.swing.JScrollPane JScrollPane;
    private javax.swing.JButton Like;
    private javax.swing.JButton Love;
    private javax.swing.JTextField MessageTextField;
    private javax.swing.JComboBox<String> Miembros;
    private javax.swing.JLabel Nombre;
    private javax.swing.JLabel Online;
    private javax.swing.JButton Sad;
    private javax.swing.JPanel Texto;
    private javax.swing.JLabel Usuario;
    private javax.swing.JButton Wow;
    // End of variables declaration//GEN-END:variables
}