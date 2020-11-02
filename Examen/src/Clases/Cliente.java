package Clases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class Cliente extends javax.swing.JFrame {
    private final Timer time;
    public Socket client = null;
    public DataInputStream input = null;
    public DataOutputStream output = null;
    public int horas = 0 , minutos = 0 , segundos = 0 , milisegundos = 0 , indicePalabra = 0 , cantidad = 0;
    public int x = 10, y = 10 , contador = 0 , xInicio = 0 , yInicio = 0 , xFin = 0 , yFin = 0 , noPalabras = 0;
    public boolean gano , ayuda , inicioPalabra , modalidad;
    public int[] direccion;
    public int[][] posiciones, marcadas;
    public String estadisticaModalidad, estadisticaDificultad;
    public String[] palabraString , palabraAnagrama;
    public String[][] tablero , tableroJuego;
    public JLabel[] words;
    public JLabel[][] letra;
    public boolean[] sentido;
    public static ArrayList<String> palabras = new ArrayList<String>();
    public static ArrayList<Palabra> ListOfWords = new ArrayList<Palabra>();
    public static ArrayList<Estadistica> estadisticas = new ArrayList<Estadistica>();

    public Cliente() {
        initComponents();
        this.setLocationRelativeTo(null);
        time = new Timer(10,acciones);
        cargarComponentes();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        wordList = new javax.swing.JPanel();
        gamePanel = new javax.swing.JPanel();
        Tiempos = new javax.swing.JButton();
        Play = new javax.swing.JButton();
        Terminar = new javax.swing.JButton();
        Connect = new javax.swing.JButton();
        Pausar = new javax.swing.JButton();
        Puerto = new javax.swing.JTextField();
        Dificultad = new javax.swing.JComboBox<>();
        Modalidad = new javax.swing.JComboBox<>();
        Tiempo = new javax.swing.JLabel();
        Concepto = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sopa de letras");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        wordList.setForeground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout wordListLayout = new javax.swing.GroupLayout(wordList);
        wordList.setLayout(wordListLayout);
        wordListLayout.setHorizontalGroup(
            wordListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        wordListLayout.setVerticalGroup(
            wordListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        getContentPane().add(wordList, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 150, 220, 450));

        gamePanel.setRequestFocusEnabled(false);

        javax.swing.GroupLayout gamePanelLayout = new javax.swing.GroupLayout(gamePanel);
        gamePanel.setLayout(gamePanelLayout);
        gamePanelLayout.setHorizontalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        gamePanelLayout.setVerticalGroup(
            gamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        getContentPane().add(gamePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 450, 450));

        Tiempos.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Tiempos.setText("Tiempos");
        Tiempos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TiemposActionPerformed(evt);
            }
        });
        getContentPane().add(Tiempos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 50, 110, -1));

        Play.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Play.setText("Iniciar");
        Play.setActionCommand("Play!");
        Play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayActionPerformed(evt);
            }
        });
        getContentPane().add(Play, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 110, 35));

        Terminar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Terminar.setText("Terminar");
        Terminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TerminarActionPerformed(evt);
            }
        });
        getContentPane().add(Terminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 110, 35));

        Connect.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Connect.setText("Conectar");
        Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectActionPerformed(evt);
            }
        });
        getContentPane().add(Connect, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 110, 35));

        Pausar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Pausar.setText("Pausar");
        Pausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PausarActionPerformed(evt);
            }
        });
        getContentPane().add(Pausar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 110, 35));

        Puerto.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Puerto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Puerto.setText("Puerto");
        getContentPane().add(Puerto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 110, 35));

        Dificultad.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Dificultad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dificultad", "Principiante", "Intermedio", "Avanzado" }));
        getContentPane().add(Dificultad, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, 130, 35));

        Modalidad.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        Modalidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Modalidad", "Concepto", "Anagrama" }));
        Modalidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModalidadActionPerformed(evt);
            }
        });
        getContentPane().add(Modalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, 130, 35));

        Tiempo.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        Tiempo.setForeground(new java.awt.Color(255, 255, 255));
        Tiempo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Tiempo.setText("Time 00:00");
        getContentPane().add(Tiempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 230, -1));

        Concepto.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        Concepto.setForeground(new java.awt.Color(255, 255, 255));
        Concepto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Concepto.setText("Concepto");
        getContentPane().add(Concepto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 680, 35));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Fondo.jpg"))); // NOI18N
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cargarComponentes(){
        Play.setEnabled(false);
        Modalidad.setEnabled(false);
        Dificultad.setEnabled(false);
        Play.setEnabled(false);
        Pausar.setEnabled(false);
        Terminar.setEnabled(false);
        Tiempos.setEnabled(false);
        Concepto.setVisible(false);
        gamePanel.setVisible(false);
        wordList.setVisible(false);
    }
    
    private void actualizarTime(){
        String tiempo = "Time "+(minutos<=9?"0":"")+minutos+":"+(segundos<=9?"0":"")+segundos;
        Tiempo.setText(tiempo);
    }
    
    private final ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            milisegundos++;
            if(milisegundos == 100){
                milisegundos = 0;
                segundos++;
            }
            if(segundos == 60){
                segundos = 0;
                minutos++;
            }
            if(minutos == 60){
                minutos = 0;
                horas++;
            }
            actualizarTime();
            mostrarAyuda();
        }
    };
    
    private void mostrarAyuda(){
        int index = 0;
        if(ayuda){
            if(((segundos==30)||(segundos==59))&&(milisegundos==00)){
                for(int i = 0; i<ListOfWords.size() ;i=i+1){
                    if(!ListOfWords.get(i).getStatus()){
                        index = i;
                        break;
                    }
                }
                String direccion = "";
                int x = ListOfWords.get(index).getxInicial();
                int y = ListOfWords.get(index).getyInicial();
                int option = obtenerDireccion(index);
                if(option<=2)
                    direccion = "Horizontal";
                else if(option>2 && option<=4)
                    direccion = "Vertical";
                else if(option>4)
                    direccion = "Diagonal";
                JOptionPane.showMessageDialog(null,"X:"+x+" - Y:"+y+" Direccion: "+direccion);
            }
        } 
    }
    
    private void showWordsAnagrama(int dificultad){
        x = 10; y = 5;
        switch(dificultad){
            case 0: //Fácil
                for(int i = 0; i<palabraString.length ;i = i + 1){
                    String palabra = desordenar(ListOfWords.get(i).getPalabra());
                    palabraAnagrama[i] = palabra;
                    words[i] = new JLabel(); 
                    words[i].setBounds(x,y,200,20);
                    words[i].setText(palabra);
                    words[i].setFont(new Font("Arial", Font.BOLD, 15));
                    words[i].setOpaque(true);
                    words[i].setBackground(Color.LIGHT_GRAY);
                    words[i].setHorizontalAlignment(SwingConstants.CENTER);
                    wordList.add(words[i]);
                    y = y + 25;
                }
            break;
            case 1: // Difícil
                for(int i = 0; i<noPalabras ;i = i + 1){
                    words[i] = new JLabel(); 
                    words[i].setBounds(x,y,200,20);
                    words[i].setText(""+ListOfWords.get(i).getPalabra().length());
                    words[i].setFont(new Font("Arial", Font.BOLD, 15));
                    words[i].setOpaque(true);
                    words[i].setBackground(Color.LIGHT_GRAY);
                    words[i].setHorizontalAlignment(SwingConstants.CENTER);
                    wordList.add(words[i]);
                    y = y + 25;
                }
            break;
        }
        wordList.updateUI();
        wordList.setVisible(true);
    }
    
    private String desordenar(String cadena){
        String resultado = "";
        int[] posiciones = new int[cadena.length()];
        posiciones = generarAleatorios(0,cadena.length()-1,cadena.length());
        for(int i = 0; i<cadena.length() ;i=i+1)
            resultado = resultado + cadena.charAt(posiciones[i]);
        return resultado;
    }

    private void crearTabla(){
        x = 0; y = 0;
        for(int i = 0; i<15 ;i=i+1){
            for(int j = 0; j<15 ;j=j+1){
                letra[i][j] = new JLabel("",SwingConstants.CENTER);
                letra[i][j].setName("");
                letra[i][j].setBounds(x,y,30,30);
                letra[i][j].setBackground(Color.WHITE);
                letra[i][j].setFont(new Font("Arial", Font.BOLD, 12));
                letra[i][j].setForeground(Color.BLACK);
                letra[i][j].setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                letra[i][j].setOpaque(true);
                letra[i][j].setBorder(new javax.swing.border.LineBorder(Color.BLACK,1));
                letra[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent event){
                        pressClick(event);
                    }
                });
                gamePanel.add(letra[i][j]);
                if(x == 420){
                    x = 0;
                    y = y + 30;
                }
                else
                    x = x + 30;
            }
        }
    }
    
    private void pressClick(MouseEvent event){
        if(event.getComponent().getBackground().equals(Color.WHITE)){
            event.getComponent().setBackground(Color.GREEN);
            int positionY = event.getComponent().getX()/30;
            int positionX = event.getComponent().getY()/30;
            if(inicioPalabra){ //Ya encontro la posicion inicial
                yFin = event.getComponent().getX()/30;
                xFin = event.getComponent().getY()/30;
                if(contador == ListOfWords.get(indicePalabra).getPalabra().length()-1){ //Termino de marcar la palabra
                    marcadas[contador][0] = xFin;
                    marcadas[contador][1] = yFin;
                    tableroJuego[xFin][yFin] = "1";
                    if((ListOfWords.get(indicePalabra).getxFinal()==xFin)&&(ListOfWords.get(indicePalabra).getyFinal()==yFin)){ //Marco la posicion final
                        if(validarPalabra(indicePalabra)){ //La palabra está completa
                            if(modalidad){
                                words[indicePalabra].setText(ListOfWords.get(indicePalabra).getPalabra());
                                words[indicePalabra].setBackground(Color.GREEN);
                            }
                            ListOfWords.get(indicePalabra).setStatus(true);
                            cantidad = cantidad + 1;
                            inicioPalabra = false;
                            indicePalabra = contador = 0;
                            xInicio = yInicio = 0;
                            if(cantidad == noPalabras){ //Completo todas las palabras
                                gano = true;
                                Terminar.doClick();
                            }
                        }else{ //Faltan espacios por llenar
                            limpiarTablero();
                            inicioPalabra = false;
                            indicePalabra = contador = 0;
                            xInicio = yInicio = 0;
                        }
                    }else{ //No marco la posicion inicial
                        limpiarTablero();
                        inicioPalabra = false;
                        indicePalabra = contador = 0;
                        xInicio = yInicio = 0;
                    }
                }
                else{
                    marcadas[contador][0] = xFin;
                    marcadas[contador][1] = yFin;
                    tableroJuego[xFin][yFin] = "1";
                    contador = contador + 1;
                }
            }else{ //No encontro la posicion inicial
                for(int i = 0; i<ListOfWords.size() ;i=i+1){
                    if((ListOfWords.get(i).getxInicial() == positionX)&&(ListOfWords.get(i).getyInicial() == positionY)){ //Marcó la posicion inicial
                        xInicio = positionX;
                        yInicio = positionY;
                        marcadas = new int[ListOfWords.get(i).getPalabra().length()][2];
                        marcadas[contador][0] = xInicio;
                        marcadas[contador][1] = yInicio;
                        tableroJuego[xInicio][yInicio] = "1";
                        inicioPalabra = true;
                        indicePalabra = i;
                        contador = contador + 1;
                        break;
                    }
                }
            }
        }else if(event.getComponent().getName().equals("")){
            event.getComponent().setBackground(Color.WHITE);
        }
    }
    
    private void iniciarTablero(){
        tableroJuego = new String[15][15];
        for(int i = 0; i<15 ;i=i+1)
            for(int j = 0; j<15 ;j=j+1)
                tableroJuego[i][j] = "0";
    }
    
    private void limpiarTablero(){
        for(int i = 0; i<marcadas.length ;i=i+1){
            int x1 = marcadas[i][0], y1 = marcadas[i][1];
            tableroJuego[x1][y1] = "0";
            letra[x1][y1].setBackground(Color.WHITE);
        }
    }
    
    private boolean validarPalabra(int index){
        boolean accept = false;
        int xDiagonalInicio = 0 , yDiagonalInicio = 0;
        switch(obtenerDireccion(index)){
            case 1: //Horizontal Normal
                for(int i = ListOfWords.get(index).getyInicial(); i<=ListOfWords.get(index).getyFinal() ;i=i+1){
                    if(!tableroJuego[ListOfWords.get(index).getxInicial()][i].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 2: //Horizontal Invertida
                for(int i = ListOfWords.get(index).getyInicial(); i>=ListOfWords.get(index).getyFinal() ;i=i-1){
                    if(!tableroJuego[ListOfWords.get(index).getxInicial()][i].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 3: //Vertical Normal
                for(int i = ListOfWords.get(index).getxInicial(); i<=ListOfWords.get(index).getxFinal() ;i=i+1){
                    if(!tableroJuego[i][ListOfWords.get(index).getyInicial()].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 4: //Vertical Invertida
                for(int i = ListOfWords.get(index).getxInicial(); i>=ListOfWords.get(index).getxFinal() ;i=i-1){
                    if(!tableroJuego[i][ListOfWords.get(index).getyInicial()].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 5: //Diagonal Normal Arriba
                xDiagonalInicio  = ListOfWords.get(index).getxInicial();
                yDiagonalInicio = ListOfWords.get(index).getyInicial();
                for(int i = xDiagonalInicio; i>=ListOfWords.get(index).getxFinal() ;i=i-1,yDiagonalInicio++){
                    if(!tableroJuego[i][yDiagonalInicio].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 6: //Diagonal Normal Abajo
                xDiagonalInicio  = ListOfWords.get(index).getxInicial();
                yDiagonalInicio = ListOfWords.get(index).getyInicial();
                for(int i = xDiagonalInicio; i<ListOfWords.get(index).getxFinal() ;i=i+1,yDiagonalInicio++){
                    if(!tableroJuego[i][yDiagonalInicio].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 7: //Diagonal Invertida Arriba
                xDiagonalInicio  = ListOfWords.get(index).getxInicial();
                yDiagonalInicio = ListOfWords.get(index).getyInicial();
                for(int i = xDiagonalInicio; i>=ListOfWords.get(index).getxFinal() ;i=i-1,yDiagonalInicio--){
                    if(!tableroJuego[i][yDiagonalInicio].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
            case 8: //Diagonal Invertida Abajo
                xDiagonalInicio  = ListOfWords.get(index).getxInicial();
                yDiagonalInicio = ListOfWords.get(index).getyInicial();
                for(int i = xDiagonalInicio; i<ListOfWords.get(index).getxFinal() ;i=i+1,yDiagonalInicio--){
                    if(!tableroJuego[i][yDiagonalInicio].equals("1")){
                        accept = false;
                        break;
                    }
                    else
                        accept = true;
                }
            break;
        }
        return accept;
    }
    
    private int obtenerDireccion(int index){
        int option = 0;
        int inicioX = ListOfWords.get(index).getxInicial(), finalX = ListOfWords.get(index).getxFinal();
        int inicioY = ListOfWords.get(index).getyInicial(), finalY = ListOfWords.get(index).getyFinal();
        if(inicioX == finalX){ //Horizontal
            if(finalY>inicioY) //Normal
                option = 1;
            if(finalY<inicioY) //Invertido
                option = 2;
        }
        if(inicioY == finalY){ //Vertical
            if(finalX>inicioX) //Normal
                option = 3;
            if(finalX<inicioX) //Invertido
                option = 4;
        }
        if((inicioX!=finalX)&&(inicioY!=finalY)){ //Diagonal
            if((finalX<inicioX)&&(finalY>inicioY)) //Normal Arriba
                option = 5;
            if((finalX>inicioX)&&(finalY>inicioY)) //Normal Abajo
                option = 6;
            if((finalX<inicioX)&&(finalY<inicioY)) //Invertida Arriba
                option = 7;
            if((finalX>inicioX)&&(finalY<inicioY)) //Invertida Abajo
                option = 8;
        }
        return option;
    }
    
    private void asignarPosiciones(){
        int posicion = 0;
        int[] x,y = new int[noPalabras];
        x = generarAleatorios(0,14,15);
        y = generarAleatorios(0,14,15);
        for(int i = 0; i<posiciones.length ;i=i+1){
            posiciones[i][0] = x[posicion];
            posiciones[i][1] = y[posicion];
            posicion = posicion + 1;
        }
    }
    
    private void asignarDireccion(){
        for(int i = 0; i<palabraString.length ;i=i+1){
            int longitud = palabraString[i].length();
            if(longitud>=7)
                direccion[i] = 1 ; //Horizontal
            if(longitud>4 && longitud<7)
                direccion[i] = 2 ; //Horizontal
            if(longitud<=4)
                direccion[i] = 3 ; //Vertical
        }
    }
    
    private void asignarSentido(){
        int[] auxSentido = new int[noPalabras];
        auxSentido = generarAleatorios(0,14,10);
        for(int i = 0; i<auxSentido.length ;i=i+1)
            sentido[auxSentido[i]] = true; //Normal
    }
    
    private void colocarPalabraHorizontal(){
        int xInicialList = 0,yInicialList = 0,xFinalList = 0,yFinalList = 0;
        for(int i = 0; i<15 ;i++)
            for(int j = 0; j<15 ;j++)
                tablero[i][j] = "1";
        for(int i = 0; i<direccion.length ;i=i+1){
            if(direccion[i] == 1){ //Horizontal
                if(sentido[i]){ //Normal
                    int x = posiciones[i][0];
                    int y = posiciones[i][1];
                    int longitud = palabraString[i].length(), spaceDisp = 15 - y;
                    if(longitud>spaceDisp){
                        while(longitud>spaceDisp){
                            y = y - 1;
                            spaceDisp = 15 - y;
                        }
                    }
                    xInicialList = x;
                    yInicialList = y;
                    for(int j = 0; j<longitud ;j=j+1){
                        tablero[x][y] = palabraString[i].substring(j,j+1);
                        letra[x][y++].setText(palabraString[i].substring(j,j+1));
                    }
                    xFinalList = x;
                    yFinalList = yInicialList + palabraString[i].length() - 1;
                    ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                }
                if(!sentido[i]){ //Invertido
                    int x = posiciones[i][0];
                    int y = posiciones[i][1];
                    int longitud = palabraString[i].length();
                    if(longitud>y){
                        while(longitud>y){
                            y = y + 1;
                        }
                    } 
                    xInicialList = x;
                    yInicialList = y;
                    for(int j = 0; j<longitud ;j=j+1){
                        tablero[x][y] = palabraString[i].substring(j,j+1);
                        letra[x][y--].setText(palabraString[i].substring(j,j+1));
                    }
                    xFinalList = x;
                    yFinalList = yInicialList - palabraString[i].length() + 1;
                    ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                }                
            }
        }
    }
    
    private void colocarPalabraDiagonal(){
        int xInicialList = 0,yInicialList = 0,xFinalList = 0,yFinalList = 0;
        for(int i = 0; i<direccion.length ;i=i+1){
            if(direccion[i] == 3){ //Vertical
                if(sentido[i]){ //Normal
                    boolean pausa = false;
                    boolean flag = false;
                    int[] auxSentido = new int[1];
                    auxSentido = generarAleatorios(0,1,1);
                    while(!pausa){ //Encuentra nuevas coordenadas
                        int[] nuevasCoordenadas = new int[2];
                        nuevasCoordenadas = generarAleatorios(0,14,2);
                        int xInicial = nuevasCoordenadas[0];
                        int yInicial = nuevasCoordenadas[1];
                        if(tablero[xInicial][yInicial].equals("1")){ // Posicion inicial libre
                            int xFinal = xInicial, yFinal = yInicial;
                            int longitud = palabraString[i].length();
                            if(auxSentido[0] == 1){ //Abajo
                                while((xFinal<14)&&(yFinal<14)){
                                    xFinal = xFinal + 1;
                                    yFinal = yFinal + 1;
                                }
                                int spaceDisp = (int)Math.sqrt(((int)(Math.pow((xFinal-xInicial),2)))+((int)(Math.pow((yFinal-yInicial),2))));
                                if(spaceDisp>=longitud){ //Espacio suficiente
                                    int xAux = xInicial, yAux = yInicial;
                                    for(int j = 0; j<longitud ;j=j+1){
                                        if(tablero[xAux++][yAux++].equals("1"))
                                            flag = true;
                                        else{
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if(flag){
                                        xInicialList = xInicial;
                                        yInicialList = yInicial;
                                        for(int j = 0; j<longitud ;j=j+1){
                                            tablero[xInicial][yInicial] = palabraString[i].substring(j,j+1);
                                            letra[xInicial++][yInicial++].setText(palabraString[i].substring(j,j+1));
                                        }
                                        xFinalList = xInicialList + palabraString[i].length() - 1;
                                        yFinalList = yInicialList + palabraString[i].length() - 1;
                                        ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                                        pausa = true;
                                    }
                                    else
                                        pausa = false;
                                } //Fin if espacio suficiente
                                else
                                    pausa = false;
                            } //Fin if abajo
                            else{ // Arriba
                                while((xFinal>0)&&(yFinal<14)){
                                    xFinal = xFinal - 1;
                                    yFinal = yFinal + 1;
                                }
                                int spaceDisp = (int)Math.sqrt(((int)(Math.pow((xFinal-xInicial),2)))+((int)(Math.pow((yFinal-yInicial),2))));
                                if(spaceDisp>=longitud){ //Espacio suficiente
                                    int xAux = xInicial, yAux = yInicial;
                                    for(int j = 0; j<longitud ;j=j+1){
                                        if(tablero[xAux--][yAux++].equals("1"))
                                            flag = true;
                                        else{
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if(flag){
                                        xInicialList = xInicial;
                                        yInicialList = yInicial;
                                        for(int j = 0; j<longitud ;j=j+1){
                                            tablero[xInicial][yInicial] = palabraString[i].substring(j,j+1);
                                            letra[xInicial--][yInicial++].setText(palabraString[i].substring(j,j+1));
                                        }
                                        xFinalList = xInicialList - palabraString[i].length() + 1;
                                        yFinalList = yInicialList + palabraString[i].length() - 1;
                                        ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                                        pausa = true;
                                    }
                                    else
                                        pausa = false;
                                } //Fin if espacio suficiente
                                else
                                    pausa = false;
                            } //Fin else abajo
                        } //Fin if Posicion inicial libre
                        else
                            pausa = false;
                    } //Fin while
                } //Fin if normal
                if(!sentido[i]){ //Invertido
                    boolean pausa = false;
                    boolean flag = false;
                    int[] auxSentido = new int[1];
                    auxSentido = generarAleatorios(0,1,1);
                    while(!pausa){ //Encuentra nuevas coordenadas
                        int[] nuevasCoordenadas = new int[2];
                        nuevasCoordenadas = generarAleatorios(0,14,2);
                        int xInicial = nuevasCoordenadas[0];
                        int yInicial = nuevasCoordenadas[1];
                        if(tablero[xInicial][yInicial].equals("1")){ // Posicion inicial libre
                            int xFinal = xInicial, yFinal = yInicial;
                            int longitud = palabraString[i].length();
                            if(auxSentido[0] == 1){ //Abajo
                                while((xFinal<14)&&(yFinal>0)){
                                    xFinal = xFinal + 1;
                                    yFinal = yFinal - 1;
                                }
                                int spaceDisp = (int)Math.sqrt(((int)(Math.pow((xFinal-xInicial),2)))+((int)(Math.pow((yFinal-yInicial),2))));
                                if(spaceDisp>=longitud){ //Espacio suficiente
                                    int xAux = xInicial, yAux = yInicial;
                                    for(int j = 0; j<longitud ;j=j+1){
                                        if(tablero[xAux++][yAux--].equals("1"))
                                            flag = true;
                                        else{
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if(flag){
                                        xInicialList = xInicial;
                                        yInicialList = yInicial;
                                        for(int j = 0; j<longitud ;j=j+1){
                                            tablero[xInicial][yInicial] = palabraString[i].substring(j,j+1);
                                            letra[xInicial++][yInicial--].setText(palabraString[i].substring(j,j+1));
                                        }
                                        xFinalList = xInicialList + palabraString[i].length() - 1;
                                        yFinalList = yInicialList - palabraString[i].length() + 1;
                                        ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                                        pausa = true;
                                    }
                                    else
                                        pausa = false;
                                } //Fin if espacio suficiente
                                else
                                    pausa = false;
                            } //Fin if abajo
                            else{ // Arriba
                                while((xFinal>0)&&(yFinal>0)){
                                    xFinal = xFinal - 1;
                                    yFinal = yFinal - 1;
                                }
                                int spaceDisp = (int)Math.sqrt(((int)(Math.pow((xFinal-xInicial),2)))+((int)(Math.pow((yFinal-yInicial),2))));
                                if(spaceDisp>=longitud){ //Espacio suficiente
                                    int xAux = xInicial, yAux = yInicial;
                                    for(int j = 0; j<longitud ;j=j+1){
                                        if(tablero[xAux--][yAux--].equals("1"))
                                            flag = true;
                                        else{
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if(flag){
                                        xInicialList = xInicial;
                                        yInicialList = yInicial;
                                        for(int j = 0; j<longitud ;j=j+1){
                                            tablero[xInicial][yInicial] = palabraString[i].substring(j,j+1);
                                            letra[xInicial--][yInicial--].setText(palabraString[i].substring(j,j+1));
                                        }
                                        xFinalList = xInicialList - palabraString[i].length() + 1;
                                        yFinalList = yInicialList - palabraString[i].length() + 1;
                                        ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                                        pausa = true;
                                    }
                                    else
                                        pausa = false;
                                } //Fin if espacio suficiente
                                else
                                    pausa = false;
                            } //Fin else abajo
                        } //Fin if posicion inicial libre
                        else
                            pausa = false;
                    } //Fin while
                } //Fin if invertid
            }
        }
    }
    
    private void colocarPalabraVertical(){
        int xInicialList = 0,yInicialList = 0,xFinalList = 0,yFinalList = 0;
        for(int i = 0; i<direccion.length ;i=i+1){
            if(direccion[i] == 2){//Vertical
                if(sentido[i]){ //Normal
                    boolean pausa = false;
                    boolean flag = false;
                    while(!pausa){ //Encuentra nuevas coordenadas
                        int[] nuevasCoordenadas = new int[2];
                        nuevasCoordenadas = generarAleatorios(0,14,2);
                        int xInicial = nuevasCoordenadas[0];
                        int yInicial = nuevasCoordenadas[1];
                        if(tablero[xInicial][yInicial].equals("1")){ // Posicion inicial libre
                            int longitud = palabraString[i].length();
                            int spaceDisp = 15 - xInicial;
                            if(spaceDisp>=longitud){ //Espacio suficiente
                                int xAux = xInicial, yAux = yInicial;
                                for(int j = 0; j<longitud ;j=j+1){
                                    if(tablero[xAux++][yAux].equals("1"))
                                        flag = true;
                                    else{
                                        flag = false;
                                        break;
                                    }
                                }
                                if(flag){
                                    xInicialList = xInicial;
                                    yInicialList = yInicial;
                                    for(int j = 0; j<longitud ;j=j+1){
                                        tablero[xInicial][yInicial] = palabraString[i].substring(j,j+1);
                                        letra[xInicial++][yInicial].setText(palabraString[i].substring(j,j+1));
                                    }
                                    xFinalList = xInicialList + palabraString[i].length() - 1;
                                    yFinalList = yInicial;
                                    ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                                    pausa = true;
                                }
                                else
                                    pausa = false;
                            } //Fin if espacio suficiente
                            else
                                pausa = false;
                        } //Fin if Posicion inicial libre
                        else
                            pausa = false;
                    } //Fin while
                } //Fin if Normal
                if(!sentido[i]){
                    boolean pausa = false;
                    boolean flag = false;
                    while(!pausa){ //Encuentra nuevas coordenadas
                        int[] nuevasCoordenadas = new int[2];
                        nuevasCoordenadas = generarAleatorios(0,14,2);
                        int xInicial = nuevasCoordenadas[0];
                        int yInicial = nuevasCoordenadas[1];
                        if(tablero[xInicial][yInicial].equals("1")){ // Posicion inicial libre
                            int longitud = palabraString[i].length();
                            if(xInicial>=longitud){ //Espacio suficiente
                                int xAux = xInicial, yAux = yInicial;
                                for(int j = 0; j<longitud ;j=j+1){
                                    if(tablero[xAux--][yAux].equals("1"))
                                        flag = true;
                                    else{
                                        flag = false;
                                        break;
                                    }
                                }
                                if(flag){
                                    xInicialList = xInicial;
                                    yInicialList = yInicial;
                                    for(int j = 0; j<longitud ;j=j+1){
                                        tablero[xInicial][yInicial] = palabraString[i].substring(j,j+1);
                                        letra[xInicial--][yInicial].setText(palabraString[i].substring(j,j+1));
                                    }
                                    xFinalList = xInicialList - palabraString[i].length() + 1;
                                    yFinalList = yInicial;
                                    ListOfWords.add(new Palabra(palabraString[i],xInicialList,yInicialList,xFinalList,yFinalList,false));
                                    pausa = true;
                                }
                                else
                                    pausa = false;
                            } //Fin if espacio suficiente
                            else
                                pausa = false;
                        } //Fin if Posicion inicial libre
                        else
                            pausa = false;
                    } //Fin while
                }
            }
        }
    }
    
    private void llenarEspacios(){
        String[] abc = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        for(int i = 0; i<15 ;i=i+1){
            for(int j = 0; j<15 ;j=j+1){
                if(tablero[i][j].equals("1")){
                    int[] position = new int[1];
                    position = generarAleatorios(0,25,1);
                    tablero[i][j] = abc[position[0]];
                    letra[i][j].setText(abc[position[0]]);
                }
            }
        }
    }
    
    private int[] generarAleatorios(int minimo, int maximo, int longitud) {
        int numero;
        int[] numeros = new int[longitud];
        Random random = new Random();
        
        for (int i = 0; i < numeros.length; i++) {
            do{
                numero = random.nextInt(maximo - minimo + 1) + minimo;
            }while(validate(numeros,i,numero));
            numeros[i] = numero;
        }
        return numeros;
    }
    
    private boolean validate(int[] numeros, int indice, int valor) {
        for(int i = 0; i < indice; i++){
            if (numeros[i] == valor)
                return true;
        }
        return false;
    }
        
    private void iniciarJuego(){
        Modalidad.setEnabled(false); 
        Dificultad.setEnabled(false);
        Play.setEnabled(false); 
        Pausar.setEnabled(false); 
        Terminar.setEnabled(true);
        gamePanel.setVisible(true);
        Tiempos.setVisible(true);
        crearTabla();
        asignarPosiciones();
        asignarDireccion();
        asignarSentido();
        colocarPalabraHorizontal();
        colocarPalabraVertical();
        colocarPalabraDiagonal();
        llenarEspacios();
    }
    
    private void prepararJuego(){
        //Inicio Variables
            horas = minutos = segundos = milisegundos = indicePalabra = cantidad = 0;
            x = y = 10; contador = xInicio = yInicio = xFin = yFin = noPalabras = 0;
            estadisticaModalidad = estadisticaDificultad = "";
            gano = ayuda = inicioPalabra = modalidad = false;
        //Fin Variables
            
        //Inicio actualiza diseño
            Connect.setEnabled(false);
            Modalidad.setEnabled(true);
            Play.setEnabled(true);
            Tiempos.setEnabled(true);
        //Fin actualiza diseño            
          
        //Inicio Inicializar arreglos
            noPalabras = palabras.size()-1;
            palabraString = new String[noPalabras];
            for(int i = 0; i<noPalabras ;i=i+1)
                palabraString[i] = palabras.get((i+1));
            direccion = new int[noPalabras];
            posiciones = new int[noPalabras][2];
            sentido = new boolean[noPalabras];
            words = new JLabel[noPalabras];
            palabraAnagrama = new String[noPalabras];
            letra = new JLabel[15][15];
            tablero = new String[15][15];
            iniciarTablero();
        //Inicio Inicializar arreglos
    }
      
    private void ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectActionPerformed
        if(Puerto.getText().length() == 0)
            JOptionPane.showMessageDialog(null,"¡Faltan campos por llenar!");
        else{
            if(client == null){
                try {
                    client = new Socket("127.0.0.1",Integer.parseInt(Puerto.getText()));
                    input = new DataInputStream(client.getInputStream());
                    output = new DataOutputStream(client.getOutputStream());
                    output.writeInt(0); output.flush();
                    palabras = (ArrayList<String>)Utils.receiveObject(input);
                    output.writeInt(1); output.flush();
                    estadisticas = (ArrayList<Estadistica>)Utils.receiveObject(input);
                    prepararJuego();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,"¡No se puede conectar con el servidor!");
                } catch (NumberFormatException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_ConnectActionPerformed

    private void PlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayActionPerformed
        time.start();
        iniciarJuego();
        switch(Modalidad.getSelectedIndex()){
            case 0:
                JOptionPane.showMessageDialog(null,"Selecciona otra modalidad");
            break;
            case 1: //Concepto
                Concepto.setVisible(true);
                Concepto.setText(palabras.get(0));
                estadisticaModalidad = "Concepto";
                estadisticaDificultad = "Ninguna";
            break;
            case 2: //Anagrama
                modalidad = true;
                wordList.setVisible(true);
                estadisticaModalidad = "Anagrama";
                switch(Dificultad.getSelectedIndex()){
                    case 0:
                        JOptionPane.showMessageDialog(null,"Selecciona otra dificultad");
                    break;
                    case 1:
                        showWordsAnagrama(0);
                        estadisticaDificultad = "Facíl";
                    break;
                    case 2:
                        ayuda = true;
                        showWordsAnagrama(0);
                        estadisticaDificultad = "Medio";
                    break;
                    case 3:
                        showWordsAnagrama(1);
                        estadisticaDificultad = "Dificíl";
                    break;
                }
            break;
        }
    }//GEN-LAST:event_PlayActionPerformed

    private void ModalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModalidadActionPerformed
        if(Modalidad.getSelectedIndex() == 2)
            Dificultad.setEnabled(true);
        else
            Dificultad.setEnabled(false);
    }//GEN-LAST:event_ModalidadActionPerformed

    private void PausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PausarActionPerformed
        time.stop();
        Play.setEnabled(true);
        Pausar.setEnabled(false);
    }//GEN-LAST:event_PausarActionPerformed

    private void TerminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TerminarActionPerformed
        if(time.isRunning()){
            time.stop();
            Play.setEnabled(false);
        }
        String nombre = JOptionPane.showInputDialog(null,"Introduce tu nombre");
        try {
            output.writeInt(2); output.flush();
            if(gano)
                estadisticas.add(new Estadistica(nombre,minutos,segundos,"Victoria",estadisticaModalidad,estadisticaDificultad));
            else
                estadisticas.add(new Estadistica(nombre,minutos,segundos,"Derrota",estadisticaModalidad,estadisticaDificultad));
            Utils.sendObject(estadisticas,output);
        } catch (IOException | HeadlessException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TerminarActionPerformed

    private void TiemposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TiemposActionPerformed
        if(estadisticas.isEmpty()){
            JOptionPane.showMessageDialog(null,"No hay registros");
        }else{
            Object[] cols = {"Nombre","Tiempo","Resultado","Modalidad","Dificultad"};
            Object[][] rows =  new Object[estadisticas.size()][5];
            for(int i = 0; i<rows.length ;i=i+1){
                rows[i][0] = estadisticas.get(i).getNombre();
                int minuto = estadisticas.get(i).getMinutos();
                int segundo = estadisticas.get(i).getSegundos();
                String tiempo = ""+(minuto<=9?"0":"")+minuto+":"+(segundo<=9?"0":"")+segundo;
                rows[i][1] = tiempo;
                rows[i][2] = estadisticas.get(i).getStatus();
                rows[i][3] = estadisticas.get(i).getModalidad();
                rows[i][4] = estadisticas.get(i).getDificultad();
            }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = screenSize.width/2-275, y = screenSize.height/2;
            JTable table = new JTable(rows,cols);
            table.setRowHeight(25);
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(1).setPreferredWidth(75);
            columnModel.getColumn(2).setPreferredWidth(75);
            columnModel.getColumn(3).setPreferredWidth(75);
            columnModel.getColumn(4).setPreferredWidth(75);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(tcr);
            table.getColumnModel().getColumn(1).setCellRenderer(tcr);
            table.getColumnModel().getColumn(2).setCellRenderer(tcr);
            table.getColumnModel().getColumn(3).setCellRenderer(tcr);
            table.getColumnModel().getColumn(4).setCellRenderer(tcr);
            JFrame frame = new JFrame();
            frame.add(new JScrollPane(table));
            frame.setTitle("Estadistícas de Usuario");
            frame.setLocation(x,y);
            frame.setSize(new Dimension(550,(estadisticas.size()*60)+10));
            frame.setVisible(true);
        }      
    }//GEN-LAST:event_TiemposActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Cliente().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Concepto;
    private javax.swing.JButton Connect;
    private javax.swing.JComboBox<String> Dificultad;
    private javax.swing.JComboBox<String> Modalidad;
    private javax.swing.JButton Pausar;
    private javax.swing.JButton Play;
    private javax.swing.JTextField Puerto;
    private javax.swing.JButton Terminar;
    private javax.swing.JLabel Tiempo;
    private javax.swing.JButton Tiempos;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel wordList;
    // End of variables declaration//GEN-END:variables
}