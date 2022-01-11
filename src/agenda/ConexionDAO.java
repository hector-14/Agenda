/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Héctor
 */
public class ConexionDAO {
    
    Connection conexion = null;
    List<DatosDTO> listaDatos=new ArrayList<>(); 
    
    // Conexion con la base de datos
    private void conecta(){
        String user="root"; // Preferentemente cambiar el usuario y contraseña
        String password="4587";
        String url="jdbc:mysql://localhost:3306/agenda?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"; // falta url buscar en base de datos de mysql
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // inserta los datos
    public boolean inserta(DatosDTO datos){
        boolean estado = true;
        try{
            conecta();
            PreparedStatement ps = conexion.prepareStatement("insert into datos "
                    + "(nombre,edad,sexo) values (?,?,?)");
            ps.setString(1,datos.getNombre());
            ps.setString(2,datos.getEdad());
            ps.setString(3,datos.getSexo());
            ps.execute();
        }catch(SQLException ex){
            estado = false;
        }finally{
            cerrar();
        }return estado;
    }
    
    // metodo para cargar la base 
    public boolean cargar(){
       boolean estado = true;
       DatosDTO datos;
       try{
            conecta();
            PreparedStatement ps = conexion.prepareStatement("select * from datos");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
                datos = new DatosDTO();
                
                // se declararn los campos de la tabla de mysql tal y como se escribieron
                datos.setId(resultado.getInt("id"));
                datos.setNombre(resultado.getString("nombre"));
                datos.setEdad(resultado.getString("edad"));
                datos.setSexo(resultado.getString("sexo"));
                listaDatos.add(datos);
            }
        }catch(SQLException ex){
            estado = false;
        }finally{
            cerrar();
        }return estado;
    } 
    
    // metodo para actualizar un dato
    public boolean actualizar(DatosDTO datos){
        boolean estado = true;
        try{
            conecta();
            PreparedStatement ps = conexion.prepareStatement("update datos set nombre=?, edad=?, sexo=? where id = ?");
            // se declaran los datos de forma ordenada en la que se actualizaran
            ps.setString(1,datos.getNombre());
            ps.setString(2,datos.getEdad());
            ps.setString(3,datos.getSexo());
            ps.setInt(4,datos.getId());
            // realizara los cambios
            ps.execute();
        }catch(SQLException ex){
            estado = false;
        }finally{
            cerrar();
        }return estado;
    }
    
    // metodo para elimar un registro de la base
    public boolean eliminar(DatosDTO datos){
        boolean estado = true;
        try{
            conecta();
            PreparedStatement ps = conexion.prepareStatement("delete from datos where id = ?");
            // declaracion del campo donde se eliminara el dato
            ps.setInt(1,datos.getId());
            // realizara los cambios
            ps.execute();
        }catch(SQLException ex){
            estado = false;
        }finally{
            cerrar();
        }return estado;
    }
    
    // metodo para la Lista de los datos
   public List<DatosDTO> getDatos(){
   return  listaDatos;
   }
   
    // Cerrar la conexion de la base
    private void cerrar(){
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConexionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
