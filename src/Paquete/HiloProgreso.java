package Paquete;

import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Fabian_Montoya
 */
public class HiloProgreso extends Thread {

    JProgressBar Barra;

    Form1 ap;
    Splash Sp;

    public static String User;

    public HiloProgreso(JProgressBar progreso1, String Usuario) {
        super();
        User = Usuario;
        Barra = progreso1;
    }

    @Override
    public void run() {
        int a = 30;
        try {
            for (int i = 1; i <= 100; i++) {
                Barra.setValue(i);
                pausa(a);
                if (i == 20) {
                 a = 30;
                 }

                 if (i==50){
                 a = 23;
                 }
                 
                 if (i==70){
                 a = 15;
                 }
               
            }
            
        } catch (Exception e) {

        }
    }

    public void pausa(int mlSeg) {
        try {
            // pausa para el splash
            Thread.sleep(mlSeg);
        } catch (Exception e) {
        }
    }

}
