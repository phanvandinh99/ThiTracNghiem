/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class DBConnection  {
    private static Properties loadConfig() {
        Properties props = new Properties();
        // Try to load from working dir
        try (InputStream in = new FileInputStream("config.properties")) {
            props.load(in);
            return props;
        } catch (Exception ignore) {}
        // Try classpath (if packaged inside jar)
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                return props;
            }
        } catch (Exception ignore) {}
        // Fallback defaults
        props.setProperty("db.url", "jdbc:mysql://localhost/hoidap_db?serverTimezone=UTC&useSSL=false");
        props.setProperty("db.user", "root");
        props.setProperty("db.password", "");
        return props;
    }

    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties cfg = loadConfig();
            String url = cfg.getProperty("db.url");
            String user = cfg.getProperty("db.user");
            String pass = cfg.getProperty("db.password");
            if (user != null && !user.isEmpty()) {
                return DriverManager.getConnection(url, user, pass);
            } else {
                return DriverManager.getConnection(url);
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.toString(), "Loi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
