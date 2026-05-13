package br.edu.unijui;

import br.edu.unijui.Message.Priority;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.util.List;

/**
 * Avaliação
 * Disciplina de Programação para Camada de Negócio
 * @author <<< Put your full name here >>> e Professor Rafael Zancan Frantz
 */
public class MessageManagerDB {

   
    public static void store(List<Message> messageList) {
        Connection con = getConnection();       
        if(con == null){
            System.out.println("Conexão falhou");
            return;
        }
        
        String sql = """
                    INSERT INTO MESSAGES (
                            ID, 
                            PRIORITY, 
                            CREATION_DATE,
                            EXPIRATION_DATE, 
                            TARGET_PORT_ID, 
                            SEQUENCE_NUMBER, 
                            CONTENT) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                     """;
        try{
            con.setAutoCommit(false);
            
            PreparedStatement pstm = con.prepareStatement(sql);
            
            for(Message msg : messageList){
                
                pstm.setString (1, msg.getId().toString());
                pstm.setInt    (2, msg.getPriority().ordinal());
                pstm.setDate   (3, new java.sql.Date(msg.getCreationDate().getTime()));
                pstm.setDate   (4, new java.sql.Date(msg.getExpirationDate().getTime()));
                pstm.setString (5, msg.getTargetPort().toString());
                pstm.setShort  (6, (short) msg.getSequenceNumber());
                pstm.setString (7, msg.getContent());
                
                pstm.executeUpdate(); 

                con.commit();
                
            }
        } catch (SQLException e) {
            System.out.println("Erro! Fazendo rollback: " + e.getMessage());
            try {
                con.rollback(); 
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                con.close(); 
            } catch (SQLException e) { 
                System.out.println(e.getMessage()); 
            }
        }
    }
    
    
    public static void printMessages( Priority priority ) {
        
        Connection con = getConnection();
        if(con == null){
            System.out.println("Conexão falhou");
        }
        
        String sql = """
                    SELECT
                        ID, 
                        PRIORITY, 
                        CREATION_DATE, 
                        EXPIRATION_DATE, 
                        CONTENT 
                    FROM MESSAGES 
                    WHERE PRIORITY =             
                    """ + priority.ordinal();
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            boolean achou = false;
            
            while(rs.next()){
                achou = true;
                System.out.println("----------------------------------------------------------");
                System.out.println("ID:                " + rs.getString("ID"));
                System.out.println("Prioridade:        " + Priority.values()[rs.getInt("PRIORITY")]);
                System.out.println("Data de criação:   " + rs.getDate("CREATION_DATE"));
                System.out.println("Data de expiração: " + rs.getDate("EXPIRATION_DATE"));
                System.out.println("Conteúdo:          " + rs.getString("CONTENT"));
            }
            if (!achou) {
                System.out.println("Nenhuma mensagem com prioridade: " + priority);
            }
           
            rs.close();
            
            st.close();
        } catch (SQLException e) {
             System.out.println("Erro na consulta: " + e.getMessage());
        } finally {
        try { 
            con.close(); 
        } catch (SQLException e) { 
            System.out.println(e.getMessage()); 
        }
        }    
    }

    
    /**
     * When invoked, it opens a connection to the database.
     *
     * @return the connection
     */
    private static Connection getConnection() {
        String url    = "jdbc:derby://localhost:1527/exam-db";
        String use    = "root";
        String pwd    = "rootpwd";
        
        try{
            Connection con = DriverManager.getConnection(url, use, pwd);
            return con;
        } catch( SQLException ex){
            System.out.println("Erro ao conectar: " + ex.getMessage());
            return null;
        }
    }

}
