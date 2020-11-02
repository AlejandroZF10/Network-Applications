import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import javax.swing.table.*;


public class Client extends JFrame{
    Timer time;
    Socket cliente = null;
    int[] posiciones;
    InputStreamReader in = null;
    OutputStreamWriter out = null;
    JTextField[] letras;
    String[] eachletter;
    JLabel time_label,hanged_label;
    String username = "", palabra = "";
    JButton finish_button, estadistics_button, exit_button;
    int aciertos = 0, errores = 0, num_letras = 0, size = 0;
    int minutos = 0, segundos = 0, milisegundos = 0, usuarios = 0;
    public static ArrayList<Estadisticas> estadisticas = new ArrayList<Estadisticas>();

    public Client(){
        connectPython();
        initComponets();
        this.setSize(450,500);
        this.setTitle("Hangman Game");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void connectPython(){
        try{
            cliente = new Socket("localhost",8000);
            in = new InputStreamReader(cliente.getInputStream(), "UTF8");
            out = new OutputStreamWriter(cliente.getOutputStream(), "UTF8");

            char[] buffer = new char[512];
            in.read(buffer);
            System.out.println(readFromPython(buffer));

            out.write("1"); out.flush();

            buffer = new char[512];
            in.read(buffer);
            size = Integer.parseInt(readFromPython(buffer).trim());

            out.write("Cantidad recibida"); out.flush();

            for(int i = 0; i<size ;i = i + 1){
                buffer = new char[512];
                in.read(buffer);
                String name = readFromPython(buffer).split("@")[0];
                out.write("Dato"+i+"0"); out.flush();

                buffer = new char[512];
                in.read(buffer);
                String times = readFromPython(buffer).split("@")[0];
                out.write("Dato"+i+"1"); out.flush();

                buffer = new char[512];
                in.read(buffer);
                String status = readFromPython(buffer).split("@")[0];
                out.write("Dato"+i+"2"); out.flush();

                estadisticas.add(new Estadisticas(name,times,status));
            }

            buffer = new char[512];
            in.read(buffer);
            username = readFromPython(buffer).split("@")[0];

            out.write("Nombre recibido"); out.flush();

            buffer = new char[512];
            in.read(buffer);
            palabra = readFromPython(buffer).split("@")[0];

            out.write("Palabra recibida"); out.flush();

        }catch(IOException exception){
            System.err.println(exception);
        }
    }

    private String readFromPython(char[] buffer){
        String mensaje = "";
        for (char c : buffer) {
            mensaje += c;
            if(c == 00)
                break;
        }
        return mensaje.toString();
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
          }
          String tiempo = (minutos<=9?"0":"")+minutos+":"+(segundos<=9?"0":"")+segundos;
          time_label.setText(tiempo);
        }
    };

    private void initComponets(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.getContentPane().add(panel);
        setLabel(panel);
        setButton(panel);
        setWordSpace(panel);
        time = new Timer(10,acciones);
        time.start();
    }

    private void setLabel(JPanel panel){
        JLabel username_label = new JLabel(username);
        username_label.setBounds(10,10,300,30);
        username_label.setFont(new Font("Arial",Font.BOLD,18));
        username_label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(username_label);

        time_label = new JLabel("00:00");
        time_label.setBounds(320,10,100,30);
        time_label.setFont(new Font("Arial",Font.BOLD,18));
        time_label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(time_label);

        ImageIcon imagen = new ImageIcon(getClass().getResource("/Image/1.png"));
        hanged_label = new JLabel();
        hanged_label.setBounds(10,100,410,300);
        hanged_label.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(410,300,Image.SCALE_SMOOTH)));
        panel.add(hanged_label);
    }

    private void setButton(JPanel panel){
        exit_button = new JButton("Salir");
        finish_button = new JButton("Terminar");
        estadistics_button = new JButton("Estadisticas");

        finish_button.setBounds(10,410,125,35);
        finish_button.setFont(new Font("Arial",Font.BOLD,18));
        ActionListener event_finish = new ActionListener(){
          public void actionPerformed(ActionEvent event){
              finish_button.setEnabled(false);
              exit_button.setEnabled(true);
              time.stop();
              String tiempo = ""+(minutos<=9?"0":"")+minutos+":"+(segundos<=9?"0":"")+segundos;
              estadisticas.add(new Estadisticas(username,tiempo,"Derrota"));
          }
        };
        finish_button.addActionListener(event_finish);
        panel.add(finish_button);

        estadistics_button.setBounds(145,410,140,35);
        estadistics_button.setFont(new Font("Arial",Font.BOLD,18));
        ActionListener event_stadistics = new ActionListener(){
            public void actionPerformed(ActionEvent event){
              if(estadisticas.size() == 1)
                  JOptionPane.showMessageDialog(null,"No hay registros");
              else{
                Object[] cols = {"Nombre","Tiempo","Status"};
                Object[][] rows =  new Object[estadisticas.size()-1][3];
                for(int i = 0; i<rows.length ;i=i+1){
                    rows[i][0] = estadisticas.get(i+1).getNombre();
                    rows[i][1] = estadisticas.get(i+1).getTiempo();
                    rows[i][2] = estadisticas.get(i+1).getStatus();
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int x = screenSize.width/2-250, y = screenSize.height/2-250;
                JTable table = new JTable(rows,cols);
                table.setRowHeight(25);
                TableColumnModel columnModel = table.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(150);
                columnModel.getColumn(1).setPreferredWidth(75);
                columnModel.getColumn(2).setPreferredWidth(75);
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
                tcr.setHorizontalAlignment(SwingConstants.CENTER);
                table.getColumnModel().getColumn(0).setCellRenderer(tcr);
                table.getColumnModel().getColumn(1).setCellRenderer(tcr);
                table.getColumnModel().getColumn(2).setCellRenderer(tcr);
                JFrame frame = new JFrame();
                frame.add(new JScrollPane(table));
                frame.setTitle("Estadisticas de Usuario");
                frame.setLocation(x,y);
                frame.setSize(new Dimension(500,(estadisticas.size()*60)+10));
                frame.setVisible(true);
              }
            }
        };
        estadistics_button.addActionListener(event_stadistics);
        panel.add(estadistics_button);

        exit_button.setBounds(295,410,125,35);
        exit_button.setFont(new Font("Arial",Font.BOLD,18));
        ActionListener event_exit = new ActionListener(){
            public void actionPerformed(ActionEvent event){
              try{
                  out.write("3"); out.flush();
                  cliente.close();
                  System.out.println("SALIR");
              }catch(IOException exception){
                System.err.println(exception);
              }
            }
        };
        exit_button.addActionListener(event_exit);
        exit_button.setEnabled(false);
        panel.add(exit_button);
    }

    private void setWordSpace(JPanel panel){
        int x = 10, y = 60;
        size = palabra.length();
        posiciones = new int[size];
        eachletter = new String[size];
        for(int i = 0; i<size ;i = i + 1){
            if(palabra.charAt(i) == ' '){ //Espacio en blanco
                posiciones[i] = 0;
                eachletter[i] = "_";
            }else{
                posiciones[i] = 1;
                eachletter[i] = ""+palabra.charAt(i);
                num_letras = num_letras + 1;
            }
        }
        x = 220 - ((size * 30)/2);
        letras = new JTextField[size];
        for(int i = 0; i<posiciones.length ;i = i + 1){
            if(posiciones[i] != 0){
                  letras[i] = new JTextField("");
                  letras[i].setName(""+i);
                  letras[i].setBounds(x,y,30,30);
                  letras[i].setFont(new Font("Arial",1,20));
                  letras[i].setHorizontalAlignment(SwingConstants.CENTER);
                  letras[i].setActionCommand(""+i);
                  letras[i].addActionListener(new ActionListener() {
                      public void actionPerformed(ActionEvent event) {
                          String index = event.getActionCommand();
                          Object objeto = event.getSource();
                          JTextField jtf = null;
                          String letra = "";
                          if(objeto instanceof JTextField)
                              jtf  = (JTextField)objeto;
                          if(jtf != null)
                              letra = jtf.getText();
                          pressClick(Integer.parseInt(index),letra);
                      }
                  });
                  panel.add(letras[i]);
            }
            x = x + 30;
        }
    }

    private void pressClick(int index, String letra){
        char[] buffer = new char[512];
        if(letra.equals(eachletter[index])){
            letras[index].setEnabled(false);
            aciertos = aciertos + 1;
            if(aciertos == num_letras){
                time.stop();
                JOptionPane.showMessageDialog(null,"ENDGAME, YOU WIN");
                finish_button.setEnabled(false);
                exit_button.setEnabled(true);
                String tiempo = ""+(minutos<=9?"0":"")+minutos+":"+(segundos<=9?"0":"")+segundos;
                estadisticas.add(new Estadisticas(username,tiempo,"Victoria"));
                try{
                    out.write("2"); out.flush();
                    out.write(username); out.flush();
                    buffer = new char[512];
                    in.read(buffer);

                    out.write(tiempo); out.flush();
                    buffer = new char[512];
                    in.read(buffer);

                    out.write("Victoria"); out.flush();
                    buffer = new char[512];
                    in.read(buffer);
              }catch(IOException exception){
                  System.err.println(exception);
              }
            }
        }else{
            ImageIcon imagen = null;
            switch(errores){
                case 0:
                  imagen = new ImageIcon(getClass().getResource("/Image/2.png"));
                break;
                case 1:
                  imagen = new ImageIcon(getClass().getResource("/Image/3.png"));
                break;
                case 2:
                  imagen = new ImageIcon(getClass().getResource("/Image/4.png"));
                break;
                case 3:
                  imagen = new ImageIcon(getClass().getResource("/Image/5.png"));
                break;
                case 4:
                  imagen = new ImageIcon(getClass().getResource("/Image/6.png"));
                break;
                case 5:
                  imagen = new ImageIcon(getClass().getResource("/Image/7.png"));
                break;
            }
            hanged_label.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(410,300,Image.SCALE_SMOOTH)));
            if(errores<5){
                letras[index].setText("");
                errores = errores + 1;
            }else{
                time.stop();
                JOptionPane.showMessageDialog(null,"ENDGAME, YOU LOSE");
                finish_button.setEnabled(false);
                exit_button.setEnabled(true);
                String tiempo = ""+(minutos<=9?"0":"")+minutos+":"+(segundos<=9?"0":"")+segundos;
                estadisticas.add(new Estadisticas(username,tiempo,"Derrota"));
                try{
                    out.write("2"); out.flush();
                    out.write(username); out.flush();
                    buffer = new char[512];
                    in.read(buffer);

                    out.write(tiempo); out.flush();
                    buffer = new char[512];
                    in.read(buffer);

                    out.write("Derrota"); out.flush();
                    buffer = new char[512];
                    in.read(buffer);
              }catch(IOException exception){
                  System.err.println(exception);
              }
            }
        }
    }

    public static void main(String[] args){
        Client player = new Client();
        player.setVisible(true);
    }
}
