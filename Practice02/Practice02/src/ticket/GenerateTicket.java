package ticket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class GenerateTicket {
    public static final String USER = "root";
    public static final String URL = "jdbc:mysql://localhost:3306/amazon";
    
    public void ticket(int idusuario){
        try{
            JasperReport ticket = (JasperReport)JRLoader.loadObject("Ticket.jasper");
            Map parametro = new HashMap();
            parametro.put("idusuario",idusuario);
            JasperPrint j = JasperFillManager.fillReport(ticket,parametro,getConnection());
            JasperViewer jv = new JasperViewer(j,false);
            jv.setTitle("Ticket de Compra");
            jv.setVisible(true);
        } catch (JRException | SQLException ex) {
            Logger.getLogger(GenerateTicket.class.getName()).log(Level.SEVERE, null, ex);
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
}