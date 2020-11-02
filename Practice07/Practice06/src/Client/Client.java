package Client;

import java.awt.*;
import java.rmi.*;
import java.rmi.registry.*;
import javax.swing.*;
import RMI.RIArithmetics;
import RMI.RITrigonometrics;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends javax.swing.JFrame {
    private int positionY = 5;
    private int panelX = 430, panelY = 245;
    
    public Client() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    private void addLabel(String operacion){
        JLabel operation = new JLabel();
        operation.setBounds(10,positionY,operacion.length()*9,25);
        operation.setText(operacion);
        operation.setAlignmentX(SwingConstants.LEADING);
        operation.setFont(new Font("Yu Gothic UI Semibold",12,18));
        if(positionY>=245){
            panelY = panelY + 30;
            resultPanel.setPreferredSize(new Dimension(panelX,panelY));
        }
        if(operation.getWidth() >= resultPanel.getWidth()){
            panelX = operation.getWidth()+10;
            resultPanel.setSize(new Dimension(panelX,panelY));
            resultPanel.setPreferredSize(new Dimension(panelX,panelY));
        }
        resultPanel.add(operation);
        resultPanel.updateUI();
        positionY = positionY + 30;   
    }
    
    private String postfija(String expresion){
        String post = "";
        expresion = depurar(expresion);
        String[] arrayInfix = expresion.split(" ");
        Stack<String>E = new Stack<>();
        Stack<String>P = new Stack<>();
        Stack<String>S = new Stack<>();

        for (int i = arrayInfix.length-1; i>=0 ;i--)
            E.push(arrayInfix[i]);

        try {
            while (!E.isEmpty()) {
                switch (preferencia(E.peek())){
                    case 1:
                        P.push(E.pop());
                    break;
                    case 3:
                    case 4:
                        while(preferencia(P.peek()) >= preferencia(E.peek()))
                            S.push(P.pop());
                        P.push(E.pop());
                    break; 
                    case 2:
                        while(!P.peek().equals("("))
                            S.push(P.pop());
                        P.pop();
                        E.pop();
                    break; 
                    default:
                        S.push(E.pop()); 
                } 
            }
        post = S.toString().replaceAll("[\\]\\[,]", "");
        }catch(Exception ex){ 
            System.out.println("Error en la expresión algebraica");
            System.err.println(ex);
        }
  
        return post;
    }
    
    private int preferencia(String operador) {
        int prf = 99;
        if (operador.equals("^")) prf = 5;
        if (operador.equals("*") || operador.equals("/")) prf = 4;
        if (operador.equals("+") || operador.equals("-")) prf = 3;
        if (operador.equals(")")) prf = 2;
        if (operador.equals("(")) prf = 1;
        return prf;
    }
    
    private String calculadora(String expresion){
        String[] post = expresion.split(" ");
        Stack<String>E = new Stack<>();
        Stack<String>P = new Stack<>();
        for(int i = post.length - 1; i >= 0; i--)
            E.push(post[i]);
        String operadores = "+-*/%";
        while(!E.isEmpty()) {
            if(operadores.contains(""+E.peek()))
                P.push(String.format("%.3f",evaluar(E.pop(),P.pop(),P.pop())));
            else 
                P.push(E.pop());
        }
        return P.peek();
    }
    
    private double evaluar(String operador, String valor2, String valor1) {
        try {
            Registry registryA = LocateRegistry.getRegistry("127.0.0.1",8008);
            RIArithmetics remoteA = (RIArithmetics)registryA.lookup("CalculadoraA");
            double numero1 = Double.parseDouble(valor1);
            double numero2 =  Double.parseDouble(valor2);
            if (operador.equals("+")) return remoteA.suma(numero1,numero2);
            if (operador.equals("-")) return remoteA.resta(numero1,numero2);
            if (operador.equals("*")) return remoteA.multiplica(numero1,numero2);
            if (operador.equals("/")) return remoteA.division(numero1,numero2);
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private String depurar(String cadena) {
        cadena = cadena.replaceAll("\\s+", "");
        cadena = "(" + cadena + ")";
        String simbols = "+-*/()";
        String str = "";
        for (int i = 0; i < cadena.length(); i++) {
            if (simbols.contains("" + cadena.charAt(i)))
                str += " " + cadena.charAt(i) + " ";
            else 
                str += cadena.charAt(i);
        }
        return str.replaceAll("\\s+", " ").trim();
    }
    
    private boolean evaluateKeys(String expresion){
        Stack<String> stack = new Stack<>();
        for(int i = 0; i<expresion.length() ;i++){
            switch(expresion.charAt(i)){
                case'(': //Llave de apertura
                    stack.push(expresion.charAt(i)+"");
                break;
                case')': //Llave de clausura
                    if(stack.empty()){
                        JOptionPane.showMessageDialog(this,"Falta llave de apertura");
                        return false;
                    }else{
                        String keyA = stack.peek();
                        String keyC = expresion.charAt(i)+"";
                        if(keyA.equals("(")&&keyC.equals(")"))
                            stack.pop();
                        else{
                            JOptionPane.showMessageDialog(this,"Las llaves no coinciden");
                            return false;
                        }
                    }
                break;
            }
        }
        if(!stack.empty())
            JOptionPane.showMessageDialog(this,"Falta llave de clausura");
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        resultScrollPane = new javax.swing.JScrollPane();
        resultPanel = new javax.swing.JPanel();
        elimianteButton = new javax.swing.JButton();
        calculateButton = new javax.swing.JButton();
        cleanButton = new javax.swing.JButton();
        numberTextField1 = new javax.swing.JTextField();
        fondoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculadora");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        resultPanel.setPreferredSize(new java.awt.Dimension(430, 260));

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 437, Short.MAX_VALUE)
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        resultScrollPane.setViewportView(resultPanel);

        getContentPane().add(resultScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 460, 260));

        elimianteButton.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        elimianteButton.setText("ELIMINAR");
        elimianteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elimianteButtonActionPerformed(evt);
            }
        });
        getContentPane().add(elimianteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1, -1));

        calculateButton.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        calculateButton.setText("CALCULAR");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });
        getContentPane().add(calculateButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 130, -1));

        cleanButton.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        cleanButton.setText("LIMPIAR");
        cleanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cleanButtonActionPerformed(evt);
            }
        });
        getContentPane().add(cleanButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 70, 130, -1));

        numberTextField1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        numberTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(numberTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 460, 35));

        fondoLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        fondoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Fondo.jpg"))); // NOI18N
        fondoLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        getContentPane().add(fondoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 400));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        String expresion = numberTextField1.getText();
        String auxiliar = expresion;
        if(expresion.length() == 0)
            JOptionPane.showMessageDialog(null,"Faltan campos por llenar");
        else{
            try {
                Registry registryT = LocateRegistry.getRegistry("127.0.0.1",8010);
                RITrigonometrics remoteT = (RITrigonometrics)registryT.lookup("CalculadoraT");
                if(evaluateKeys(expresion)){
                    for(int i = 0; i<expresion.length() ;i++){ //Expresiones trigonometricas
                        if(expresion.charAt(i)=='t'){ //Tangente
                            int index = i + 4;
                            String tangente = new String();
                            while(expresion.charAt(index)!=')')
                                tangente = tangente + expresion.charAt(index++);
                            String aux_tan = "tan("+tangente+")";
                            double tan = remoteT.tangente(Double.parseDouble(tangente));
                            String new_tan = String.format("%.3f",tan);
                            expresion = expresion.replace(aux_tan,new_tan);
                            if(new_tan.length() == expresion.length())
                                break;
                            else
                                i = new_tan.length();  
                        }
                        if(expresion.charAt(i)=='s'){ //Seno - Secante
                            if(expresion.charAt(i+2)=='n'){ //Seno
                                int index = i + 4;
                                String seno = new String();
                                while(expresion.charAt(index)!=')')
                                    seno = seno + expresion.charAt(index++);
                                String aux_sen = "sen("+seno+")";
                                double sen = remoteT.seno(Double.parseDouble(seno));
                                String new_sen = String.format("%.3f",sen);
                                expresion = expresion.replace(aux_sen,new_sen);
                                if(new_sen.length() == expresion.length())
                                    break;
                                else
                                    i = new_sen.length();
                            }else{ //Secante
                                int index = i + 4;
                                String secante = new String();
                                while(expresion.charAt(index)!=')')
                                    secante = secante + expresion.charAt(index++);
                                String aux_sec = "sec("+secante+")";
                                double sec = remoteT.secante(Double.parseDouble(secante));
                                String new_sec = String.format("%.3f",sec);
                                expresion = expresion.replace(aux_sec,new_sec);
                                if(new_sec.length() == expresion.length())
                                    break;
                                else
                                    i = new_sec.length();
                            }
                        }
                        if(expresion.charAt(i)=='c'){ //Coseno - Cotangente - Cosecante
                            if(expresion.charAt(i+1)=='s'){ //Cosecante
                                int index = i + 4;
                                String cosecante = new String();
                                while(expresion.charAt(index)!=')')
                                    cosecante = cosecante + expresion.charAt(index++);
                                String aux_csc = "csc("+cosecante+")";
                                double csc = remoteT.cosecante(Double.parseDouble(cosecante));
                                String new_csc = String.format("%.3f",csc);
                                expresion = expresion.replace(aux_csc,new_csc);
                                if(new_csc.length() == expresion.length())
                                    break;
                                else
                                    i = new_csc.length();
                            }else{
                                if(expresion.charAt(i+2)=='s'){ //Coseno
                                    int index = i + 4;
                                    String coseno = new String();
                                    while(expresion.charAt(index)!=')')
                                        coseno = coseno + expresion.charAt(index++);
                                    String aux_cos = "cos("+coseno+")";
                                    double cos = remoteT.coseno(Double.parseDouble(coseno));
                                    String new_cos = String.format("%.3f",cos);
                                    expresion = expresion.replace(aux_cos,new_cos);
                                    if(new_cos.length() == expresion.length())
                                        break;
                                    else
                                        i = new_cos.length();
                                }else{ //Cotangente
                                    int index = i + 4;
                                    String cotangente = new String();
                                    while(expresion.charAt(index)!=')')
                                        cotangente = cotangente + expresion.charAt(index++);
                                    String aux_ctg = "ctg("+cotangente+")";
                                    double ctg = remoteT.cotangente(Double.parseDouble(cotangente));
                                    String new_ctg = String.format("%.3f",ctg);
                                    expresion = expresion.replace(aux_ctg,new_ctg);
                                    if(new_ctg.length() == expresion.length())
                                        break;
                                    else
                                        i = new_ctg.length();
                                }
                            }
                        }
                    }
                    addLabel(auxiliar+" = "+calculadora(postfija(expresion)));
                }
            } catch (RemoteException | NotBoundException ex) {
                System.out.println(ex.getMessage());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_calculateButtonActionPerformed

    private void cleanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cleanButtonActionPerformed
        resultPanel.removeAll();
        resultPanel.repaint();
        resultPanel.setPreferredSize(new Dimension(430,250));
        resultPanel.updateUI();
        positionY = 5;
    }//GEN-LAST:event_cleanButtonActionPerformed

    private void elimianteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimianteButtonActionPerformed
        numberTextField1.setText("");
    }//GEN-LAST:event_elimianteButtonActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Client().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton calculateButton;
    private javax.swing.JButton cleanButton;
    private javax.swing.JButton elimianteButton;
    private javax.swing.JLabel fondoLabel;
    private javax.swing.JTextField numberTextField1;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JScrollPane resultScrollPane;
    // End of variables declaration//GEN-END:variables
}
