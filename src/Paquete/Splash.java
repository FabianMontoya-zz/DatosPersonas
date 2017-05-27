package Paquete;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author Fabian_Montoya
 */
public class Splash extends JFrame implements ChangeListener {

    JLabel LImagen, LImagen2, LTexto;
    JProgressBar Barra;

    Form1 ap;

    boolean aux = false;
    boolean exitoso = false;

    public static String User;
    public static long IDAdm;

    GridBagLayout GRID = new GridBagLayout();

    public Splash() {
        super("ARTURO se está preparando...");

        try {
            Image icon = new ImageIcon(getClass().getResource("/Imagenes/Database.png")).getImage();
            setIconImage(icon);
            setLayout(GRID);
            setUndecorated(true); //Eliminar toda la ventana en la que es contenida
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            setSize(450, 220);
            setResizable(false);
            setLocationRelativeTo(null);
            Imagen();
            Barra();

            HiloProgreso hilo = new HiloProgreso(Barra, User);
            //Iniciamos el Hilo
            hilo.start();
            hilo = null;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al iniciar la aplicación.\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Splash.java", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "Error:  " + ex + "\n\nPulse ACEPTAR, la aplicación se cerrará.", "¡ERROR! - Splash.java", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    public void Imagen() {

        LImagen = new JLabel();
        LTexto = new JLabel("Comprobando Seguridad...");
        LImagen2 = new JLabel();
        ImageIcon fot = new ImageIcon(getClass().getResource("/Imagenes/Gif-Cargando.gif"));
        ImageIcon fot2 = new ImageIcon(getClass().getResource("/Imagenes/712.gif"));
        //Icon icono = new ImageIcon(fot.getImage().getScaledInstance(LImagen.getWidth(), LImagen.getHeight(), Image.SCALE_DEFAULT));
        LImagen2.setIcon(fot2);
        LImagen.setIcon(fot);
        fot.getImage().flush();
        LImagen.setIcon(fot);

        GridBagConstraints constraintsImagen = new GridBagConstraints();

        //   constraintsImagen.fill = GridBagConstraints.BOTH;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        //constraintsImagen.anchor = GridBagConstraints.FIRST_LINE_START;
        constraintsImagen.gridx = 0; // El área de texto empieza en la columna cero.
        constraintsImagen.gridy = 0; // El área de texto empieza en la fila uno
        constraintsImagen.gridwidth = 1; // El área de texto ocupa dos columnas.
        constraintsImagen.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsImagen.weighty = 2; //Se estira en Y       
        constraintsImagen.weightx = 2; //Se estira en X
        add(LImagen, constraintsImagen);
        add(LImagen2, constraintsImagen);

        GridBagConstraints constraintsTexto = new GridBagConstraints();

        constraintsTexto.gridx = 0; // El área de texto empieza en la columna cero.
        constraintsTexto.gridy = 1; // El área de texto empieza en la fila uno
        constraintsTexto.gridwidth = 1; // El área de texto ocupa dos columnas.
        constraintsTexto.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsTexto.weighty = 0.0; //Se estira en Y       
        constraintsTexto.weightx = 1; //Se estira en X
        LTexto.setHorizontalAlignment(JLabel.RIGHT);
        add(LTexto, constraintsTexto);
    }

    public void Barra() {
        Barra = new JProgressBar();
        Barra.setValue(0);
        Barra.setStringPainted(true);

        GridBagConstraints constraintsBarra = new GridBagConstraints();

        constraintsBarra.fill = GridBagConstraints.HORIZONTAL;// El area de texto debe estirarse en ambos sentidos. Ponemos el campo fill.
        constraintsBarra.anchor = GridBagConstraints.SOUTH;
        constraintsBarra.gridx = 0; // El área de texto empieza en la columna cero.
        constraintsBarra.gridy = 2; // El área de texto empieza en la fila uno
        constraintsBarra.gridwidth = 1; // El área de texto ocupa 1 columnas.
        constraintsBarra.gridheight = 1; // El área de texto ocupa 1 filas.
        constraintsBarra.weighty = 0.0; //Se estira en Y       
        constraintsBarra.weightx = 1; //Se estira en X
        add(Barra, constraintsBarra);
        Barra.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {

        if (Barra.getValue() == 15) {
            LTexto.setText("Desencriptando Datos...");
        }

        if (Barra.getValue() == 46) {
            try {
                LTexto.setText("Comprobando Datos...");
                ap = new Form1(User, IDAdm);//ABRE OTRO JFRAME
                exitoso = true;
            } catch (Exception ex) {

            }
            //ap.setVisible(true);
        }

        if (Barra.getValue() == 80) {
            LTexto.setText("Iniciando...");
        }

        if (Barra.getValue() == 100) {
            dispose();//CIERRA EL SPLASH
            if (exitoso == true) {
                ap.setVisible(true);
                JOptionPane.showMessageDialog(null, "Bienvenido de vuelta " + User + ".", "Ingreso - Bienvenido (" + User + ")", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }
}
