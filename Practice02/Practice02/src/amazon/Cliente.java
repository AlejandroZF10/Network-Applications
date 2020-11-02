package amazon;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class Cliente extends javax.swing.JFrame {
    public static int x = 10, y = 10;
    public static int cantidad = 0;
    public Socket client = null;
    public DataInputStream input = null;
    public DataOutputStream output = null;
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Product> carrito = new ArrayList<>();
    
    public Cliente() {
        initComponents();
        this.setLocationRelativeTo(null);
        ImageIcon imagen1 = null; Icon fondo1;
        imagen1 = new ImageIcon(getClass().getResource("/images/Amazon.png"));
        fondo1 = new ImageIcon(imagen1.getImage().getScaledInstance(jLabel5.getWidth(),jLabel5.getHeight(),Image.SCALE_DEFAULT));
        jLabel5.setIcon(fondo1); 
        productsPanel.setVisible(false); 
        table.setVisible(false);
        btnDelete.setVisible(false); 
        btnBuy.setVisible(false); 
        btnTicket.setVisible(false);
    }
      
    public class ButtonActionHandler implements ActionListener {
        private final JButton button;
        public ButtonActionHandler(JButton button) {
            this.button = button;
        }
        @Override
        public void actionPerformed(ActionEvent evt) {
            int posicion = Integer.parseInt(button.getName());
            int rows = ((DefaultTableModel)jTable1.getModel()).getRowCount();
            int id = products.get(posicion).getID();
            String nombre = products.get(posicion).getName();
            float price = products.get(posicion).getPrice();
            Object row[] = {0,id,nombre,price};
            if(rows == 0)
                ((DefaultTableModel)jTable1.getModel()).addRow(row);
            else{
                int flag = 0;
                for(int i=0; i<rows; i++) {
                    int value = (int)jTable1.getValueAt(i,1);
                    if(value == id){
                        flag = 0; break;
                    }
                    else
                        flag = 1;
                }
                if(flag == 1)
                    ((DefaultTableModel)jTable1.getModel()).addRow(row);
                else
                    JOptionPane.showMessageDialog(null,"El artículo ya ha sido seleccionado");
            }            
        }
    }
    
    private void setPanel(){
        int tamPanel = 0;
        if(cantidad<4)
            tamPanel = 360;
        else{
            if(cantidad%3 == 0)
                tamPanel = (cantidad/3) * 300;
            else
                tamPanel = ((cantidad/3)+1) * 300;
        }
        jPanel1.setPreferredSize(new Dimension(500,tamPanel));
    }
    
    private void showImages(){
        String posicion = null;
        ImageIcon imagen1 = null; Icon fondo1;
        JButton[] button1 = new JButton[cantidad];
        for(int i = 0; i<cantidad ;i = i + 1){
            button1[i] = new JButton();
            button1[i].setBounds(x,y,200,250);
            if(i<9)
                posicion = "/products/0"+(i+1)+".jpg";
            else
                posicion = "/products/"+(i+1)+".jpg";
            imagen1 = new ImageIcon(getClass().getResource(posicion));
            fondo1 = new ImageIcon(imagen1.getImage().getScaledInstance(button1[i].getWidth(),button1[i].getHeight(),Image.SCALE_DEFAULT));
            button1[i].setName(Integer.toString(i));
            button1[i].setIcon(fondo1);
            button1[i].addActionListener(new ButtonActionHandler(button1[i]));
            jPanel1.add(button1[i]);
            if(x == 430){
                x = 10;
                y = y + 300;
            }
            else
                x = x + 210;
        }
    }
    
    private void showNames(){
        x = 10; y = 260;
        JLabel[] label1 = new JLabel[cantidad];
        for(int i = 0; i<cantidad ;i = i + 1){
            label1[i] = new JLabel(); 
            label1[i].setBounds(x,y,200,25);
            label1[i].setText(products.get(i).getName());
            label1[i].setFont(new Font("Arial", Font.BOLD, 15));
            label1[i].setHorizontalAlignment(SwingConstants.CENTER);
            jPanel1.add(label1[i]);
            if(x == 430){
                x = 10;
                y = y + 300;
            }
            else
                x = x + 210;
        }
        jPanel1.updateUI();
    }
    
    private void showTable(){
        productsPanel.setVisible(true); 
        table.setVisible(true);
        btnDelete.setVisible(true); 
        btnBuy.setVisible(true); 
        btnTicket.setVisible(true);
        btnTicket.setEnabled(false);
    }
    
    public void eliminar(){
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int rows = ((DefaultTableModel)jTable1.getModel()).getRowCount()-1;
        for (int i = rows; i >= 0; i--)
            model.removeRow(model.getRowCount()-1);
    }
    
    private static void createStock(ArrayList<Product> product, int posicion, int stock){
        int id = product.get(posicion).getID();
        String name = product.get(posicion).getName();
        String description = product.get(posicion).getDescription();
        float price = product.get(posicion).getPrice();
        int offer = product.get(posicion).getOffer();
        carrito.add(new Product(id,name,description,price,stock,offer));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        productsPanel = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        table = new javax.swing.JPanel();
        tablePanel = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        connectTextField = new javax.swing.JTextField();
        searchTextField = new javax.swing.JTextField();
        btnTicket = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBuy = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        btnConnect = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Practice 2 - Amazon");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(600, 300));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 358, Short.MAX_VALUE)
        );

        productsPanel.setViewportView(jPanel1);

        getContentPane().add(productsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 660, 360));

        table.setLayout(new java.awt.BorderLayout());

        jTable1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cantidad", "ID", "Nombre", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePanel.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(75);
        }

        table.add(tablePanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(table, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, 490, 300));

        connectTextField.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        connectTextField.setForeground(new java.awt.Color(102, 102, 102));
        connectTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        connectTextField.setText("Sign In");
        connectTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                connectTextFieldMouseClicked(evt);
            }
        });
        getContentPane().add(connectTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 10, 120, 40));

        searchTextField.setEditable(false);
        searchTextField.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(102, 102, 102));
        searchTextField.setText("Search");
        getContentPane().add(searchTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 600, 40));

        btnTicket.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        btnTicket.setText("Ticket");
        btnTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTicketActionPerformed(evt);
            }
        });
        getContentPane().add(btnTicket, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 440, 120, 35));

        btnDelete.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 440, 120, 35));

        btnBuy.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        btnBuy.setText("Buy it!");
        btnBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyActionPerformed(evt);
            }
        });
        getContentPane().add(btnBuy, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 440, 120, 35));

        btnSearch.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, 90, 40));

        btnConnect.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });
        getContentPane().add(btnConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 55, 120, 35));

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Today's deals   Help   Registry   Gift cards   Sell   Amazon.com   AmazonPrime.com");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 690, 30));
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 200, 80));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Portada.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 1100, 100));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Fondo.png"))); // NOI18N
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 1100, 410));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Fondo.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1100, 410));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Portada.jpg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 100));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        try{
            client = new Socket("127.0.0.1",Integer.parseInt(connectTextField.getText()));
            input = new DataInputStream(client.getInputStream());
            output = new DataOutputStream(client.getOutputStream());
            output.writeInt(0); output.flush();
            products = (ArrayList<Product>) Utils.receiveObject(input);
            cantidad = products.size();
            btnConnect.setEnabled(false);
            setPanel(); 
            showImages(); 
            showNames();
            showTable();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error en la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    private void connectTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_connectTextFieldMouseClicked
        connectTextField.setText("");
    }//GEN-LAST:event_connectTextFieldMouseClicked

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if(jTable1.getSelectedRow() == -1)
            JOptionPane.showMessageDialog(null,"Please, select a product!");
        else
            ((DefaultTableModel)jTable1.getModel()).removeRow(jTable1.getSelectedRow());  
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyActionPerformed
        int rows = ((DefaultTableModel)jTable1.getModel()).getRowCount();
        if(rows > 0){ // Si hay artículos seleccionados
            int flag = 1;
            for(int i = 0; i<rows ;i = i + 1){
                int art = (int) ((DefaultTableModel)jTable1.getModel()).getValueAt(i,1);
                for(int j = 0; j<cantidad ;j = j + 1){
                    int id = products.get(j).getID();
                    if(art == id){ //Artículos seleccionados
                        int disp = products.get(j).getStock();
                        int stock = Integer.parseInt((String)((DefaultTableModel)jTable1.getModel()).getValueAt(i,0));
                        if((disp<stock)||(stock<=0)){
                            flag = 0;
                            JOptionPane.showMessageDialog(null,"The product "+products.get(j).getName()+" is wrong");
                            break;
                        }
                    }
                }
                if(flag == 0)
                    break;
            }
            if(flag == 1){
                for(int i = 0; i<rows ;i = i + 1){
                    int art = (int) ((DefaultTableModel)jTable1.getModel()).getValueAt(i,1);
                    for(int j = 0; j<cantidad ;j = j + 1){
                        int id = products.get(j).getID();
                        if(art == id){ //Artículos seleccionados
                           int stock = Integer.parseInt((String)((DefaultTableModel)jTable1.getModel()).getValueAt(i,0));
                            createStock(products,j,stock);
                        }
                    }   
                }
                try {
                    Utils.sendObject(carrito,output);
                    if(input.readInt() == 1){
                        JOptionPane.showMessageDialog(null,"Your purchase was succesfully, print your ticket!");
                        btnTicket.setEnabled(true);
                        btnBuy.setEnabled(false);
                        btnDelete.setEnabled(false);
                        eliminar();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else // No hay artículos seleccionados
            JOptionPane.showMessageDialog(null,"Please, select a product!");
    }//GEN-LAST:event_btnBuyActionPerformed

    private void btnTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTicketActionPerformed
        try {
            output.writeInt(1);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnTicketActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            new Cliente().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuy;
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTicket;
    private javax.swing.JTextField connectTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable jTable1;
    private javax.swing.JScrollPane productsPanel;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JPanel table;
    private javax.swing.JScrollPane tablePanel;
    // End of variables declaration//GEN-END:variables
}