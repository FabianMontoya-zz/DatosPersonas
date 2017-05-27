package Paquete;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Fabian_Montoya
 */
public class Modify extends JDialog implements ActionListener, KeyListener, FocusListener {

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;
    
    Calendar fecha = new GregorianCalendar();
    Date fechaActual;    
    DateFormat formatoFecha = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); //Mes, día, año y hora

    JLabel LTitulo, LSub, LSub2, LSub3, LID, LNombres, LApellidos, LTipoDocumento, LDocumento, LFecNacimiento, LDia, LMes, LAño, LEdad, LEdadAños, LDireccion, LCelular, LTelefono, LBautizado, LSellado, LComentario;
    JTextField TID, TNombres, TApellidos, TDocumento, TFecNacimiento, TDia, TMes, TAño, TDireccion, TCelular, TTelefono, TComentario;
    JComboBox CTipoDocumento, CBautizado, CSellado;
    JButton BConsultar, BGuardar, BReiniciar;

    String Nombre, Apellido, TipoDocumento = "N/A", Documento="", Documento1="", FechaNacimiento="", Telefono="", Celular="", Direccion="", Bautizado="", Sellado="", Comentario="";
    String TiposDocumentos[] = {"Seleccione...", "CC - Cédula de Ciudadanía", "TI - Tarjeta de Identidad", "RC - Registro Civil"};
    String SiNo[] = {"Si", "No"};
    int LimiteID = 5, LimiteNombre = 50, LimiteApellido = 50, LimiteDocumento = 30, LimiteDia = 2, LimiteMes = 2, LimiteAño = 4, LimiteDireccion = 100, LimiteCelular = 11, LimiteTelefono = 8, LimiteComentario = 300;
    int Edad = 0, Dia = 0, Mes = 0, Año = 0, ID = 0;
    long ID_ADM = 0;

    public Modify(Frame parent, boolean modal, int id_cli, long idadm) {
        super(parent, modal);
        try {
            setTitle("Modificar Persona - ARTURO 1.0.5");
            setLayout(null);
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/Edit.png")).getImage();
            setIconImage(icon);

            ID = id_cli;
            ID_ADM = idadm;

            //------
            Textos();
            Cajas();
            Botones();

            if (ID > 0) {
                BConsultar.setEnabled(true);
                Consultar(ID);
            } else if (ID == 0) {
                BConsultar.setEnabled(false);
                TNombres.setEnabled(false);
                TApellidos.setEnabled(false);
                TDocumento.setEnabled(false);
                TDireccion.setEnabled(false);
                TCelular.setEnabled(false);
                TTelefono.setEnabled(false);
                TComentario.setEnabled(false);
                TID.requestFocus();
            }

            setSize(420, 560);
            setResizable(false);
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al abrir la ventana.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Modify.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la acción se cancelará.", "¡ERROR! - Modify.java", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void Textos() {
        LTitulo = new JLabel("MODIFICACIÓN DE PERSONA");
        LTitulo.setFont(LTitulo.getFont().deriveFont(14f));
        LTitulo.setBounds(105, 2, 300, 20);

        LSub = new JLabel("Digite el ID de la persona que desea utilizar, realice las modificaciones");
        LSub.setBounds(8, 25, 440, 20);

        LSub2 = new JLabel("y pulse GUARDAR para efectuar los cambios realizados.");
        LSub2.setBounds(38, 40, 440, 20);

        LSub3 = new JLabel("* Campo obligatorio.");
        LSub3.setBounds(15, 63, 440, 20);
        LSub3.setFont(LTitulo.getFont().deriveFont(8f));

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
        add(LSub3);
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
        TNombres.addKeyListener(this);

        TApellidos = new JTextField("");
        TApellidos.setBounds(140, 160, 212, 20);
        TApellidos.addKeyListener(this);

        CTipoDocumento = new JComboBox(TiposDocumentos);
        CTipoDocumento.setBounds(140, 190, 212, 20);
        CTipoDocumento.addActionListener(this);

        TDocumento = new JTextField("");
        TDocumento.setBounds(140, 220, 212, 20);
        TDocumento.addKeyListener(this);
        TDocumento.setEnabled(false);

        TDia = new JTextField("");
        TDia.setBounds(167, 250, 30, 20);
        TDia.addKeyListener(this);
        TDia.addFocusListener(this);

        TMes = new JTextField("");
        TMes.setBounds(243, 250, 30, 20);
        TMes.setEnabled(false);
        TMes.addKeyListener(this);
        TMes.addFocusListener(this);

        TAño = new JTextField("");
        TAño.setBounds(310, 250, 42, 20);
        TAño.setEnabled(false);
        TAño.addKeyListener(this);
        TAño.addFocusListener(this);

        TDireccion = new JTextField("");
        TDireccion.setBounds(140, 310, 212, 20);
        TDireccion.addKeyListener(this);

        TCelular = new JTextField("");
        TCelular.setBounds(140, 340, 90, 20);
        TCelular.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        TCelular.addKeyListener(this);

        TTelefono = new JTextField("");
        TTelefono.setBounds(140, 370, 90, 20);
        TTelefono.addKeyListener(this);

        CBautizado = new JComboBox(SiNo);
        CBautizado.setBounds(140, 400, 90, 20);
        CBautizado.addActionListener(this);

        CSellado = new JComboBox(SiNo);
        CSellado.setBounds(140, 430, 90, 20);
        CSellado.addActionListener(this);

        TComentario = new JTextField("");
        TComentario.setBounds(87, 460, 298, 20);
        TComentario.addKeyListener(this);

        TNombres.setEnabled(false);
        TApellidos.setEnabled(false);
        CTipoDocumento.setEnabled(false);
        TDocumento.setEnabled(false);
        TDia.setEnabled(false);
        TMes.setEnabled(false);
        TAño.setEnabled(false);
        TDireccion.setEnabled(false);
        TCelular.setEnabled(false);
        TTelefono.setEnabled(false);
        CBautizado.setEnabled(false);
        CSellado.setEnabled(false);
        TComentario.setEnabled(false);

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
        add(TComentario);
    }

    private void Botones() {
        BConsultar = new JButton("Consultar");
        BConsultar.setBounds(200, 90, 90, 20);
        BConsultar.addActionListener(this);
        BConsultar.setEnabled(false);
        add(BConsultar);

        BGuardar = new JButton("Guardar");
        BGuardar.setBounds(82, 495, 100, 20);
        BGuardar.addActionListener(this);
        add(BGuardar);

        BReiniciar = new JButton("Reiniciar");
        BReiniciar.setBounds(210, 495, 100, 20);
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
                TNombres.setEnabled(true);
                TApellidos.setEnabled(true);
                CTipoDocumento.setEnabled(true);
                TDocumento.setEnabled(true);
                TDia.setEnabled(true);
                TMes.setEnabled(true);
                TAño.setEnabled(true);
                TDireccion.setEnabled(true);
                TCelular.setEnabled(true);
                TTelefono.setEnabled(true);
                CBautizado.setEnabled(true);
                CSellado.setEnabled(true);
                TComentario.setEnabled(true);
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
        TNombres.setEnabled(false);
        TApellidos.setEnabled(false);
        CTipoDocumento.setEnabled(false);
        TDocumento.setEnabled(false);
        TDia.setEnabled(false);
        TMes.setEnabled(false);
        TAño.setEnabled(false);
        TDireccion.setEnabled(false);
        TCelular.setEnabled(false);
        TTelefono.setEnabled(false);
        CBautizado.setEnabled(false);
        CSellado.setEnabled(false);
        TComentario.setEnabled(false);
        TID.requestFocus();
        TID.selectAll();
    }

    private void Guardar(String nombre, String apellido, String tipoDocumento, String documento, String fechaNacimiento, String direccion, String celular, String telefono, String bautizado, String sellado, String comentario) {
        nombre = Nombre;
        apellido = Utilidades.Encriptar(Apellido);
        tipoDocumento = Utilidades.Encriptar(TipoDocumento);
        documento = Utilidades.Encriptar(Documento);
        fechaNacimiento = Utilidades.Encriptar(FechaNacimiento);
        direccion = Utilidades.Encriptar(Direccion);
        celular = Utilidades.Encriptar(Celular);
        telefono = Utilidades.Encriptar(Telefono);
        bautizado = Utilidades.Encriptar(Bautizado);
        sellado = Utilidades.Encriptar(Sellado);
        comentario = Utilidades.Encriptar(Comentario);
        String ID_Usuario = Utilidades.Encriptar("" + ID_ADM);
        fechaActual = new Date(); //Se inicializa acá para tomar la hora exacta en la que se inserta
        String fecha = Utilidades.Encriptar(formatoFecha.format(fechaActual));

        String sql = "UPDATE PERSONAS SET "+
                     "NOMBRES_PER = '" + nombre + "', APELLIDOS_PER = '" + apellido + "', "+
                     "TypeDocument_PER = '" + tipoDocumento + "', Documento_PER = '" + documento + "', "+
                     "FecNacimiento_PER = '" + fechaNacimiento + "', Direccion_PER = '" + direccion + "', "+
                     "Celular_PER = '" + celular + "', Telefono_PER = '" + telefono + "', "+
                     "Bautizado_PER = '" + bautizado + "', Sellado_PER = '" + sellado + "', "+
                     "COMENTARIOS  = '" + comentario + "', Usuario_Actualizacion = '"+ID_Usuario+"', "+
                     "Fecha_Actualizacion = '"+fecha+"' WHERE ID_PER  = " + ID + ";";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();

            sentencia.execute(sql);
            conexion.close();

            JOptionPane.showMessageDialog(null, "Se han guardado los cambios efectuados a " + Nombre + " " + Apellido + " correctamente.\nPara ver los cambios reflejados recuerda actualizar la tabla en la página principal.", "Confirmación - Modificación correcta", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al Modificar la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Form1 - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Modify.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int consultardocumento(String Documento, String TipoDocumento) {
        int valido = 1;
        String CadenaDocumento = "";

        Documento = Utilidades.Encriptar(Documento);
        TipoDocumento = Utilidades.Encriptar(TipoDocumento);

        String sql = "SELECT Documento_PER FROM PERSONAS WHERE Documento_PER = '" + Documento + "' AND TypeDocument_PER = '"+TipoDocumento+"';";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
            while (resultado.next() != false) {
                CadenaDocumento = resultado.getString("Documento_PER");
                valido = 0;
            }
            conexion.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al consultar el documento en la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Form1 - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Modify.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }

        return valido;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == BConsultar) {
            ID = Integer.parseInt(TID.getText());
            Consultar(ID);
        }

        if (ae.getSource() == BGuardar) {

            Nombre = TNombres.getText().toUpperCase();
            Apellido = TApellidos.getText().toUpperCase();
            TipoDocumento = TipoDocumento.toUpperCase();
            Documento = TDocumento.getText().toUpperCase();
            FechaNacimiento = Utilidades.FormatoFechaNacimiento(TDia.getText(), TMes.getText(), TAño.getText());
            Direccion = TDireccion.getText().toUpperCase();
            Celular = TCelular.getText().toUpperCase();
            Telefono = TTelefono.getText().toUpperCase();
            Bautizado = CBautizado.getSelectedItem().toString();
            Sellado = CSellado.getSelectedItem().toString();
            Comentario = TComentario.getText();

            if (Nombre.length() != 0 && Apellido.length() != 0) {
                int ax = JOptionPane.showConfirmDialog(null, "Los datos a ingresar son: \n- Nombre(s): " + Nombre + ".\n- Apellidos: " + Apellido + ".\n- Tipo Documento: " + TipoDocumento + ".\n- Documento: " + Documento + ".\n- Fecha de Nacimiento: " + FechaNacimiento + ".\n- Dirección: " + Direccion + ".\n- Número celular: " + Celular + ".\n- Número de teléfono: " + Telefono + ".\n- Bautizado: " + Bautizado + ".\n- Sellado: " + Sellado + ".\n- Comentario: " + Comentario + "\n\n ¿Son correctos?, confirme para guardar la nueva persona.", "Confirmación - Ingreso nueva persona", JOptionPane.YES_NO_OPTION);
                int documentovalido = 0;
                if (ax == JOptionPane.YES_OPTION) {
                    if ((Documento.compareTo(Documento1)) == 0) {
                        documentovalido = 1;
                    } else {
                        documentovalido = consultardocumento(Documento, TipoDocumento);
                    }
                    if (documentovalido == 1) {
                        Guardar(Nombre, Apellido, TipoDocumento, Documento, FechaNacimiento, Direccion, Celular, Telefono, Bautizado, Sellado, Comentario);
                    } else if (documentovalido == 0) {
                        JOptionPane.showMessageDialog(null, "El documento digitado ya fue asignado a un usuario, rectifique por favor.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
                        TDocumento.requestFocus();
                    }
                } else if (ax == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null, "Modificación cancelada, corrobore sus datos.");
                }
            } else {
                if (Nombre.length() == 0) {
                    JOptionPane.showMessageDialog(null, "El campo NOMBRES es obligatorio\nPor favor revice.", "¡ERROR! - Campo incompleto", JOptionPane.ERROR_MESSAGE);
                    TNombres.requestFocus();
                } else if (Apellido.length() == 0) {
                    JOptionPane.showMessageDialog(null, "El campo APELLIDOS es obligatorio\nPor favor revice.", "¡ERROR! - Campo incompleto", JOptionPane.ERROR_MESSAGE);
                    TApellidos.requestFocus();
                }

            }

        }

        if (ae.getSource() == BReiniciar) {
            int ax2 = JOptionPane.showConfirmDialog(null, "El formulario se reiniciara\n¿Desea continuar?", "Confirmar - Reiniciar formulario", JOptionPane.YES_NO_OPTION);

            if (ax2 == JOptionPane.YES_OPTION) {
                TID.setText("0");
                ReiniciarCampos();

            } else if (ax2 == JOptionPane.NO_OPTION) {

            }
        }

        if (ae.getSource() == CTipoDocumento) {
            if (CTipoDocumento.getSelectedIndex() == 0) {
                TDocumento.setText("");
                TDocumento.setEnabled(false);
                TipoDocumento = "N/A";
            } else if (CTipoDocumento.getSelectedIndex() == 1) {
                TDocumento.setText("");
                TDocumento.setEnabled(true);
                TipoDocumento = "CC";
            } else if (CTipoDocumento.getSelectedIndex() == 2) {
                TDocumento.setText("");
                TDocumento.setEnabled(true);
                TipoDocumento = "TI";
            } else if (CTipoDocumento.getSelectedIndex() == 3) {
                TDocumento.setText("");
                TDocumento.setEnabled(true);
                TipoDocumento = "RC";
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
                    BConsultar.setEnabled(false);
                    ReiniciarCampos();
                }
            } catch (Exception ex) {
                //
            }
        }

        if (ke.getSource() == TNombres) {
            if (Character.isDigit(c)) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "El campo solo admite letras, no números.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            }
            //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son 
            //minusculas
            if (ke.getKeyChar() >= 'a' && ke.getKeyChar() <= 'z') {
                //si lo son, entonces lo reemplazamos por su respectivo en mayúscula en el mismo evento
                // (esto se logra por que en java todas las variables son pasadas por referencia)
                ke.setKeyChar((char) (((int) ke.getKeyChar()) - 32));
            }

            if (((int) ke.getKeyChar()) == 39) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de este carácter.\n\nIntente con uno diferente.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            }

            if (TNombres.getText().length() == LimiteNombre - 1) {
                ke.consume();
            }

        }

        if (ke.getSource() == TApellidos) {
            if (Character.isDigit(c)) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "El campo solo admite letras, no números.", "¡ERROR!", JOptionPane.ERROR_MESSAGE);
            }
            //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son 
            //minusculas
            if (ke.getKeyChar() >= 'a' && ke.getKeyChar() <= 'z') {
                //si lo son, entonces lo reemplazamos por su respectivo en mayúscula en el mismo evento
                // (esto se logra por que en java todas las variables son pasadas por referencia)
                ke.setKeyChar((char) (((int) ke.getKeyChar()) - 32));
            }

            if (((int) ke.getKeyChar()) == 39) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de este carácter.\n\nIntente con uno diferente.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            }

            if (TApellidos.getText().length() == LimiteApellido - 1) {
                ke.consume();
            }
        }

        if (ke.getSource() == TDireccion) {
            if (ke.getKeyChar() >= 'a' && ke.getKeyChar() <= 'z') {
                //si lo son, entonces lo reemplazamos por su respectivo en mayúscula en el mismo evento
                // (esto se logra por que en java todas las variables son pasadas por referencia)
                ke.setKeyChar((char) (((int) ke.getKeyChar()) - 32));
            }

            if (((int) ke.getKeyChar()) == 39) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de este carácter.\n\nIntente con uno diferente.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            }

            if (TDireccion.getText().length() == LimiteDireccion - 1) {
                ke.consume();
            }

        }

        if (ke.getSource() == TDocumento) {
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

            if (TDocumento.getText().length() == LimiteDocumento - 1) {
                ke.consume();
            }
        }

        if (ke.getSource() == TDia) {
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

            if (TDia.getText().length() == LimiteDia) {
                ke.consume();
            }
        }

        if (ke.getSource() == TMes) {
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

            if (TMes.getText().length() == LimiteMes) {
                ke.consume();
            }
        }

        if (ke.getSource() == TAño) {
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

            if (TAño.getText().length() == LimiteAño) {
                ke.consume();
            }
        }

        if (ke.getSource() == TCelular) {
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

            if (TCelular.getText().length() == LimiteCelular - 1) {
                ke.consume();
            }

        }

        if (ke.getSource() == TTelefono) {
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
            if (TTelefono.getText().length() == LimiteTelefono - 1) {
                ke.consume();
            }
        }

        if (ke.getSource() == TComentario) {
            if (((int) ke.getKeyChar()) == 39) {
                ke.consume();
                JOptionPane.showMessageDialog(null, "No se permite el uso de este carácter.\n\nIntente con uno diferente.", "¡ERROR! - Carácter no valido", JOptionPane.ERROR_MESSAGE);
            }
            if (TComentario.getText().length() == LimiteComentario - 1) {
                ke.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();

        if (key == KeyEvent.VK_F1) {
            JOptionPane.showMessageDialog(null, "• La ventana Modificar Persona permite editar cada uno de los campos con los que habíamos registrado \nuna persona anteriormente.\n\n• Para ver los datos de una persona debes digitar el número de su ID con el cual quedó registrado, luego pulsa el\nbotón Consultar o la tecla Enter.\nSi el ID coincide, en las casillas aparecerán los respectivos datos y se habilitarán para su modificación.\n\n• A la hora de modificar, los campos Nombres y Apellidos son obligatorios, no los puedes dejar en blanco.\n\n• A excepción de los comentarios, todos los campos se escribirán en mayúsculas automáticamente, sino \nlo hacen al instante, a la hora de confirmar el registro rectifica que todos los datos estén en mayúsculas.\n\n• Para efectuar los cambios dados debes pulsar el botón Guardar, te pedirá que confirmes los datos, al confirmar\nse guardarán los datos según fueron modificados.\n\n• Para visualizar los cambios efectuados debes pulsar 'Actualizar Tabla (R)' en la ventana principal de la aplicación.\n\nAl pulsar ACEPTAR esta ventana se cerrará.", "Ayuda Modificar Persona - ARTURO 1.0.5", JOptionPane.INFORMATION_MESSAGE);
        }
        if (ke.getSource() == TID) {
            if (key == KeyEvent.VK_ENTER) {
                BConsultar.doClick();
            }
        }

        if (ke.getSource() == TDia) {
            TMes.setEnabled(false);
            TAño.setEnabled(false);
        }

        if (ke.getSource() == TMes) {
            TAño.setEnabled(false);
        }

        if (ke.getSource() == TAño) {
            LEdadAños.setText("0 años.");
            Edad = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        if (ke.getSource() == TID) {
            try {
                if (Integer.parseInt(TID.getText()) >= 1) {
                    BConsultar.setEnabled(true);
                }
            } catch (Exception ex) {
                //
            }
        }

        char c = ke.getKeyChar();

        if (ke.getSource() == TDia && Character.isDigit(c)) {

            Dia = Integer.parseInt(TDia.getText());
            if (Dia > 31 || Dia < 1) {
                JOptionPane.showMessageDialog(null, "El Día digitado debe estar dentro del rango 01 a 31\npara ser valido.", "¡ERROR! - Día No valido", JOptionPane.ERROR_MESSAGE);
                TDia.requestFocus();
                TDia.selectAll();
                TMes.setEnabled(false);
                TMes.setText("");
            } else {
                TMes.setEnabled(true);
                TMes.setText("");
            }
        }

        if (ke.getSource() == TMes && Character.isDigit(c)) {

            Mes = Integer.parseInt(TMes.getText());
            if (Mes > 12 || Mes < 1) {
                JOptionPane.showMessageDialog(null, "El Mes digitado debe estar dentro del rango 01 a 12\npara ser valido.", "¡ERROR! - Mes No valido", JOptionPane.ERROR_MESSAGE);
                TMes.requestFocus();
                TMes.selectAll();
                TAño.setEnabled(false);
                TAño.setText("");
            } else {
                TAño.setText("");
                TAño.setEnabled(true);
            }
        }

        if (ke.getSource() == TAño && Character.isDigit(c)) {

            if (TAño.getText().length() == 4) {
                int añoactual = fecha.get(Calendar.YEAR);
                int mesactual = fecha.get(Calendar.YEAR);
                int añominimo = añoactual - 60;
                int añomaximo = añoactual - 10;
                Año = Integer.parseInt(TAño.getText());
                if (Año > añomaximo || Año < añominimo) {
                    JOptionPane.showMessageDialog(null, "El Año digitado debe estar dentro del rango " + añominimo + " a " + añomaximo + "\npara ser valido.", "¡ERROR! - Año No valido", JOptionPane.ERROR_MESSAGE);
                    TAño.requestFocus();
                    TAño.selectAll();
                } else {
                    Edad = Utilidades.CalcularEdad(Dia, Mes, Año);
                    LEdadAños.setText(Edad + " años.");
                }
            }
        }
    }

    @Override
    public void focusGained(FocusEvent fe) {
    }

    @Override
    public void focusLost(FocusEvent fe) {

        if (fe.getSource() == TDia) {
            String diatxt = TDia.getText();
            if (diatxt.equals("")) {
                TMes.setEnabled(false);
                TAño.setEnabled(false);
            } else {
                if (diatxt.length() == 1) {
                    diatxt = "0" + diatxt;
                    TDia.setText(diatxt);
                }
            }
        }

        if (fe.getSource() == TMes) {
            String Mes = TMes.getText();
            if (Mes.equals("")) {
                TAño.setEnabled(false);
            } else {
                if (Mes.length() == 1) {
                    Mes = "0" + Mes;
                    TMes.setText(Mes);
                }
            }
        }

        if (fe.getSource() == TAño) {
            String Año = TAño.getText();
            if (Año.equals("") || Año.length() < 4) {
                JOptionPane.showMessageDialog(null, "El año debe ser de 4 dígitos exactos. Ej: 1995, 2001.", "¡ERROR! - Año No valido", JOptionPane.ERROR_MESSAGE);
                TAño.requestFocus();
                TAño.selectAll();
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
