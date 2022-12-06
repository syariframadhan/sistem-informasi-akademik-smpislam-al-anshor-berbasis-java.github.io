package Tool;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
/**
 *
 * @author LENOVO
 */
public class koneksi {
private Connection koneksi;
 public Connection connect(){
 try{
 Class.forName("com.mysql.jdbc.Driver");
 System.out.println("koneksi berhasil");
 }
 catch(ClassNotFoundException ex){
 System.out.println("gagal koneksi"+ex);
 }
 String url = "jdbc:mysql://localhost/sistem-informasi-akademik";
 try{
 koneksi = DriverManager.getConnection(url,"root","");
 System.out.println("berhasil koneksi database");
 }
 catch (SQLException ex){
 System.out.println("gagal koneksi database"+ex);  
}
 return koneksi;
 }
}

