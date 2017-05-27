package Paquete;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Fabian_Montoya
 */
public class Form1 extends JFrame implements ActionListener, KeyListener, MouseListener {

    Connection conexion;
    ResultSet resultado;
    Statement sentencia;

    JTextField Tusuario;
    JLabel LTitulo, LSub, LUsuario, L2, L4, L5;
    JButton BConsultar, BModificar, BBorrar, BActualizar, BExportar;

    String User;
    String[] data = new String[500];
    int pulso = 0, ID = 0;

    long ID_ADM = 0;

    JMenu Menu1, Menu2, Menu3, Menu4;
    JMenuBar MBarra;
    JMenuItem Item1, Item2, Item3, Item4, Item5, Item6, Item7, Item8, Item9, Item10;

    JTable tabla;
    JScrollPane scroll;
    DefaultTableModel ModeloTabla;
    GridBagLayout GRID = new GridBagLayout();
    String[] columnNames = {"ID Persona", "Nombres", "Apellidos", "Tipo Documento", "Documento", "Fecha Nacimiento", "Edad (Años)", "Dirección", "Celular", "Teléfono", "Bautizado", "Sellado", "Comentarios", "Usuario Creación", "Fecha Creación", "Usuario Actualización", "Fecha Actualización"};

    private boolean open = true;

