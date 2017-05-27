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
public class Delete extends JDialog implements ActionListener, KeyListener {

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;

    JLabel LTitulo, LSub, LSub2, LSub3, LID, LNombres, LApellidos, LTipoDocumento, LDocumento, LFecNacimiento, LDia, LMes, LAño, LEdad, LEdadAños, LDireccion, LCelular, LTelefono, LBautizado, LSellado, LComentario;
    JTextField TID, TNombres, TApellidos, TDocumento, TFecNacimiento, TDia, TMes, TAño, TDireccion, TCelular, TTelefono;
    JComboBox CTipoDocumento, CBautizado, CSellado;
    JTextArea TComentario;
    JScrollPane scroll;
    JButton BConsultar, BEliminar, BReiniciar;

    String Nombre, Apellido, TipoDocumento = "N/A", Documento = "", Documento1 = "", FechaNacimiento = "", Telefono = "", Celular = "", Direccion = "", Bautizado = "", Sellado = "", Comentario = "";
    String TiposDocumentos[] = {"Seleccione...", "CC - Cédula de Ciudadanía", "TI - Tarjeta de Identidad", "RC - Registro Civil"};
    String SiNo[] = {"Si", "No"};
    int LimiteID = 5, LimiteNombre = 50, LimiteApellido = 50, LimiteDocumento = 30, LimiteDia = 2, LimiteMes = 2, LimiteAño = 4, LimiteDireccion = 100, LimiteCelular = 11, LimiteTelefono = 8, LimiteComentario = 300;
    int Edad = 0, Dia = 0, Mes = 0, Año = 0, ID = 0, Tamaño = 0;

