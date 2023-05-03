package Data;
import Models.Usuario;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author MIRKO
 */
public class Datos {
    String dbURL = "jdbc:postgresql://localhost:5432/batallanaval";
    String username = "postgres";
    String password = "123456789";
    //Connection conexion;
    
    private Connection connectDatabase() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbURL, username, password);
            boolean valid = connection.isValid(50000);
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }
        return connection;
    }
    public void crearTablaUsuarios(){
        Connection conexion = this.connectDatabase();
        try {
            String sql = "CREATE TABLE IF NOT EXISTS usuarios(id INT PRIMARY KEY,nick VARCHAR(15)NOT NULL, sesion_id INT NOT NULL, password VARCHAR(15) NOT NULL)";
            Statement stmt = conexion.createStatement();
            //stmt.execute("DROP TABLE IF EXISTS usuarios");
            stmt.execute(sql);
            stmt.close();
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Datos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateUsers(HashMap<Integer, Usuario> Usuarios){
        Connection conexion = this.connectDatabase();
        try {
            Statement stmt = conexion.createStatement();
            for(Integer key: Usuarios.keySet()){
                String id = String.valueOf(key);
                String nick = Usuarios.get(key).getNick();
                String password = Usuarios.get(key).getPassword();
                String seseion_id = String.valueOf(Usuarios.get(key).getSesionId());
                
                String sql = String.format("INSERT INTO usuarios (id, nick, password, sesion_id) VALUES(%s, '%s', '%s', %s)", id,nick, password, seseion_id);
                String sql_2 = "ON CONFLICT (id) DO UPDATE SET nick = excluded.nick, sesion_id = excluded.sesion_id";
                stmt.executeUpdate(sql + sql_2);
            }
            stmt.close();
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(Datos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public HashMap<Integer, Usuario> getUsuarios(){
        HashMap<Integer, Usuario> usuarios = new HashMap<>();
        try {
            Connection conexion = this.connectDatabase();
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM usuarios");
            while(rs.next()){
                int id = rs.getInt("id");
                int sesion_is = rs.getInt("sesion_id");
                String nick = rs.getString("nick");
                String password = rs.getString("password");
                Usuario user = new Usuario(sesion_is, nick, password);
                usuarios.put(id, user);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Datos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarios;
    }
}