    public Form1(String USUARIO, long IDADM) {
        super("Datos Personas - ARTURO 1.0.5");
        try {

            setLayout(GRID);
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/admin.png")).getImage();
            setIconImage(icon);
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            User = USUARIO;
            ID_ADM = IDADM;
            menu();
            botones();
            textos();
            Cajas();
            panel();

            Tusuario.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            getContentPane().addKeyListener(this);

            addWindowListener(new WindowAdapter() {
                @Override

                public void windowClosing(WindowEvent e) {
                    control();
                }
            });
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE); //Evitar que se cierre al dar X o Alt + F4
            setSize(1000, 740);
            setResizable(true);
            //setVisible(true);
            setLocationRelativeTo(null);
            setExtendedState(MAXIMIZED_BOTH);
            Tusuario.requestFocus();
            Item7.setEnabled(false);
            if (ID_ADM == 0) { //Si entra con la Back Door
                Item7.setEnabled(false);
                Item8.setEnabled(false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al abrir la ventana.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la aplicación se cerrará.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        open = true;
    }

    public void control() {
        int ax = JOptionPane.showConfirmDialog(null, "Se cerrará la sesión.\n¿Desea continuar?", "Cerrar sesión - Confirmar", JOptionPane.YES_NO_OPTION);
        if (ax == JOptionPane.YES_OPTION) {
            dispose();
            INICIO r = new INICIO();
            r.setSize(340, 300);
            r.setResizable(false);
            r.setVisible(true);
            r.setDefaultCloseOperation(EXIT_ON_CLOSE);
            r.setLocationRelativeTo(null);
        } else {
        }
    }

    public void panel() throws Exception {

        Conexion c = new Conexion();
        GridBagConstraints constraintsTabla = new GridBagConstraints();
        ModeloTabla = new DefaultTableModel(null, columnNames);
        TableRowSorter sorter = new TableRowSorter(ModeloTabla);
        tabla = new JTable(ModeloTabla) {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        }; //return false: Desabilitar edición de celdas.

        TableColumnModel columnModel = tabla.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(77); //ID
        columnModel.getColumn(1).setPreferredWidth(185); //Nombres
        columnModel.getColumn(2).setPreferredWidth(185);//Apellidos
        columnModel.getColumn(3).setPreferredWidth(120); //Tipo Documento
        columnModel.getColumn(4).setPreferredWidth(140); //Documento
        columnModel.getColumn(5).setPreferredWidth(120); //Fecha Nacimiento
        columnModel.getColumn(6).setPreferredWidth(85); //Edad (Años)
        columnModel.getColumn(7).setPreferredWidth(500); //Dirección
        columnModel.getColumn(8).setPreferredWidth(80); //Celular
        columnModel.getColumn(9).setPreferredWidth(80); //Telefono
        columnModel.getColumn(10).setPreferredWidth(80); //Bautizado
        columnModel.getColumn(11).setPreferredWidth(80); //Sellado
        columnModel.getColumn(12).setPreferredWidth(600); //Comentarios
        columnModel.getColumn(13).setPreferredWidth(130); //Usuario Creación
        columnModel.getColumn(14).setPreferredWidth(130); //Fecha Creación
        columnModel.getColumn(15).setPreferredWidth(130); //Usuario Actualización
        columnModel.getColumn(16).setPreferredWidth(130); //Fecha Actualización

        // tabla.setPreferredScrollableViewportSize(new Dimension(600, 100)); //Se define el tamaño
        scroll = new JScrollPane(tabla);
        constraintsTabla.ipadx = 200; //Solicita 150 de espacio extra
        constraintsTabla.fill = GridBagConstraints.BOTH;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        constraintsTabla.anchor = GridBagConstraints.FIRST_LINE_START;
        constraintsTabla.gridx = 0; // El área de texto empieza en la columna cero.
        constraintsTabla.gridy = 2; // El área de texto empieza en la fila uno
        constraintsTabla.gridwidth = 2; // El área de texto ocupa dos columnas.
        constraintsTabla.gridheight = 4; // El área de texto ocupa 3 filas.
        constraintsTabla.weighty = 2; //Se estira en Y       
        constraintsTabla.weightx = 5; //Se estira en X

        this.getContentPane().add(scroll, constraintsTabla);
        try {
            TraerDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al traer los datos de la base de datos.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR para cerrar esta ventana.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);
        }
        // tabla.setRowSorter(sorter); //Ordenar Alfabeticamente
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //add(scroll, BorderLayout.CENTER); //Solo cuando no tiene Layout definido
        scroll.addKeyListener(this);
        tabla.addKeyListener(this);
        tabla.addMouseListener(this);
    }

    public void menu() {
        MBarra = new JMenuBar();
        setJMenuBar(MBarra);

        Menu1 = new JMenu("Inicio");
        Menu2 = new JMenu("Personas");
        Menu3 = new JMenu("Cuenta");
        Menu4 = new JMenu("Ayuda");

        MBarra.add(Menu1);
        MBarra.add(Menu2);
        MBarra.add(Menu3);
        MBarra.add(Menu4);

        Item1 = new JMenuItem("Cerrar Sesión"); //Alt+F4
        Item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        Item2 = new JMenuItem("Cerrar Aplicación"); //Alt+F1
        Item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.ALT_MASK));
        //
        Item3 = new JMenuItem("Agregar Nueva Persona"); //Ctrl+N
        Item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        Item4 = new JMenuItem("Modificar Persona"); //Ctrl+M
        Item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
        Item5 = new JMenuItem("Consultar Persona"); //Ctrl+F
        Item5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        Item6 = new JMenuItem("Eliminar Persona"); //Ctrl+D
        Item6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        //
        Item7 = new JMenuItem("Ver mis datos");

        Item8 = new JMenuItem("Cambiar mi contraseña");
        //
        Item9 = new JMenuItem("Cuadro de Ayuda");
        Item9.setAccelerator(KeyStroke.getKeyStroke("F1"));
        Item10 = new JMenuItem("Acerca de la aplicación");
        Item10.setAccelerator(KeyStroke.getKeyStroke("F11"));

        Menu1.add(Item1);
        Menu1.addSeparator();
        Menu1.add(Item2);

        Menu2.add(Item3);
        Menu2.add(Item4);
        Menu2.add(Item5);
        Menu2.addSeparator();
        Menu2.add(Item6);

        Menu3.add(Item7);
        Menu3.add(Item8);

        Menu4.add(Item9);
        Menu4.addSeparator();
        Menu4.add(Item10);

