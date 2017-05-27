package Paquete;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Fabian_Montoya
 */
public class PrimerIngreso extends JFrame implements ActionListener, KeyListener {

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;

    JLabel LTitulo, LSub, LSub2, LSub3, LNombre, LApellido, LDocumento, LPass, LPass2, LMensaje, Limg;
    JTextField TNombre, TApellido, TDocumento;
    JPasswordField TPass, TPass2;
    JButton BGuardar, BReiniciar;

    String Nombre = "", Apellido = "", Documento = "", Pass = "", Pass2 = "";
    int LimiteNombre = 30, LimiteApellido = 30, LimitePass = 40;

    public PrimerIngreso(String Doc) {
        super("Primer Registro - ARTURO 1.0.5");
        try {
            setLayout(null);
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/Register.png")).getImage();
            setIconImage(icon);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            setSize(400, 350);
            setResizable(false);
            Documento = Doc;

            Textos();
            Cajas();
            Botones();

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

            addWindowListener(new WindowAdapter() {
                @Override

                public void windowClosing(WindowEvent e) {
                    INICIO i = new INICIO();
                }
            });

            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al abrir la ventana.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - PrimerIngreso.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la acción se cancelará.", "¡ERROR! - PrimerIngreso.java", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void Textos() {
        LTitulo = new JLabel("DATOS ADMINISTRADOR");
        LTitulo.setFont(LTitulo.getFont().deriveFont(14f));
        LTitulo.setBounds(112, 2, 300, 20);

        LSub = new JLabel("Ingrese todos los datos solicitados y luego pulse GUARDAR");
        LSub.setBounds(25, 25, 440, 20);

        LSub2 = new JLabel("para efectuar los cambios.");
        LSub2.setBounds(115, 40, 440, 20);

        LSub3 = new JLabel("*Pulse F1 para ver el cuadro de ayuda.");
        LSub3.setFont(LSub3.getFont().deriveFont(9f));
        LSub3.setBounds(1, 305, 400, 20);

        LDocumento = new JLabel("Documento Administrador:");
        LDocumento.setBounds(15, 80, 440, 20);

        LNombre = new JLabel("Primer Nombre:");
        LNombre.setBounds(25, 120, 440, 20);

        LApellido = new JLabel("Primer Apellido:");
        LApellido.setBounds(25, 150, 440, 20);

        LPass = new JLabel("Contraseña:");
        LPass.setBounds(25, 180, 440, 20);

        LPass2 = new JLabel("Repita la contraseña:");
        LPass2.setBounds(25, 210, 440, 20);

        LMensaje = new JLabel("Mayúsculas activas.");
        LMensaje.setBounds(165, 232, 120, 15);
        LMensaje.setFont(LMensaje.getFont().deriveFont(10f));
        LMensaje.setVisible(false);

        Limg = new JLabel();
        Limg.setBounds(150, 232, 13, 13);
        Limg.setVisible(false);
        ImageIcon fot = new ImageIcon(getClass().getResource("/Imagenes/Atencion.png"));
        Icon icono = new ImageIcon(fot.getImage().getScaledInstance(Limg.getWidth(), Limg.getHeight(), Image.SCALE_DEFAULT));
        Limg.setIcon(icono);
        this.repaint();

        add(LTitulo);
        add(LSub);
        add(LSub2);
        add(LSub3);
        add(LDocumento);
        add(LNombre);
        add(LApellido);
        add(LPass);
        add(LPass2);
        add(LMensaje);
        add(Limg);

    }

    private void Cajas() {
        TDocumento = new JTextField(Documento);
        TDocumento.setBounds(170, 80, 130, 20);
        TDocumento.setEditable(false);
        // TDocumento.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        TDocumento.addKeyListener(this);

        TNombre = new JTextField("");
        TNombre.setBounds(150, 120, 170, 20);
        TNombre.addKeyListener(this);

        TApellido = new JTextField("");
        TApellido.setBounds(150, 150, 170, 20);
        TApellido.addKeyListener(this);

        TPass = new JPasswordField();
        TPass.setBounds(150, 180, 170, 20);
        TPass.addKeyListener(this);

        TPass2 = new JPasswordField();
        TPass2.setBounds(150, 210, 170, 20);
        TPass2.addKeyListener(this);

        add(TDocumento);
        add(TNombre);
        add(TApellido);
        add(TPass);
        add(TPass2);

    }

    private void Botones() {
        BGuardar = new JButton("Guardar");
        BGuardar.setBounds(82, 260, 100, 20);
        BGuardar.addActionListener(this);
        add(BGuardar);

        BReiniciar = new JButton("Reiniciar");
        BReiniciar.setBounds(200, 260, 100, 20);
        BReiniciar.addActionListener(this);
        add(BReiniciar);
    }

    private void GuardarAdmin() {
        String nombre = "", apellido = "", documento = "", contra = "";
        nombre = Nombre;
        apellido = Apellido;
        documento = Utilidades.Encriptar(Documento);
        contra = Utilidades.Encriptar(Pass);
        String sql = "UPDATE ADMIN SET Nombre_ADM = '" + nombre + "', Apellido_ADM = '" + apellido + "', Documento_ADM = '" + documento + "', Contraseña_ADM = '" + contra + "';";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();

            sentencia.execute(sql);
            conexion.close();

            JOptionPane.showMessageDialog(null, "Registro completado\n\n" + Nombre + " " + Apellido + " ya puedes ingresar al sistema.", "Confirmación - Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            INICIO i = new INICIO();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al Insertar en la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "PrimerIngreso.java - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "PrimerIngreso.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == BReiniciar) {
            int ax2 = JOptionPane.showConfirmDialog(null, "El formulario se reiniciará\n¿Desea continuar?", "Confirmar - Reiniciar formulario", JOptionPane.YES_NO_OPTION);

            if (ax2 == JOptionPane.YES_OPTION) {
                TNombre.setText("");
                TApellido.setText("");
                TPass.setText("");
                TPass2.setText("");
                TNombre.requestFocus();

            } else if (ax2 == JOptionPane.NO_OPTION) {

            }
        }

        if (ae.getSource() == BGuardar) {
            Nombre = TNombre.getText();
            Apellido = TApellido.getText();
            Pass = TPass.getText();
            Pass2 = TPass2.getText();
            if (Nombre.length() != 0 && Apellido.length() != 0) {
                if (Pass.equals(Pass2) && Pass.length() != 0) {
                    int ax = JOptionPane.showConfirmDialog(null, "Verifique sus datos, luego de confirmar solo podrá cambiar la contraseña\n\n- Nombre: " + Nombre + ".\n- Apellido: " + Apellido + ".\n\n ¿Son correctos?.", "Confirmación - Datos Administrador", JOptionPane.YES_NO_OPTION);

                    if (ax == JOptionPane.YES_OPTION) {
                        GuardarAdmin();
                    } else if (ax == JOptionPane.NO_OPTION) {
                        JOptionPane.showMessageDialog(null, "Registro cancelado, rectifique sus datos.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "La contraseña es incorrecta o no coincide, por favor revisar.", "¡ERROR! - Contraseña incorrecta", JOptionPane.ERROR_MESSAGE);
                    TPass.setText("");
                    TPass2.setText("");
                    TPass.requestFocus();
                }
            } else {
                if (Nombre.length() == 0) {
                    JOptionPane.showMessageDialog(null, "El campo NOMBRE es obligatorio, por favor revisar.", "¡ERROR! - Campo vacio", JOptionPane.ERROR_MESSAGE);
                    TPass.setText("");
                    TPass2.setText("");
                    TNombre.requestFocus();
                } else if (Apellido.length() == 0) {
                    JOptionPane.showMessageDialog(null, "El campo APELLIDO es obligatorio, por favor revisar.", "¡ERROR! - Campo vacio", JOptionPane.ERROR_MESSAGE);
                    TPass.setText("");
                    TPass2.setText("");
                    TApellido.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();

        if (ke.getSource() == TNombre) {
            if (Character.isDigit(c)) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "El campo solo admite letras, no números.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            } else if (((int) ke.getKeyChar()) >= 33 && ((int) ke.getKeyChar()) <= 64 || ((int) ke.getKeyChar()) >= 91 && ((int) ke.getKeyChar()) <= 96 || ((int) ke.getKeyChar()) >= 123 && ((int) ke.getKeyChar()) <= 163 || ((int) ke.getKeyChar()) >= 166 && ((int) ke.getKeyChar()) <= 255) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de caracteres especiales.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            } else if (((int) ke.getKeyChar()) == 32) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "Este campo no admite Espacios.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            }
            //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son 
            //minusculas
            if (ke.getKeyChar() >= 'a' && ke.getKeyChar() <= 'z') {
                //si lo son, entonces lo reemplazamos por su respectivo en mayúscula en el mismo evento
                // (esto se logra por que en java todas las variables son pasadas por referencia)
                ke.setKeyChar((char) (((int) ke.getKeyChar()) - 32));
            }

            if (TNombre.getText().length() == LimiteNombre - 1) {
                ke.consume();
            }
        }
        if (ke.getSource() == TApellido) {
            if (Character.isDigit(c)) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "El campo solo admite letras, no números.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            } else if (((int) ke.getKeyChar()) >= 33 && ((int) ke.getKeyChar()) <= 64 || ((int) ke.getKeyChar()) >= 91 && ((int) ke.getKeyChar()) <= 96 || ((int) ke.getKeyChar()) >= 123 && ((int) ke.getKeyChar()) <= 163 || ((int) ke.getKeyChar()) >= 166 && ((int) ke.getKeyChar()) <= 255) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de caracteres especiales.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            } else if (((int) ke.getKeyChar()) == 32) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "Este campo no admite Espacios.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            }
            //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son 
            //minusculas
            if (ke.getKeyChar() >= 'a' && ke.getKeyChar() <= 'z') {
                //si lo son, entonces lo reemplazamos por su respectivo en mayúscula en el mismo evento
                // (esto se logra por que en java todas las variables son pasadas por referencia)
                ke.setKeyChar((char) (((int) ke.getKeyChar()) - 32));
            }

            if (TApellido.getText().length() == LimiteApellido - 1) {
                ke.consume();
            }
        }

        if (ke.getSource() == TPass) {
            if (((int) ke.getKeyChar()) == 39) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de este carácter.\n\nIntente con uno diferente.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            }
            if (TPass.getText().length() == LimitePass - 1) {
                ke.consume();
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();

        if (key == KeyEvent.VK_F1) {
            JOptionPane.showMessageDialog(null, "• En el campo Nombre debes digitar solo tu primer nombre.\n\n• En el campo Apellido solo debes digitar tu primer apellido.\n\n• A la hora de elegir tu contraseña, asegúrate que sea fácil de recordar pero a la vez que sea segura,\nel sistema distingue mayúsculas de minúsculas, es decir, no es lo mismo escribir «SthepHanny123»\nque escribir «sthephanny123», intenta usar una combinación de mayúsculas, minúsculas, números,\ny si prefieres, puedes agregar símbolos para una mayor seguridad.\n\n• El sistema te avisará si tienes activa la tecla mayúscula, así evitarás confundirte a la hora de\nescribir tu contraseña.\n\n• Los datos aquí suministrados serán con los que el sistema te identificará, tu documento y\ncontraseña digitados serán tu llave para poder ingresar al programa, no debes olvidarlos.\n\n• Al pulsar Guardar el sistema te pedirá verificar tu Nombre y Apellido ya que estos datos\nno podrás modificarlos luego, solo podrás cambiar tu contraseña tras ingresar al sistema.\n\nAl pulsar ACEPTAR esta ventana se cerrará.", "Ayuda Primer Ingreso - ARTURO 1.0.5", JOptionPane.INFORMATION_MESSAGE);
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
        //
    }

    public static void main(String args[]) {
        /*JDialog aplicacion = new NewUser();
         aplicacion.setSize(400, 500);
         aplicacion.setResizable(false);
         aplicacion.setLocationRelativeTo(null);
         aplicacion.setVisible(true);*/

    }
}