    public Delete(Frame parent, boolean modal, int id_cli) {
        super(parent, modal);
        try {
            setTitle("Eliminar Persona - ARTURO 1.0.5");
            setLayout(null);
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/Edit.png")).getImage();
            setIconImage(icon);

            ID = id_cli;

            //------
            Textos();
            Cajas();
            Botones();

            if (ID > 0) {
                BConsultar.setEnabled(true);
                Consultar(ID);
            } else if (ID == 0) {
                ReiniciarCampos();
            }

            setSize(475, 690);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al abrir la ventana.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Delete.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la acción se cancelará.", "¡ERROR! - Delete.java", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Textos() {
        LTitulo = new JLabel("ELIMINACIÓN DE PERSONA");
        LTitulo.setFont(LTitulo.getFont().deriveFont(14f));
        LTitulo.setBounds(140, 2, 300, 20);

        LSub = new JLabel("Digite el ID de la persona que desea eliminar de la base de datos");
        LSub.setBounds(45, 25, 440, 20);

        LSub2 = new JLabel("*RECUERDE: Al eliminar una persona no abrá forma de recuperarla nuevamente.");
        LSub2.setBounds(12, 50, 440, 20);
        LSub2.setFont(LTitulo.getFont().deriveFont(9f));

        LID = new JLabel("ID:");
        LID.setBounds(120, 90, 440, 20);

        LNombres = new JLabel("Nombre(s)*:");
        LNombres.setBounds(15, 130, 440, 20);

        LApellidos = new JLabel("Apellidos*:");
        LApellidos.setBounds(15, 160, 440, 20);

        LTipoDocumento = new JLabel("Tipo Documento:");
        LTipoDocumento.setBounds(15, 190, 440, 20);

        LDocumento = new JLabel("Número Documento:");
        LDocumento.setBounds(15, 220, 440, 20);

        LFecNacimiento = new JLabel("Fecha Nacimiento:");
        LFecNacimiento.setBounds(15, 250, 440, 20);

        LDia = new JLabel("Día:");
        LDia.setBounds(140, 250, 440, 20);

        LMes = new JLabel("Mes:");
        LMes.setBounds(210, 250, 440, 20);

        LAño = new JLabel("Año:");
        LAño.setBounds(280, 250, 440, 20);

        LEdad = new JLabel("Edad:");
        LEdad.setBounds(15, 280, 440, 20);

        LEdadAños = new JLabel("0 años.");
        LEdadAños.setBounds(140, 280, 440, 20);

        LDireccion = new JLabel("Dirección Domicilio:");
        LDireccion.setBounds(15, 310, 440, 20);

        LCelular = new JLabel("Número Celular:");
        LCelular.setBounds(15, 340, 440, 20);

        LTelefono = new JLabel("Número Teléfono:");
        LTelefono.setBounds(15, 370, 440, 20);

        LBautizado = new JLabel("Persona Bautizada*:");
        LBautizado.setBounds(15, 400, 440, 20);

        LSellado = new JLabel("Persona Sellada*:");
        LSellado.setBounds(15, 430, 440, 20);

        LComentario = new JLabel("Comentario:");
        LComentario.setBounds(15, 460, 440, 20);

        add(LTitulo);
        add(LSub);
        add(LSub2);
        add(LID);
        add(LNombres);
        add(LApellidos);
        add(LTipoDocumento);
        add(LDocumento);
        add(LFecNacimiento);
        add(LDia);
        add(LMes);
        add(LAño);
        add(LEdad);
        add(LEdadAños);
        add(LDireccion);
        add(LCelular);
        add(LTelefono);
        add(LBautizado);
        add(LSellado);
        add(LComentario);
    }

    private void Cajas() {
        TID = new JTextField("" + ID);
        TID.setBounds(140, 90, 45, 20);
        TID.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        TID.addKeyListener(this);

        TNombres = new JTextField("");
        TNombres.setBounds(140, 130, 212, 20);
        TNombres.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        TApellidos = new JTextField("");
        TApellidos.setBounds(140, 160, 212, 20);

        CTipoDocumento = new JComboBox(TiposDocumentos);
        CTipoDocumento.setBounds(140, 190, 212, 20);

        TDocumento = new JTextField("");
        TDocumento.setBounds(140, 220, 212, 20);

        TDia = new JTextField("");
        TDia.setBounds(167, 250, 30, 20);

        TMes = new JTextField("");
        TMes.setBounds(243, 250, 30, 20);

        TAño = new JTextField("");
        TAño.setBounds(310, 250, 42, 20);

        TDireccion = new JTextField("");
        TDireccion.setBounds(140, 310, 212, 20);

        TCelular = new JTextField("");
        TCelular.setBounds(140, 340, 90, 20);
        TCelular.setCursor(new Cursor(Cursor.TEXT_CURSOR));

        TTelefono = new JTextField("");
        TTelefono.setBounds(140, 370, 90, 20);

        CBautizado = new JComboBox(SiNo);
        CBautizado.setBounds(140, 400, 90, 20);

        CSellado = new JComboBox(SiNo);
        CSellado.setBounds(140, 430, 90, 20);

        TComentario = new JTextArea();
        //  TComentario.setBounds(87, 310, 298, 80);        
        TComentario.setLineWrap(true);
        TComentario.setWrapStyleWord(true);
        TComentario.addKeyListener(this);

        scroll = new JScrollPane(TComentario, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(15, 490, 430, 80);

        TNombres.setEditable(false);
        TApellidos.setEditable(false);
        TDocumento.setEditable(false);
        TDia.setEditable(false);
        TMes.setEditable(false);
        TAño.setEditable(false);
        TDireccion.setEditable(false);
        TCelular.setEditable(false);
        TTelefono.setEditable(false);
        TComentario.setEditable(false);

        CTipoDocumento.setEnabled(false);
        CBautizado.setEnabled(false);
        CSellado.setEnabled(false);

        add(TID);
        add(TNombres);
        add(TApellidos);
        add(CTipoDocumento);
        add(TDocumento);
        add(TDia);
        add(TMes);
        add(TAño);
        add(TDireccion);
        add(TCelular);
        add(TTelefono);
        add(CBautizado);
        add(CSellado);
        add(scroll);
    }

    private void Botones() {
        BConsultar = new JButton("Consultar");
        BConsultar.setBounds(200, 90, 90, 20);
        BConsultar.addActionListener(this);
        BConsultar.setEnabled(false);
        add(BConsultar);

        BEliminar = new JButton("Eliminar");
        BEliminar.setBounds(110, 600, 100, 20);
        BEliminar.addActionListener(this);
        add(BEliminar);

        BReiniciar = new JButton("Reiniciar");
        BReiniciar.setBounds(230, 600, 100, 20);
        BReiniciar.addActionListener(this);
        add(BReiniciar);
    }

    private void Consultar(int ID) {
        boolean existe = false;
        String sql = "SELECT Nombres_PER, "
                + " Apellidos_PER, TypeDocument_PER, "
                + " Documento_PER, FecNacimiento_PER, "
                + " Direccion_PER, "
                + " Celular_PER, Telefono_PER, "
                + " Bautizado_PER, Sellado_PER, "
                + " Comentarios FROM PERSONAS WHERE ID_PER = " + ID + ";";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);

            while (resultado.next() != false) {
                try {
                    TNombres.setText(resultado.getString("Nombres_PER"));
                    TApellidos.setText(Utilidades.Desencriptar(resultado.getString("Apellidos_PER")));
                    TipoDocumento = Utilidades.Desencriptar(resultado.getString("TypeDocument_PER"));
                    if (TipoDocumento.equals("CC")) {
                        CTipoDocumento.setSelectedIndex(1);
                    } else if (TipoDocumento.equals("TI")) {
                        CTipoDocumento.setSelectedIndex(2);
                    } else if (TipoDocumento.equals("RC")) {
                        CTipoDocumento.setSelectedIndex(3);
                    } else {
                        CTipoDocumento.setSelectedIndex(0);
                    }
                    TDocumento.setText(Utilidades.Desencriptar(resultado.getString("Documento_PER")));
                    Documento1 = Utilidades.Desencriptar(resultado.getString("Documento_PER"));
                    FechaNacimiento = Utilidades.Desencriptar(resultado.getString("FecNacimiento_PER"));
                    String Fecha[] = FechaNacimiento.split("/");
                    TDia.setText("" + Fecha[0]);
                    TMes.setText("" + Fecha[1]);
                    TAño.setText("" + Fecha[2]);
                    LEdadAños.setText(Utilidades.CalcularEdad(Integer.parseInt(Fecha[0]), Integer.parseInt(Fecha[1]), Integer.parseInt(Fecha[2])) + " años.");
                    TDireccion.setText(Utilidades.Desencriptar(resultado.getString("Direccion_PER")));
                    TCelular.setText(Utilidades.Desencriptar(resultado.getString("Celular_PER")));
                    TTelefono.setText(Utilidades.Desencriptar(resultado.getString("Telefono_PER")));
                    CBautizado.setSelectedItem(Utilidades.Desencriptar(resultado.getString("Bautizado_PER")));
                    CSellado.setSelectedItem(Utilidades.Desencriptar(resultado.getString("Sellado_PER")));
                    TComentario.setText(Utilidades.Desencriptar(resultado.getString("Comentarios")));
                    existe = true;
                } catch (Exception ex) {

                }
            }

            if (existe == false) {
                JOptionPane.showMessageDialog(null, "No existen datos asociados a este ID (" + ID + ")\nComprueba nuevamente.", "¡ERROR! - ID sin datos", JOptionPane.ERROR_MESSAGE);
                ReiniciarCampos();
            } else if (existe == true) {
                conexion.close();
                BEliminar.setEnabled(true);
                Tamaño = TID.getText().length();
                TID.requestFocus();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al consultar la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Modify - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Modify - Consultar - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ReiniciarCampos() {
        TNombres.setText("");
        TApellidos.setText("");
        CTipoDocumento.setSelectedIndex(0);
        TDocumento.setText("");
        TDia.setText("");
        TMes.setText("");
        TAño.setText("");
        LEdadAños.setText("0 años.");
        TTelefono.setText("");
        TCelular.setText("");
        TDireccion.setText("");
        CBautizado.setSelectedIndex(0);
        CSellado.setSelectedIndex(0);
        TComentario.setText("");
        BConsultar.setEnabled(false);
        BEliminar.setEnabled(false);
        TID.requestFocus();
        TID.selectAll();
    }

    public void BorrarCliente(int ID) {
        String Nombre = "", Apellido = "";
        Nombre = (TNombres.getText());
        Apellido = (TApellidos.getText());
        int ax = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar a " + Nombre + " " + Apellido + " con ID " + ID + " permanentemente?", "Eliminar Persona - Confirmar", JOptionPane.YES_NO_OPTION);
        if (ax == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM PERSONAS WHERE ID_PER = " + ID + ";";
            try {
                DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
                conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
                sentencia = conexion.createStatement();

                sentencia.execute(sql);
                conexion.close();

                JOptionPane.showMessageDialog(null, "Se ha borrado la persona correctamente.\n\nPara ver los cambios reflejados recuerda actualizar la tabla en la página principal.", "Confirmación - Eliminación correcta", JOptionPane.INFORMATION_MESSAGE);
                ReiniciarCampos();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error al Borrar la Persona de la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Delete.java - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Delete.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //No se hace nada
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == BConsultar) {
            ID = Integer.parseInt(TID.getText());
            Consultar(ID);
        }

        if (ae.getSource() == BEliminar) {
            ID = Integer.parseInt(TID.getText());
            BorrarCliente(ID);

        }

        if (ae.getSource() == BReiniciar) {
            int ax2 = JOptionPane.showConfirmDialog(null, "El formulario se reiniciara\n¿Desea continuar?", "Confirmar - Reiniciar formulario", JOptionPane.YES_NO_OPTION);

            if (ax2 == JOptionPane.YES_OPTION) {
                TID.setText("0");
                ReiniciarCampos();
            } else if (ax2 == JOptionPane.NO_OPTION) {

            }
        }

    }

    @Override
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();

        if (ke.getSource() == TID) {

            try {
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

                if (TID.getText().length() == LimiteID) {
                    ke.consume();
                }

                if (TID.getText().length() == 0 || Integer.parseInt(TID.getText()) == 0) {
                    ReiniciarCampos();
                }
            } catch (Exception ex) {
                //
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == KeyEvent.VK_F1) {
            JOptionPane.showMessageDialog(null, "• La ventana Eliminar Persona permite borrar definitivamente a una persona y todos los datos\nrelacionados a esta que se encuentren registrados en el sistema.\n\n• Esta ventana no permite ningún tipo de modificación sobre los datos mostrados.\n\n• Para ver los datos de una persona debes digitar el número de su ID con el cual quedó registrado,\nluego pulsa el botón Consultar o la tecla Enter.\n\n• Si el ID coincide, en las casillas aparecerán los respectivos datos y se habilitará el botón para Eliminar.\n\n• El botón Eliminar solo se habilitará cuando la consulta sea exitosa, es decir, que el ID consultado se encuentra\nregistrado en el sistema.\n\n• El botón Reiniciar borra los datos que se encuentren en los campos y reinicia la consulta dejando\nel campo del ID en 0, listo para una nueva consulta.\n\n• Al aceptar Eliminar una persona no abrá forma de recuperar los datos con los que estaba registrada\nes por esto que el sistema solicita la verificación de esta acción.\n\n• Recuerde que para visualizar los cambios efectuados debe pulsar 'Actualizar Tabla (R)' en la ventana principal.\n\nAl pulsar ACEPTAR esta ventana se cerrará.", "Ayuda Eliminar Persona - ARTURO 1.0.5", JOptionPane.INFORMATION_MESSAGE);
        }
        if (ke.getSource() == TID) {
            if (key == KeyEvent.VK_ENTER) {
                BConsultar.doClick();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

        if (ke.getSource() == TID) {
            if (Tamaño > TID.getText().length()) {
                BEliminar.setEnabled(false);
            }
            try {
                if (Integer.parseInt(TID.getText()) >= 1) {
                    BConsultar.setEnabled(true);
                }
            } catch (Exception ex) {
                //
            }
        }
    }

    public static void main(String args[]) {
        /*JDialog AP = new NewUser();
         AP.setSize(400, 500);
         AP.setResizable(false);
         AP.setLocationRelativeTo(null);
         AP.setVisible(true);*/

    }

}
