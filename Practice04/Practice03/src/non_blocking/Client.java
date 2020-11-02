package non_blocking;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.nio.*;
import java.nio.charset.*;
import java.nio.channels.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.border.*;

public final class Client extends javax.swing.JFrame implements Runnable{
    private final int port = 10;
    private Selector selector;
    private SocketAddress remote;
    private DatagramChannel channel;
    private int positionY = 0;
    private final String username;
    private String auxiliarText = "";
    private final String host = "230.0.0.1";
    private final JList contactos;
    private final DefaultListModel nombres;
   
    public Client() {
        initComponents();
        this.setLocationRelativeTo(null);
        Texto.setBackground(new Color(10,27,37));
        username = JOptionPane.showInputDialog(null,"Introduce tu nombre!");
        this.setTitle(username);
        Nombre.setText(username);
        Miembros.addItem(username);
        Archivo.setEnabled(false);
        EmoticonPanel.setVisible(false);
        Texto.setBackground(new Color(10,27,37));
        joinMe("Union@"+username+"@Todos@"+username+"");
        nombres = new DefaultListModel();
        nombres.addElement("Todos");
        contactos = new JList(nombres);
    }
    
    public void joinMe(String message){
        try{
            remote = new InetSocketAddress(host, port);
            
            NetworkInterface ni = NetworkInterface.getByIndex(1);
            System.out.println("You choosed "+ni.getDisplayName());
            
            channel = DatagramChannel.open(StandardProtocolFamily.INET);
            channel.configureBlocking(false);
            channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            channel.setOption(StandardSocketOptions.IP_MULTICAST_TTL, 128);
            
            InetAddress group = InetAddress.getByName("230.0.0.1");
            channel.join(group, ni);
            selector = Selector.open();
            channel.register(selector,SelectionKey.OP_WRITE);
            
            ByteBuffer buffer_write = ByteBuffer.allocateDirect(1024);
            selector.select(5000);
            Iterator<SelectionKey>it = selector.selectedKeys().iterator();
            
            while(it.hasNext()){
                SelectionKey key = (SelectionKey)it.next();
                it.remove();
                if(key.isWritable()){
                    message = "Union@"+username+"@Todos@Nuevo Miembro@";
                    byte[] bytes = message.getBytes();
                    buffer_write.clear();
                    buffer_write.put(bytes);
                    buffer_write.flip();
                    channel.send(buffer_write, remote);
                }
            }
        }catch(IOException e){
            System.err.println("Sintaxis: java UDPEchoClient host [port]");
        }
    }
    
    public void addName(String name){
        Miembros.addItem(name);
        nombres.addElement(name);
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
    
    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        System.out.println("Display name: "+netint.getDisplayName());
        System.out.println("Name: "+netint.getName());
        String multicast = (netint.supportsMulticast())?"Soporta multicast":"No soporta multicast";
        System.out.println("Multicast: "+multicast);
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        Collections.list(inetAddresses).forEach((inetAddress) -> {
            System.out.println("InetAddress: "+inetAddress);
        });
        System.out.println();
     }
    
