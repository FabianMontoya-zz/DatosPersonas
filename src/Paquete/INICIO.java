package Paquete;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabian_Montoya
 */
public class INICIO extends JFrame implements ActionListener, KeyListener {

    JButton Ingresar, Registrar;
    JLabel LTitulo, LSub, LSub2, Lusuario, Lpassword, LLogin, LMensaje, Limg;
    JTextField Tusuario;
    JPasswordField Tpassword;

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;
    
    String usuario, contra;
    public static String User;
    boolean Valido = false;
    int LimiteNombre = 19, LimiteContra = 39, intentos = 0;

    INICIO() {
        super("Acceso - ARTURO 1.0.5");

        try {

            setLayout(null);
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/Login.png")).getImage();
            setIconImage(icon);
            IniciarDB();
            LLogin = new JLabel();
            LLogin.setBounds(5, 90, 85, 75);
            ImageIcon fot2 = new ImageIcon(getClass().getResource("/Imagenes/login_icon.gif"));
            //ImageIcon fot2 = new ImageIcon(getClass().getResource("/Imagenes/login-IOS.png"));
            Icon icono2 = new ImageIcon(fot2.getImage().getScaledInstance(LLogin.getWidth(), LLogin.getHeight(), Image.SCALE_DEFAULT));
            LLogin.setIcon(icono2);
            this.repaint();

            LTitulo = new JLabel("IDENTIFICACIÓN PARA INGRESO");
            LTitulo.setFont(LTitulo.getFont().deriveFont(15f));
            LTitulo.setBounds(50, 7, 400, 20);

            LSub = new JLabel("Digite sus datos y pulse INGRESAR para validar su ingreso.");
            LSub.setFont(LSub.getFont().deriveFont(11f));
            LSub.setBounds(8, 25, 400, 20);

            LSub2 = new JLabel("*Pulse F1 para ver el cuadro de ayuda.");
            LSub2.setFont(LSub2.getFont().deriveFont(9f));
            LSub2.setBounds(1, 255, 400, 20);

            Tusuario = new JTextField();
            Tusuario.setBounds(175, 100, 130, 20);
            Tusuario.addKeyListener(this);

            Tpassword = new JPasswordField();
            Tpassword.setBounds(175, 130, 130, 20);
            Tpassword.addKeyListener(this);

            Lusuario = new JLabel("Documento:");
            Lusuario.setBounds(90, 100, 100, 20);

            Lpassword = new JLabel("Contraseña:");
            Lpassword.setBounds(90, 130, 100, 20);

            LMensaje = new JLabel("Mayúsculas activas.");
            LMensaje.setBounds(190, 152, 120, 15);
            LMensaje.setFont(LMensaje.getFont().deriveFont(10f));
            LMensaje.setVisible(false);

            Limg = new JLabel();
            Limg.setBounds(175, 152, 13, 13);
            Limg.setVisible(false);
            ImageIcon fot = new ImageIcon(getClass().getResource("/Imagenes/Atencion.png"));
            Icon icono = new ImageIcon(fot.getImage().getScaledInstance(Limg.getWidth(), Limg.getHeight(), Image.SCALE_DEFAULT));
            Limg.setIcon(icono);
            this.repaint();

            Ingresar = new JButton("Ingresar");
            Ingresar.setBounds(110, 180, 100, 20);
            Ingresar.addActionListener(this);
            Ingresar.addKeyListener(this);

            add(LLogin);
            add(LTitulo);
            add(LSub);
            add(LSub2);
            add(Lusuario);
            add(Lpassword);
            add(LMensaje);
            add(Limg);
            add(Tusuario);
            add(Tpassword);
            add(Ingresar);
            addKeyListener(this);

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

            setSize(340, 300);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al iniciar la aplicación.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - INICIO.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la aplicación se cerrará.", "¡ERROR! - INICIO.java", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void IniciarDB() {
        String sql = "SELECT Documento_ADM FROM ADMIN;";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
            while (resultado.next() != false) {
                //
            }
            conexion.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al iniciar la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "INICIO.java - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "INICIO.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        /* String encriptado1 = "";
         String encriptado2 = "";
         String desencriptado1 = "";
         String desencriptado2 = "";
         encriptado1 = Utilidades.Encriptar("1032494911"); //Comprobante para ver encriptación y desencriptación
         System.out.println("Doc: "+encriptado1);
         encriptado2 = Utilidades.Encriptar("Fabian123");
         System.out.println("PSS: "+encriptado2);
         try {
         desencriptado1 = Utilidades.Desencriptar(encriptado1);
         System.out.println("Doc: "+desencriptado1);
         desencriptado2 = Utilidades.Desencriptar(encriptado2);
         System.out.println("PSS: "+desencriptado2);
         } catch (Exception ex) {
         
         }*/

        if (ae.getSource() == Ingresar) {

            Conexion Con = new Conexion();
            usuario = Tusuario.getText();
            contra = Tpassword.getText();

            if (Tusuario.getText().length() > 0 && Tpassword.getText().length() == 0) {
                try {
                    Valido = Con.VerificarLogin(usuario, contra, intentos);
                } catch (Exception ex) {
                    Logger.getLogger(INICIO.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (Valido == false) {
                    Tusuario.requestFocus();
                    Tusuario.selectAll();
                    intentos = intentos + 1;
                }

                if (Valido == true) {
                    // Tpassword.requestFocus();
                    intentos = 0;
                    setVisible(false);
                }

            } else {
                if (usuario.length() > 0) {
                    usuario = Utilidades.Encriptar(usuario);
                    contra = Utilidades.Encriptar(contra);
                    //System.out.println(contra);
                    //JOptionPane.showMessageDialog(null, "DOC = " + usuario + "\nPASS = " + contra + "\n\nDoc = " + Tusuario.getText() + "\nPass = " + Tpassword.getText());
                    try {
                        Valido = Con.VerificarLogin(usuario, contra, intentos);
                    } catch (Exception ex) {
                        Logger.getLogger(INICIO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (Valido == false) {
                        Tpassword.setText("");
                        Tusuario.requestFocus();
                        Tusuario.selectAll();
                        intentos = intentos + 1;
                    }
                    if (Valido == true) {
                        //El Splash se abre el Conexion.java
                        intentos = 0;
                        setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No puede ingresar con los campos vacios.\n\nDebe ingresar como minimo el documento para el acceso.", "Ingreso - ¡Error! Campos vacios", JOptionPane.ERROR_MESSAGE);
                    Tusuario.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();

        if (ke.getSource() == Tusuario) {
            if (Character.isLetter(c)) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "El campo solo admite números, no letras.\n\nIntente de nuevo.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            } else if (((int) ke.getKeyChar()) >= 33 && ((int) ke.getKeyChar()) <= 47 || ((int) ke.getKeyChar()) >= 58) { // && ((int) ke.getKeyChar()) <= 58 || ((int) ke.getKeyChar()) >= 123 && ((int) ke.getKeyChar()) <= 163 || ((int) ke.getKeyChar()) >= 166 && ((int) ke.getKeyChar()) <= 255) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de carácteres especiales.\n\nIntente de nuevo.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            } else if (((int) ke.getKeyChar()) == 32) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "Este campo no admite Espacios.\n\nIntente de nuevo.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            }
            if (Tusuario.getText().length() == LimiteNombre) {
                ke.consume();
            }
        }

        if (ke.getSource() == Tpassword) {
            if (Tpassword.getText().length() == LimiteContra) {
                ke.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();

        if (key == KeyEvent.VK_ENTER) {
            Ingresar.doClick();
        }

        if (key == KeyEvent.VK_F1) {
            JOptionPane.showMessageDialog(null, "• Para ingresar digite su número de documento y la contraseña con la cual se registró luego\npulse el botón INGRESAR o la tecla ENTER.\nSi no recuerda su contraseña, solicite una nueva con su administrador.\n\n• Si es Primer Ingreso digite el número de Documento con el cual ha sido registrado,\ndeje en blanco el campo de la contraseña y pulse el botón INGRESAR o la tecla Enter,\nel sistema lo guiará en los pasos siguientes.\n\n• Como medida de seguridad el sistema solo permite el ingreso erróneo de los datos de\ningreso Tres (3) veces, si supera este límite, la aplicación se cerrará automaticamente.\nDicha cantidad acumulada de fallos se muestra entre paréntesis en los titulos de\nlos mensajes de error que se generan.\n\n• Recuerda que al pulsar F1 en cualquiera de las pantallas se mostrará un cuadro de ayuda\n como este que le explicará de la mejor manera lo que se permite hacer en esta.\n\nAl pulsar ACEPTAR esta ventana se cerrará.", "Ayuda Login - ARTURO 1.0.5", JOptionPane.INFORMATION_MESSAGE);
        }

        boolean ax;
        ax = Toolkit.getDefaultToolkit().getLockingKeyState(ke.VK_CAPS_LOCK);
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
        JFrame aplicacion = new INICIO();
        /*aplicacion.setSize(340, 300);
         aplicacion.setResizable(false);
         aplicacion.setLocationRelativeTo(null);
         aplicacion.setVisible(true);
         aplicacion.setDefaultCloseOperation(EXIT_ON_CLOSE);*/

    }

}
