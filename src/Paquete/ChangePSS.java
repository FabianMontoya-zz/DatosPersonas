package Paquete;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author Fabian_Montoya
 */
public class ChangePSS extends JDialog implements ActionListener, KeyListener {

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;

    JLabel LTitulo, LSub, LSub2, LSub3, LNombre, LContraseña, LNewPass, LNewPass2;
    JPasswordField TNewPass, TNewPass2, TContraseña;
    JButton BGuardar, BReiniciar;
    JLabel LMensaje, Limg;

    long ID_ADM = 0;
    int LimitePass = 40;
    String NewPass = "", NewPass2 = "", Contraseña = "", Nombre = "", PASS = "";
    boolean ax;

    private Form1 fr;

    public ChangePSS(Frame parent, boolean modal, long id_adm) {
        super(parent, modal);
        try {
            setTitle("Cambiar Contraseña de Acceso - ARTURO 1.0.5");
            setLayout(null);
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/Configurar.png")).getImage();
            setIconImage(icon);

            ID_ADM = id_adm;
            ConsultarADM(ID_ADM);
            //------
            Textos();
            Cajas();
            Botones();

            ax = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
            if (ax == true) {
                LMensaje.setVisible(true);
                Limg.setVisible(true);
            }
            if (ax == false) {
                LMensaje.setVisible(false);
                Limg.setVisible(false);
            }

            setSize(450, 330);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al abrir la ventana.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - ChangePass.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la acción se cancelará.", "¡ERROR! - ChangePass.java", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ConsultarADM(long ID) throws Exception {
        String CadenaNombre = "", CadenaApellido = "";
        String Document = Utilidades.Encriptar(""+ID);
        String sql = "SELECT Nombre_ADM, Apellido_ADM, Contraseña_ADM FROM ADMIN WHERE Documento_ADM = '" + Document + "';";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);

            while (resultado.next() != false) {
                CadenaNombre = resultado.getString("Nombre_ADM");
                CadenaApellido = resultado.getString("Apellido_ADM");
                PASS = Utilidades.Desencriptar(resultado.getString("Contraseña_ADM"));
            }
            conexion.close();
            Nombre = CadenaNombre + " " + CadenaApellido;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al consultar el administrador en la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Conexion.java - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Conexion.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void CambiarPass(String Contraseña, long ID) {
        String Pass = "";
        Pass = Utilidades.Encriptar(Contraseña);
        String Document = Utilidades.Encriptar(""+ID);
        String sql = "UPDATE ADMIN SET Contraseña_ADM = '" + Pass + "' WHERE Documento_ADM = '" + Document + "';";

        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();

            sentencia.execute(sql);
            conexion.close();

            JOptionPane.showMessageDialog(null, "Se ha actualizado tu clave de acceso exitosamente " + Nombre + ".\nÚsala la próxima vez que ingreses al sistema.", "Confirmación - Cambio de contraseña", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al Actualizar la contraseña.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "ChangePass.java - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "ChangePass.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Textos() {
        LTitulo = new JLabel("CAMBIO CONTRASEÑA DE ACCESO");
        LTitulo.setFont(LTitulo.getFont().deriveFont(14f));
        LTitulo.setBounds(95, 2, 300, 20);

        LSub = new JLabel("Digite y compruebe la nueva contraseña que desea utilizar para");
        LSub.setBounds(45, 25, 440, 20);

        LSub2 = new JLabel("acceder al sistema y pulse GUARDAR para efectuar los cambios realizados.");
        LSub2.setBounds(8, 40, 440, 20);

        LSub3 = new JLabel("* Campo obligatorio.");
        LSub3.setBounds(8, 58, 440, 20);
        LSub3.setFont(LTitulo.getFont().deriveFont(8f));

        LNombre = new JLabel("Hola " + Nombre);
        LNombre.setBounds(8, 85, 440, 20);

        LNewPass = new JLabel("Nueva contraseña:");
        LNewPass.setBounds(47, 130, 440, 20);

        LNewPass2 = new JLabel("Repita la contraseña:");
        LNewPass2.setBounds(47, 160, 440, 20);

        LContraseña = new JLabel("Contraseña anterior*:");
        LContraseña.setBounds(47, 200, 440, 20);

        LMensaje = new JLabel("Mayúsculas activas.");
        LMensaje.setBounds(190, 222, 120, 15);
        LMensaje.setFont(LMensaje.getFont().deriveFont(10f));
        LMensaje.setVisible(false);

        Limg = new JLabel();
        Limg.setBounds(175, 222, 13, 13);
        Limg.setVisible(false);
        ImageIcon fot = new ImageIcon(getClass().getResource("/Imagenes/Atencion.png"));
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(Limg.getWidth(), Limg.getHeight(), Image.SCALE_DEFAULT));
        Limg.setIcon(icono);
        this.repaint();

        add(LTitulo);
        add(LSub);
        add(LSub2);
        add(LSub3);
        add(LNombre);
        add(LNewPass);
        add(LNewPass2);
        add(LContraseña);
        add(LMensaje);
        add(Limg);

    }

    public void Cajas() {
        TNewPass = new JPasswordField();
        TNewPass.setBounds(173, 130, 180, 20);
        TNewPass.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        TNewPass.addKeyListener(this);

        TNewPass2 = new JPasswordField();
        TNewPass2.setBounds(173, 160, 180, 20);
        TNewPass2.addKeyListener(this);

        TContraseña = new JPasswordField();
        TContraseña.setBounds(173, 200, 180, 20);
        TContraseña.addKeyListener(this);

        add(TNewPass);
        add(TNewPass2);
        add(TContraseña);
    }

    private void Botones() {
        BGuardar = new JButton("Guardar");
        BGuardar.setBounds(112, 260, 100, 20);
        BGuardar.addActionListener(this);
        add(BGuardar);

        BReiniciar = new JButton("Reiniciar");
        BReiniciar.setBounds(230, 260, 100, 20);
        BReiniciar.addActionListener(this);
        add(BReiniciar);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == BReiniciar) {
            TNewPass.setText("");
            TNewPass2.setText("");
            TContraseña.setText("");
            TNewPass.requestFocus();
        }

        if (ae.getSource() == BGuardar) {

            NewPass = TNewPass.getText();
            NewPass2 = TNewPass2.getText();
            Contraseña = TContraseña.getText();
            //JOptionPane.showMessageDialog(null, "Nueva Pass: " + NewPass + "\nNueva Pass2: " + NewPass2 + "\nContraseña:  " + Contraseña + "\n\nLongitudes:\n1) " + TNewPass.getText().length() + "\n2) " + TNewPass2.getText().length() + "\n3) " + TContraseña.getText().length(), "Contraseñas...", JOptionPane.QUESTION_MESSAGE);
            if (TNewPass.getText().length() > 0 && TNewPass2.getText().length() > 0 && TContraseña.getText().length() > 0) { //Campos Vacios
                if (NewPass.equals(NewPass2)) { //Nueva contraseña coincide
                    if (Contraseña.equals(PASS)) { //Contraseña Antigua coincide
                        if (NewPass.equals(PASS) == false) { //La contraseña nueva es la misma que la antigua
                            int ax = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea cambiar su contraseña?\nLuego de cambiarla no podrá recuperar la anterior.", "Cambiar contraseña - Confirmar", JOptionPane.YES_NO_OPTION);
                            if (ax == JOptionPane.YES_OPTION) {
                                CambiarPass(NewPass, ID_ADM);
                            } else {
                                TNewPass2.setText("");
                                TContraseña.setText("");
                                TNewPass.requestFocus();
                                TNewPass.selectAll();
                            }

                        } else { //La contraseña nueva es la misma que la antigua
                            JOptionPane.showMessageDialog(null, "La nueva contraseña ingresada es igual a la que posee actualmente.\n\nDebe ingresar una nueva contraseña para poder efectuar el cambio de la clave de acceso.", "¡ERROR! - Contraseña sin cambios", JOptionPane.ERROR_MESSAGE);
                            TNewPass.setText("");
                            TNewPass2.setText("");
                            TContraseña.setText("");
                            TNewPass.requestFocus();
                        }
                    } else {//Contraseña antigua coincide
                        JOptionPane.showMessageDialog(null, "La contraseña ingresada es incorrecta.\n\nDebe ingresar su contraseña anterior para poder efectuar el cambio de la clave de acceso.", "¡ERROR! - Contraseña de acceso incorrecta", JOptionPane.ERROR_MESSAGE);
                        TNewPass.setText("");
                        TNewPass2.setText("");
                        TContraseña.setText("");
                        TNewPass.requestFocus();
                    }
                } else { //Nueva contraseña coincide
                    JOptionPane.showMessageDialog(null, "La nueva contraseña que ingresó no coincide, favor rectifique nuevamente.", "¡ERROR! - Nuevas contraseñas no coinciden", JOptionPane.ERROR_MESSAGE);

                    TNewPass2.setText("");
                    TContraseña.setText("");
                    TNewPass.requestFocus();
                    TNewPass.selectAll();
                }
            } else { //Campos Vacios
                JOptionPane.showMessageDialog(null, "Debes llenar todos los campos para efectuar el cambio de contraseña.", "¡ERROR! - Campos vacios", JOptionPane.ERROR_MESSAGE);
                TNewPass.requestFocus();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        boolean ax;
        ax = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        if (ax == true) {
            LMensaje.setVisible(true);
            Limg.setVisible(true);
        }
        if (ax == false) {
            LMensaje.setVisible(false);
            Limg.setVisible(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    public static void main(String args[]) {
        /*JDialog AP = new NewUser();
         AP.setSize(400, 500);
         AP.setResizable(false);
         AP.setLocationRelativeTo(null);
         AP.setVisible(true);*/
    }

}