    @Override
    public void run() {
        while(true){
            try {
                channel.register(selector,SelectionKey.OP_READ);
                ByteBuffer buffer_read = ByteBuffer.allocateDirect(1024);
                selector.select(5000);
                Iterator<SelectionKey>it2 = selector.selectedKeys().iterator();
                while(it2.hasNext()){
                    SelectionKey key = (SelectionKey)it2.next();
                    it2.remove();
                    if(key.isReadable()){
                        buffer_read.clear();
                        SocketAddress client = channel.receive(buffer_read);
                        buffer_read.flip();
                        String mensaje = StandardCharsets.UTF_8.decode(buffer_read).toString();
                        System.out.println(client.toString());

                        String accion = mensaje.split("---")[0].split("@")[0];                        
                        switch(accion){
                            case "Union":
                                String usuarios = mensaje.split("---")[1];
                                String contenido = mensaje.split("---")[0].split("@")[3];
                                nuevoMiembro(contenido);
                                nombres.removeAllElements();
                                Miembros.removeAllItems();
                                String[] array_users = usuarios.split("@");
                                for (String array_user : array_users)
                                    addName(array_user);
                                contactos.setSelectedIndex(0);
                            break;
                            case "Send":
                                String origen_send = mensaje.split("---")[0].split("@")[1];
                                String destino_send = mensaje.split("---")[0].split("@")[2];
                                String contenido_send = mensaje.split("---")[0].split("@")[3];
                                if(((destino_send.equals("Todos"))||(destino_send.equals(username)))&&(!origen_send.equals(username))){
                                    int cantidad = 0;
                                    boolean flag = false;
                                    for(int i = 1; i<8 ;i = i + 1){
                                        if(contenido_send.contains(":e"+i+":")){
                                            flag = true;
                                            cantidad = cantidad + 1;
                                        }
                                    }
                                    if(flag)
                                        mostrarMensaje(origen_send,contenido_send,true,cantidad,false);
                                    else
                                        mostrarMensaje(origen_send,contenido_send,false,cantidad,false);
                                }
                            break;
                            case "Close":
                                String usuarios_close = mensaje.split("---")[1];
                                String contenido_close = mensaje.split("---")[0].split("@")[3];
                                nuevoMiembro(contenido_close);
                                nombres.removeAllElements();
                                Miembros.removeAllItems();
                                String[] array_users_close = usuarios_close.split("@");
                                for (String array_user_close : array_users_close)
                                    addName(array_user_close);
                                contactos.setSelectedIndex(0);
                            break;
                        }
                    }
                }   
            } catch (ClosedChannelException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
        Texto = new javax.swing.JPanel();
        Miembros = new javax.swing.JComboBox<>();
        Emoticonos = new javax.swing.JButton();
        Enviar = new javax.swing.JButton();
        Archivo = new javax.swing.JButton();
        MessageTextField = new javax.swing.JTextField();
        Fondo = new javax.swing.JLabel();
        Nombre = new javax.swing.JLabel();
        Online = new javax.swing.JLabel();
        Usuario = new javax.swing.JLabel();

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
            .addGap(0, 400, Short.MAX_VALUE)
        );
        TextoLayout.setVerticalGroup(
            TextoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        getContentPane().add(Texto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 400, 500));

        Miembros.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Miembros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos" }));
        getContentPane().add(Miembros, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 140, 25));

        Emoticonos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Emoticon.jpg"))); // NOI18N
        Emoticonos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmoticonosActionPerformed(evt);
            }
        });
        getContentPane().add(Emoticonos, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 565, 35, 30));

        Enviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Enviar.jpg"))); // NOI18N
        Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnviarActionPerformed(evt);
            }
        });
        getContentPane().add(Enviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 565, 35, 30));

        Archivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Archivo.png"))); // NOI18N
        Archivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArchivoActionPerformed(evt);
            }
        });
        getContentPane().add(Archivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 565, 35, 30));

        MessageTextField.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        getContentPane().add(MessageTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 565, 260, 30));

        Fondo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Fondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Modo Oscuro.jpg"))); // NOI18N
        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 560, 400, 40));

        Nombre.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        Nombre.setForeground(new java.awt.Color(255, 255, 255));
        Nombre.setText("Alejandro Zepeda");
        Nombre.setToolTipText("");
        getContentPane().add(Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 40));

        Online.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        Online.setForeground(new java.awt.Color(255, 255, 255));
        Online.setText("Online");
        Online.setToolTipText("");
        getContentPane().add(Online, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 35, 50, 20));

        Usuario.setBackground(new java.awt.Color(0, 0, 0));
        Usuario.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Usuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Modo Oscuro.jpg"))); // NOI18N
        getContentPane().add(Usuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 60));

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
            String cadena = "Send@"+username+"@"+destino+"@"+texto+"@";

            MessageTextField.setText("");
            try {
                selector = Selector.open();
                channel.register(selector,SelectionKey.OP_WRITE);
                ByteBuffer buffer_write = ByteBuffer.allocateDirect(1024);
                selector.select(5000);
                Iterator<SelectionKey>it = selector.selectedKeys().iterator();
                while(it.hasNext()){
                    SelectionKey key = (SelectionKey)it.next();
                    it.remove();
                    System.out.println(cadena);
                    if(key.isWritable()){
                        byte[] bytes = cadena.getBytes();
                        buffer_write.clear();
                        buffer_write.put(bytes);
                        buffer_write.flip();
                        channel.send(buffer_write, remote);
                    }
                }
            } catch (ClosedChannelException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            int cantidad = 0;
            boolean flag = false;
            for(int i = 1; i<8 ;i = i + 1){
                if(texto.contains(":e"+i+":")){
                    flag = true;
                    cantidad = cantidad + 1;
                }
            }
            if(flag)
                mostrarMensaje(username,texto,true,cantidad,true);
            else
                mostrarMensaje(username,texto,false,cantidad,true);
        }
    }//GEN-LAST:event_EnviarActionPerformed

    private void ArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArchivoActionPerformed

    }//GEN-LAST:event_ArchivoActionPerformed

    private void AngryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AngryActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e7: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_AngryActionPerformed

    private void SadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SadActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e6: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_SadActionPerformed

    private void WowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WowActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e5: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_WowActionPerformed

    private void HappyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HappyActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e4: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_HappyActionPerformed

    private void ImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ImportActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e3: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_ImportActionPerformed

    private void LoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoveActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e2: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_LoveActionPerformed

    private void LikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LikeActionPerformed
        auxiliarText = MessageTextField.getText();
        auxiliarText = auxiliarText + " :e1: ";
        MessageTextField.setText("");
        MessageTextField.setText(auxiliarText);
        EmoticonPanel.setVisible(false);
    }//GEN-LAST:event_LikeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            String cadena = "Close@"+username+"@Todos@"+username+" ha abandonado el grupo@";
            selector = Selector.open();
            channel.register(selector,SelectionKey.OP_WRITE);
            
            ByteBuffer buffer_write = ByteBuffer.allocateDirect(1024);
            selector.select(5000);
            Iterator<SelectionKey>it = selector.selectedKeys().iterator();
            
            while(it.hasNext()){
                SelectionKey key = (SelectionKey)it.next();
                it.remove();
                if(key.isWritable()){
                    byte[] bytes = cadena.getBytes();
                    buffer_write.clear();
                    buffer_write.put(bytes);
                    buffer_write.flip();
                    channel.send(buffer_write, remote);
                }
            }
            channel.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            Client client = new Client();
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