package Paquete;

/**
 *
 * @author Fabian_Montoya Tomado de *
 * http://www.qualityinfosolutions.com/metodos-para-encriptar-y-desencriptar-en-java/
 */
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.commons.codec.binary.Base64;

public class Utilidades {

    private static Calendar fecha = new GregorianCalendar();

    public static String Encriptar(String texto) {

        String secretKey = "LeidyJimenez"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainTextBytes = texto.getBytes("utf-8");
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.encodeBase64(buf);
            base64EncryptedString = new String(base64Bytes);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al encriptar los datos\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Encriptación", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "" + ex, "¡ERROR! - Encriptación", JOptionPane.ERROR_MESSAGE);
        }
        return base64EncryptedString;
    }

    public static String Desencriptar(String textoEncriptado) throws Exception {

        String secretKey = "LeidyJimenez"; //llave para encriptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Se ha producido un error al desencriptar los datos\n\nPor favor consulte con su desarrollador.\n\n Pulse ACEPTAR para ver el error.", "¡ERROR! - Desencriptación", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(null, "" + ex, "¡ERROR! - Desencriptación", JOptionPane.ERROR_MESSAGE);
        }
        return base64EncryptedString;
    }

    /*Calcula la edad exacta en años dependiendo del día mes y año que se envien*/
    public static int CalcularEdad(int dia, int mes, int año) {
        int Edad = 0;

        int añoactual = fecha.get(Calendar.YEAR);
        int mesactual = fecha.get(Calendar.MONTH) + 1;
        int diaactual = fecha.get(Calendar.DAY_OF_MONTH);

        Edad = añoactual - año;

        if (mesactual < mes) {
            Edad = Edad - 1;
        } else if (mesactual == mes) {
            if (diaactual < dia) {
                Edad = Edad - 1;
            }
        }

        return Edad;
    }
    
    /*Crea el formato para que la fecha quede DD/MM/AAAA sin importan si llenan o no el campo*/
    public static String FormatoFechaNacimiento(String TXTDia, String TXTMes, String TXTAño) {
        String Fecha = "", Dia ="", Mes="", Año="";

        if (TXTDia.equals("") || TXTMes.equals("") || TXTAño.equals("")) {
            if (TXTDia.equals("")){
                Dia ="00";
            }else{
                Dia = TXTDia;
            }
            if (TXTMes.equals("")){
                Mes = "00";
            }else{
                Mes = TXTMes;
            }
            if (TXTAño.equals("")){
                Año = "0000";
            }else{
                Año = TXTAño;
            }
            Fecha = Dia+"/"+Mes+"/"+Año;
        } else {
            Fecha = TXTDia + "/" + TXTMes + "/" + TXTAño;
        }

        return Fecha;
    }
}
