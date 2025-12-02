import org.apache.commons.net.ftp.FTPClient;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class CGWallpaper2024 {

    private static final String FTP_SERVER = "10.0.0.1";
    private static final String FTP_USER = "cgfondo";
    private static final String FTP_PASSWORD = "cg123";
    private static final String IMAGE_PREFIX = "fondo";
    private static final String IMAGE_EXTENSION = ".jpeg";
    private static final int IMAGE_COUNT = 10;

    // Interfaz para llamadas a la API de Windows
    public interface User32 extends Library {
        User32 INSTANCE = Native.load("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo(int uiAction, int uiParam, String pvParam, int fWinIni);
    }

    private static final int SPI_SETDESKWALLPAPER = 0x0014;
    private static final int SPIF_UPDATEINIFILE = 0x01 | 0x02;

    public static void main(String[] args) {
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(FTP_SERVER);
            ftpClient.login(FTP_USER, FTP_PASSWORD);

            if (ftpClient.isConnected()) {
                System.out.println("Conectado al servidor FTP.");

                // Selección aleatoria de la imagen
                Random random = new Random();
                int randomIndex = random.nextInt(IMAGE_COUNT) + 1;
                String imageName = IMAGE_PREFIX + randomIndex + IMAGE_EXTENSION;
                String imageURL = "ftp://" + FTP_USER + ":" + FTP_PASSWORD + "@" + FTP_SERVER + "/" + imageName;

                System.out.println("Estableciendo fondo: " + imageName);
                establecerFondoDePantalla(imageURL);

                ftpClient.logout();
                ftpClient.disconnect();
            } else {
                System.err.println("No se pudo conectar al servidor FTP.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void establecerFondoDePantalla(String imageURL) {
        try {
            URL url = new URL(imageURL);
            InputStream inputStream = url.openStream();
            BufferedImage image = ImageIO.read(inputStream);

            // Guardar la imagen en el disco como BMP (requisito de Windows)
            String tempPath = System.getProperty("java.io.tmpdir") + "wallpaper.bmp";
            ImageIO.write(image, "bmp", new java.io.File(tempPath));

            // Establecer el fondo de pantalla usando la API de Windows
            boolean result = User32.INSTANCE.SystemParametersInfo(SPI_SETDESKWALLPAPER, 0, tempPath, SPIF_UPDATEINIFILE);
            if (result) {
                System.out.println("Fondo de pantalla cambiado correctamente.");
            } else {
                System.err.println("Error al cambiar el fondo de pantalla.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