        Item1.addActionListener(this);
        Item2.addActionListener(this);
        Item3.addActionListener(this);
        Item4.addActionListener(this);
        Item5.addActionListener(this);
        Item6.addActionListener(this);
        Item7.addActionListener(this);
        Item8.addActionListener(this);
        Item9.addActionListener(this);
        Item10.addActionListener(this);
    }

    public void botones() {
        GridBagConstraints constraintsActualizar = new GridBagConstraints();
        BActualizar = new JButton("Actualizar Tabla (R)");
        //constraintsActualizar.ipady = 20; //Solicita 20 de espacio extra
        constraintsActualizar.fill = GridBagConstraints.HORIZONTAL;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.

        constraintsActualizar.gridx = 0; // El área de texto empieza en la columna cero.
        constraintsActualizar.gridy = 6; // El área de texto empieza en la fila uno
        constraintsActualizar.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsActualizar.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsActualizar.weighty = 0.0; //Se estira en Y       
        constraintsActualizar.weightx = 0.5; //Se estira en X
        add(BActualizar, constraintsActualizar);
        BActualizar.addKeyListener(this);
        BActualizar.addActionListener(this);

        GridBagConstraints constraintsBExportar = new GridBagConstraints();
        BExportar = new JButton("Exportar Datos"); // 
        constraintsBExportar.fill = GridBagConstraints.HORIZONTAL;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        constraintsBExportar.gridx = 1; // El área de texto empieza en la columna cero.
        constraintsBExportar.gridy = 6; // El área de texto empieza en la fila uno
        constraintsBExportar.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsBExportar.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsBExportar.weighty = 0.0; //Se estira en Y       
        constraintsBExportar.weightx = 0.5; //Se estira en X
        add(BExportar, constraintsBExportar);
        BExportar.addActionListener(this);

        GridBagConstraints constraintsConsultar = new GridBagConstraints();
        BConsultar = new JButton("Consultar");
        //constraintsActualizar.ipady = 20; //Solicita 20 de espacio extra
        constraintsConsultar.fill = GridBagConstraints.HORIZONTAL;
        constraintsConsultar.anchor = GridBagConstraints.FIRST_LINE_START;
        constraintsConsultar.gridx = 2; // El área de texto empieza en la columna cero.
        constraintsConsultar.gridy = 5; // El área de texto empieza en la fila uno
        constraintsConsultar.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsConsultar.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsConsultar.weighty = 0.0; //Se estira en Y       
        constraintsConsultar.weightx = 0.5; //Se estira en X
        add(BConsultar, constraintsConsultar);
        BConsultar.addKeyListener(this);
        BConsultar.addActionListener(this);

        GridBagConstraints constraintsModificar = new GridBagConstraints();
        BModificar = new JButton("Modificar");
        //constraintsActualizar.ipady = 20; //Solicita 20 de espacio extra
        constraintsModificar.fill = GridBagConstraints.HORIZONTAL;
        constraintsModificar.anchor = GridBagConstraints.FIRST_LINE_START;
        constraintsModificar.gridx = 3; // El área de texto empieza en la columna cero.
        constraintsModificar.gridy = 5; // El área de texto empieza en la fila uno
        constraintsModificar.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsModificar.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsModificar.weighty = 0.0; //Se estira en Y       
        constraintsModificar.weightx = 0.5; //Se estira en X
        add(BModificar, constraintsModificar);
        BModificar.addKeyListener(this);
        BModificar.addActionListener(this);

        GridBagConstraints constraintsBorrar = new GridBagConstraints();
        BBorrar = new JButton("Eliminar");
        //constraintsActualizar.ipady = 20; //Solicita 20 de espacio extra
        constraintsBorrar.fill = GridBagConstraints.HORIZONTAL;
        constraintsBorrar.anchor = GridBagConstraints.NORTH;
        constraintsBorrar.gridx = 4; // El área de texto empieza en la columna cero.
        constraintsBorrar.gridy = 5; // El área de texto empieza en la fila uno
        constraintsBorrar.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsBorrar.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsBorrar.weighty = 0.0; //Se estira en Y       
        constraintsBorrar.weightx = 0.5; //Se estira en X
        add(BBorrar, constraintsBorrar);
        BBorrar.addKeyListener(this);
        BBorrar.addActionListener(this);

    }

    public void textos() {

        GridBagConstraints constraintsTitulo = new GridBagConstraints();

        LTitulo = new JLabel("SISTEMA DE GESTIÓN DE DATOS");
        LTitulo.setFont(LTitulo.getFont().deriveFont(15f));
        constraintsTitulo.fill = GridBagConstraints.FIRST_LINE_START;
        constraintsTitulo.gridx = 0; // El área de texto empieza en la columna.
        constraintsTitulo.gridy = 0; // El área de texto empieza en la fila
        constraintsTitulo.gridwidth = 5; // El área de texto ocupa 3 columnas.
        constraintsTitulo.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsTitulo.weightx = 1; //Fila 0. Necesita estirarse, hay que poner weighty        
        constraintsTitulo.anchor = GridBagConstraints.CENTER;
        add(LTitulo, constraintsTitulo);

        LUsuario = new JLabel("Bienvenido " + User);
        LTitulo.setFont(LTitulo.getFont().deriveFont(15f));
        constraintsTitulo.fill = GridBagConstraints.FIRST_LINE_START;
        constraintsTitulo.gridx = 0; // El área de texto empieza en la columna.
        constraintsTitulo.gridy = 0; // El área de texto empieza en la fila
        constraintsTitulo.gridwidth = 5; // El área de texto ocupa 3 columnas.
        constraintsTitulo.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsTitulo.weightx = 1; //Fila 0. Necesita estirarse, hay que poner weighty        
        constraintsTitulo.anchor = GridBagConstraints.LINE_END;
        add(LUsuario, constraintsTitulo);

        LSub = new JLabel("  ");
        constraintsTitulo.fill = GridBagConstraints.FIRST_LINE_START;
        constraintsTitulo.gridx = 1; // El área de texto empieza en la columna.
        constraintsTitulo.gridy = 1; // El área de texto empieza en la fila
        constraintsTitulo.gridwidth = 3; // El área de texto ocupa 3 columnas.
        constraintsTitulo.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsTitulo.weightx = 0.0; //Fila 0. Necesita estirarse, hay que poner weighty        
        constraintsTitulo.anchor = GridBagConstraints.CENTER;
        add(LSub, constraintsTitulo);

        GridBagConstraints constraintsL2 = new GridBagConstraints();
        L2 = new JLabel("Seleccione o digite el ID de una de las personas y luego la opción que desea ejecutar."); // 
        // constraintsL2.fill = GridBagConstraints.PAGE_END;
        constraintsL2.ipady = 40;
        constraintsL2.gridx = 2; // El área de texto empieza en la columna cero.
        constraintsL2.gridy = 3; // El área de texto empieza en la fila uno
        constraintsL2.gridwidth = 3; // El área de texto ocupa dos columnas.
        constraintsL2.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsL2.weightx = 0.0; //Fila 0. Necesita estirarse, hay que poner weighty        
        //  constraintsL2.anchor = GridBagConstraints.PAGE_END;
        add(L2, constraintsL2);

        GridBagConstraints constraintsL3 = new GridBagConstraints();
        L4 = new JLabel("   "); // 
        constraintsL3.fill = GridBagConstraints.HORIZONTAL;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        constraintsL3.ipady = 100;
        constraintsL3.gridx = 2; // El área de texto empieza en la columna cero.
        constraintsL3.gridy = 2; // El área de texto empieza en la fila uno
        constraintsL3.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsL3.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsL3.weighty = 0.0; //Se estira en Y       
        constraintsL3.weightx = 0.5; //Se estira en X
        add(L4, constraintsL3);

        //GridBagConstraints constraintsL3 = new GridBagConstraints();
        L5 = new JLabel("ID Persona:"); // 
        constraintsL3.fill = GridBagConstraints.HORIZONTAL;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        constraintsL3.gridx = 3; // El área de texto empieza en la columna cero.
        constraintsL3.ipady = 25;
        constraintsL3.gridy = 4; // El área de texto empieza en la fila uno
        constraintsL3.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsL3.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsL3.weighty = 0.0; //Se estira en Y       
        constraintsL3.weightx = 0.5; //Se estira en X
        add(L5, constraintsL3);
    }

    public void Cajas() {
        Tusuario = new JTextField("1");
        GridBagConstraints constraintsUsuario = new GridBagConstraints();
        constraintsUsuario.ipadx = 17; //Solicita 20 de espacio extra
        constraintsUsuario.anchor = GridBagConstraints.CENTER;
        constraintsUsuario.gridx = 2; // El área de texto empieza en la columna cero.
        constraintsUsuario.gridy = 4; // El área de texto empieza en la fila uno
        constraintsUsuario.gridwidth = 3; // El área de texto ocupa 1 columnas.
        constraintsUsuario.gridheight = 1; // El área de texto ocupa 1 filas.
        // constraintsUsuario.weighty = 0.0; //Se estira en Y       
        // constraintsUsuario.weightx = 0.0; //Se estira en X
        add(Tusuario, constraintsUsuario);
        this.Tusuario.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        Tusuario.addKeyListener(this);
    }

    public boolean ExportarDatos() {
        boolean Completo = false;

        File archivo;
        String ruta = "";

        JFileChooser seleccionar = null;

        LookAndFeel previousLF = UIManager.getLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            seleccionar = new JFileChooser();
            UIManager.setLookAndFeel(previousLF);
        } catch (IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException e) {
        }

        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Libro de Excel (*.xlsx)", "xlsx");
        seleccionar.setFileFilter(filtro);

        if (seleccionar.showDialog(null, "Exportar Datos") == JFileChooser.APPROVE_OPTION) {
            ruta = "" + seleccionar.getSelectedFile();

            ruta = ruta.replace(".xlsx", "");

            archivo = new File(ruta);
            if (new File(archivo + ".xlsx").exists()) {
                if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "El archivo '" + archivo.getName() + ".xlsx' ya existe.\n¿Deseas reemplazar el archivo existente?", "Confirmar Guardar Como", JOptionPane.YES_NO_OPTION)) {
                    GenerarExcel(archivo);
                }
            } else {
                GenerarExcel(archivo);
            }

        } else {
            setEnabled(true);
        }
        setEnabled(true);
        return Completo;
    }

    private void GenerarExcel(File archivo) {
        JOptionPane.showMessageDialog(null, "Se generará un archivo Excel con todos los datos mostrados en la tabla.\nEste proceso puede tardar dependiendo la cantidad de datos a exportar.\n\nAl pulsar Aceptar espere hasta que el archivo se abra automáticamente.", "¡Atención! - Información sobre la Exportación", JOptionPane.INFORMATION_MESSAGE);
        setEnabled(false);
        int cantFila = tabla.getRowCount();
        int cantColumna = tabla.getColumnCount();
        Workbook wb;
        wb = new XSSFWorkbook();

        Sheet hoja = wb.createSheet("Jovenes Olarte");

        CellStyle my_style = wb.createCellStyle();
        CellStyle my_styleAll = wb.createCellStyle();
        /* Create HSSFFont object from the workbook */
        Font my_font = wb.createFont();
        /* set the weight of the font */
        my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        /* attach the font to the style created earlier */
        my_style.setFont(my_font);
        my_style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        my_style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        my_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        my_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        my_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        my_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        
        my_styleAll.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        my_styleAll.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        my_styleAll.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        my_styleAll.setBorderTop(HSSFCellStyle.BORDER_THIN);
        my_styleAll.setBorderRight(HSSFCellStyle.BORDER_THIN);
        my_styleAll.setBorderLeft(HSSFCellStyle.BORDER_THIN);

        try {
            for (int i = -1; i < cantFila; i++) {
                Row fila = hoja.createRow(i + 1);
                for (int j = 0; j < cantColumna; j++) {
                    Cell celda = fila.createCell(j);
                    if (i == -1) {
                        celda.setCellValue(String.valueOf(tabla.getColumnName(j)));
                        celda.setCellStyle(my_style);
                    } else {
                        celda.setCellValue(String.valueOf(tabla.getValueAt(i, j)));
                        celda.setCellStyle(my_styleAll);
                    }

                    for (int x = 0; x < cantColumna; x++) {
                        hoja.autoSizeColumn((short) x);
                    }

                    hoja.setAutoFilter(CellRangeAddress.valueOf("A1:Q2"));

                    wb.write(new FileOutputStream(archivo + ".xlsx"));
                }
            }

            AbrirExcelExterno(archivo + ".xlsx");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al exportar los datos en el archivo de Excel.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error: \n" + ex + "\n\nPulse ACEPTAR para cerrar esta ventana.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void AbrirExcelExterno(String Ruta) {
        try {
            File path = new File(Ruta);
            Desktop.getDesktop().open(path);
            JOptionPane.showMessageDialog(null, "El archivo se ha guardado en la siguiente ruta:\n" + Ruta + "", "¡Exito! - Datos Exportados Correctamente", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar el archivo y por lo tanto no se abrirá", "¡Error! - Form1.java", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == Item1) {
            control();
        }
        if (ae.getSource() == Item2) {
            System.exit(0);
        }
        //-----
        if (ae.getSource() == Item3) {
            NewUser AP = new NewUser(this, true, ID_ADM);
        }
        if (ae.getSource() == Item4) {
            Modify AP = new Modify(this, true, 0, ID_ADM);
        }
        if (ae.getSource() == Item5) {
            Find AP = new Find(this, true, 0, ID_ADM);
        }
        if (ae.getSource() == Item6) {
            Delete AP = new Delete(this, true, 0);
        }
        //-----
        if (ae.getSource() == Item7) {
            //NewUser aplicacion = new NewUser(this, true);
        }
        if (ae.getSource() == Item8) {
            ChangePSS AP = new ChangePSS(this, true, ID_ADM);
        }
        //-----
        if (ae.getSource() == Item9) {
            JOptionPane.showMessageDialog(null, "• En la tabla principal se listan todas las personas que se encuentran registradas en el sistema.\n\n• En la opción Personas podrá: Agregar Nuevas Personas, Modificar, Consultar o Eliminar Personas ya registradas en el sistema.\n\n• En la opción Cuenta podrá: Ver sus datos registrados y Cambiar su Contraseña de Acceso al sistema.\n\n• El botón Exportar Datos genera un archivo Excel con todos los datos existentes en el sistema, solo debes pulsar en él,\nseleccionar el lugar donde quieres que se guarde y un nombre para el archivo, el sistema hará todo el resto del trabajo.\nToda la ventana se bloquea mientras el archivo es generado.\nAl finalizar la exportación se abrirá automáticamente el archivo generado y se reactivará la ventana.\n\n• El sistema cuenta con atajos de teclado* los cuales se listan a continuación:\n*Nota: Para ejecutar cualquiera de estos comandos se deben pulsar conjuntamente las teclas que se mencionen.\n\n- Alt + F4: Cierra la sesión actual y lo lleva a la página de inicio.\n- Alt + F1: Cierra la sesión y cierra el programa inmediatamente (No solicita confirmación).\n- R: Actualiza los datos en la tabla principal.\n- Ctrl + N: Abre la ventana para agregar una nueva persona.\n- Ctrl + M: Abre la ventana para modificar una persona registrada en el sistema.\n- Ctrl + F: Abre la ventana de consulta de una persona registrada en el sistema.\n- Ctrl + D: Abre la ventana para eliminar una de las personas registradas en el sistema.\n- Teclas Arriba y Abajo: Permite desplazarse entre las personas listadas en la tabla principal.\n\nAl pulsar ACEPTAR esta ventana se cerrará.", "Ayuda Inicio - ARTURO 1.0.5", JOptionPane.INFORMATION_MESSAGE);
        }
        if (ae.getSource() == Item10) {
            JOptionPane.showMessageDialog(null, "ARTURO 1.0.5 es una aplicación de gestión de base de datos de personas, ARTURO maneja un sistema de encriptamiento\ncompuesto el cual le permite ser el único capaz de manipular los datos registrados en sus bases de datos.\n\nDesarrollado y distribuido por Fabian Dario Montoya, la distribución, copia o uso no autorizado de este aplicativo puede \nconllevar procesos legales en contra de quien incumpla.\n\n• Aviso de Copyright:\nCopyright © 2016-2018 Fabian_Montoya Developing Creations, Bogotá D.C, Colombia\nTodos los derechos reservados.\nTodos los textos, imágenes, gráficos, pistas de sonidos, datos de vídeo y animación, así como su composición o diseño están protegidos \npor derechos de autor y otras leyes de protección intelectual. Su contenido no puede copiarse o distribuirse para fines comerciales u otras, \nno puede mostrarse, incluso en una versión modificada, sin la previa autorización del respectivo desarrollador.\n\nAl pulsar ACEPTAR esta ventana se cerrará.", "Acerca de - ARTURO 1.0.5", JOptionPane.INFORMATION_MESSAGE);
        }

        if (ae.getSource() == BActualizar) {
            try {
                TraerDatos();
                Tusuario.setEditable(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error al traer los datos de la base de datos.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR para cerrar esta ventana.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (ae.getSource() == BExportar) {
            try {
                TraerDatos();
                Tusuario.setEditable(true);
                ExportarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error al traer los datos de la base de datos.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR para cerrar esta ventana.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (ae.getSource() == BModificar) {
            ID = Integer.parseInt(Tusuario.getText());
            Modify AP = new Modify(this, true, ID, ID_ADM);
        }

        if (ae.getSource() == BConsultar) {
            ID = Integer.parseInt(Tusuario.getText());
            Find AP = new Find(this, true, ID, ID_ADM);
        }

        if (ae.getSource() == BBorrar) {
            ID = Integer.parseInt(Tusuario.getText());
            BorrarCliente(ID);
        }

    }

    @Override
    public void keyTyped(KeyEvent ke) {
        char c = ke.getKeyChar();
        int LimiteID = 5;
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
            if (Tusuario.getText().length() == LimiteID) {
                ke.consume();
            }

            if (Tusuario.getText().length() == 0) {
                BConsultar.setEnabled(false);
                BModificar.setEnabled(false);
                BBorrar.setEnabled(false);
            }

        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();

        if (key == KeyEvent.VK_ALT) {
            // JOptionPane.showMessageDialog(null, "Pulsó Alt", "Alt + F1 Press", JOptionPane.INFORMATION_MESSAGE);
            pulso = pulso + 1;
        }

        if (key == KeyEvent.VK_F1) {
            // JOptionPane.showMessageDialog(null, "Pulsó F1", "Alt + F1 Press", JOptionPane.INFORMATION_MESSAGE);
            pulso = pulso + 1;
        }

        if (key == KeyEvent.VK_R) {
            try {
                TraerDatos();
                Tusuario.setEditable(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error al traer los datos de la base de datos.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR para cerrar esta ventana.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (pulso == 2) {
            System.exit(0);
        }
        pulso = 0;

        if (Tusuario.getText().length() >= 1) {
            BConsultar.setEnabled(true);
            BModificar.setEnabled(true);
            BBorrar.setEnabled(true);
        }

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_UP) {
            Tusuario.setEditable(false);
            int Fila = tabla.getSelectedRow();
            Tusuario.setText(String.valueOf(tabla.getValueAt(Fila, 0)));
        }

    }

    public static void main(String args[]) {

    }

    public void TraerDatos() throws Exception {
        String Data[] = new String[500];
        int x;

        String sql = "SELECT ID_PER, Nombres_PER, "
                + " Apellidos_PER, TypeDocument_PER, "
                + " Documento_PER, FecNacimiento_PER, "
                + " Direccion_PER, "
                + " Celular_PER, Telefono_PER, "
                + " Bautizado_PER, Sellado_PER, "
                + " Comentarios, Usuario_Creacion, "
                + " Fecha_Creacion, Usuario_Actualizacion, "
                + " Fecha_Actualizacion FROM PERSONAS ORDER BY Nombres_PER ASC;";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(sql);
            if (tabla.getRowCount() >= 1) {
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    ModeloTabla.removeRow(i);
                    i -= 1;
                }
            }
            while (resultado.next()) {
                Data[0] = resultado.getString(1);
                Data[1] = resultado.getString(2);
                for (x = 2; x <= 5; x++) {
                    Data[x] = Utilidades.Desencriptar(resultado.getString(x + 1));
                }
                String Fecha[] = Data[5].split("/");
                Data[6] = "" + Utilidades.CalcularEdad(Integer.parseInt(Fecha[0]), Integer.parseInt(Fecha[1]), Integer.parseInt(Fecha[2]));
                for (x = 7; x < 17; x++) {
                    Data[x] = Utilidades.Desencriptar(resultado.getString(x));
                }
                ModeloTabla.addRow(Data);
            }
            conexion.close();
            Tusuario.setText("1");
            Tusuario.requestFocus();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al consultar la Base de Datos y no se motrarán datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Form1 - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Form1 - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void BorrarCliente(int ID) {
        String Nombre = "", Apellido = "";
        try {
            int Fila = tabla.getSelectedRow();

            Nombre = (String.valueOf(tabla.getValueAt(Fila, 1)));
            Apellido = (String.valueOf(tabla.getValueAt(Fila, 2)));
            int ax = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar la persona " + Nombre + " " + Apellido + " con ID " + ID + " permanentemente?", "Eliminar Persona - Confirmar", JOptionPane.YES_NO_OPTION);
            if (ax == JOptionPane.YES_OPTION) {
                Borrar(ID);
            } else {
                //No se hace nada
            }
        } catch (Exception ex) {
            try {

                int val = ID, X = 0;
                int columna = 0;

                for (int fila = 0; fila < tabla.getRowCount(); fila++) {

                    Integer num = Integer.parseInt((String) tabla.getValueAt(fila, columna));

                    if (val == num) {
                        X = num;
                        Nombre = (String.valueOf(tabla.getValueAt(fila, 1)));
                        Apellido = (String.valueOf(tabla.getValueAt(fila, 2)));
                        //tabla.changeSelection(fila, 0, true, true);
                        int ax = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar a la persona " + Nombre + " " + Apellido + " con ID " + ID + " permanentemente?", "Eliminar Persona - Confirmar", JOptionPane.YES_NO_OPTION);
                        if (ax == JOptionPane.YES_OPTION) {
                            Borrar(ID);
                        } else {
                            //No se hace nada
                        }
                    }

                }

                if (X != ID) {
                    Delete AP = new Delete(this, true, ID);
                }

            } catch (Exception e) {
                //
            }

        }
    }

    private void Borrar(int ID) {
        String sql = "DELETE FROM PERSONAS WHERE ID_PER = " + ID + ";";
        try {
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/JovenesOlarteBD", "administrador", "ADMUsuarios");
            sentencia = conexion.createStatement();

            sentencia.execute(sql);
            conexion.close();

            JOptionPane.showMessageDialog(null, "Se ha borrado la persona correctamente.", "Confirmación - Eliminación correcta", JOptionPane.INFORMATION_MESSAGE);
            try {
                TraerDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Se ha producido un error al traer los datos de la base de datos.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Form1.java", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR para cerrar esta ventana.", "¡ERROR! - Form1.java", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al Borrar la persona de la Base de Datos.\n\nPor favor consulte con su desarrollador.\nPulse ACEPTAR para ver el error.", "Form1 - ¡ERROR!", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "", "Form1.java - ¡ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        int Fila = tabla.getSelectedRow();
        Tusuario.setText(String.valueOf(tabla.getValueAt(Fila, 0)));
        Tusuario.setEditable(false);
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
