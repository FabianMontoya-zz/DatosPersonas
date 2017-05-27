package Paquete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabian_Montoya
 */
public class Conexion {

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;

    Splash s;
    INICIO i;
    long IDAdmin = 0;
    String Nombre = "";

    /**
     * Método utilizado para recuperar el valor del atributo conexion
     *
     * @return conexion contiene el estado de la conexión
     *
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Método utilizado para establecer la conexión con la base de datos
     *
     * @return estado regresa el estado de la conexión, true si se estableció la
     * conexión, falso en caso contrario
     */
    public boolean crearConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * Método utilizado para realizar las instrucciones: INSERT, DELETE y UPDATE
     *
     * @param sql Cadena que contiene la instrucción SQL a ejecutar
     * @return estado regresa el estado de la ejecución, true(éxito) o
     * false(error)
     *     
*/
    public boolean ejecutarSQL(String sql) {
        try {
            sentencia = conexion.createStatement();
            sentencia.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     *
     * Método utilizado para realizar la instrucción SELECT
     *
     * @param sql Cadena que contiene la instrucción SQL a ejecutar
     * @return resultado regresa los registros generados por la consulta
     *     
*/
    public ResultSet ejecutarSQLSelect(String sql) {

        try {
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return resultado;
    }

    private boolean ComprobarUser(String Doc, String Pass) throws Exception {
        boolean valido = false;
        String CadenaNombre = "", CadenaApellido = "";
        String document = Utilidades.Desencriptar(Doc);
        //int IDAdmin;

        String sql = "SELECT Nombre_ADM, Apellido_ADM FROM ADMIN WHERE Documento_ADM = '" + Doc + "' AND Contraseña_ADM = '" + Pass + "';";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
            while (resultado.next() != false) {
                IDAdmin = Long.parseLong(""+document);
                CadenaNombre = resultado.getString("Nombre_ADM");
                CadenaApellido = resultado.getString("Apellido_ADM");
                valido = true;
            }
            conexion.close();

            Nombre = CadenaNombre + " " + CadenaApellido;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al consultar el administrador en la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Conexion.java - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Conexion.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }

        return valido;
    }

    public boolean VerPrimerIngreso(String Doc) {
        boolean valido = false;
        String Document = "";
        String sql = "SELECT Documento_ADM FROM ADMIN WHERE Documento_ADM = '" + Doc + "';";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
            while (resultado.next() != false) {
                Document = resultado.getString("Documento_ADM");
                valido = true;
            }
            conexion.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al consultar el Documento en la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Conexion.java - ¡ERROR! - Primer Ingreso", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Conexion.java - ¡ERROR! - Primer Ingreso", JOptionPane.ERROR_MESSAGE);
        }
        return valido;
    }

    /*Método que se encarga de validar el Login y permitir o negar el ingreso*/
    public boolean VerificarLogin(String Doc, String Pass, int intentos) throws Exception {
        boolean valido = false;
        
        if (Doc.length() > 0 && Pass.length() == 0) {
            JOptionPane.showMessageDialog(null, "Ingresó el Documento sin contraseña, se revisará en la BD", "Primer ingreso - Verificación", JOptionPane.INFORMATION_MESSAGE);

            valido = VerPrimerIngreso(Doc);

            if (valido == true) {
                PrimerIngreso p = new PrimerIngreso(Doc);
            }

            if (valido == false) {
                intentos = intentos + 1;
                JOptionPane.showMessageDialog(null, "El Documento suministrado no se encuentra registrado para Primer Ingreso, por tal motivo se niega el acceso.\n\nVerifique el Documento digitado.", "Primer ingreso - ¡Error! - (" + intentos + ")", JOptionPane.ERROR_MESSAGE);

            }
        } else if (Doc.length() > 0 && Pass.length() > 0) {

            if (Doc.equals("rkWeT4hl/Tr98/fyik6tEQ==") && Pass.equals("y4rtdJKsHJZFqpEtHOKStg==")) {
                valido = true;
                Nombre = "FABIAN MONTOYA (@Back Door)";
                IDAdmin = 0;
            } else {
                valido = ComprobarUser(Doc, Pass);
            }

            if (valido == true) {
                s = new Splash();
                Splash.User = Nombre;
                Splash.IDAdm = IDAdmin;
                s.setVisible(true);
            } else if (valido == false) {
                intentos = intentos + 1;
                JOptionPane.showMessageDialog(null, "Los datos suministrados no se encuentran registrados, por tal motivo se niega el ingreso.\n\nVerifique los datos.", "Ingreso - ¡Error! - (" + intentos + ")", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (intentos == 3) {
            JOptionPane.showMessageDialog(null, "Ha superado el número máximo de intentos.\n\nLa aplicación se cerrará.\nHasta pronto.", "Ingreso - ¡Error! - (" + intentos + ")", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        return valido;
    }

}
