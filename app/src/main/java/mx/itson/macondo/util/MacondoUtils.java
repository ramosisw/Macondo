package mx.itson.macondo.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ramos.isw@gmail.com
 *         Created by Julio C. Ramos on 04/05/2015.
 */
public class MacondoUtils {
    public static final int REQUEST_TOMAR_FOTO = 1;

    /**
     *
     * @return Object de la ubicacion temporal del archivo
     */
    public static File generateNewFilePath() {
        File folder = new File("/storage/sdcard0/mx.itson.macondo");
        if (folder.exists() || folder.mkdir()) {
            String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return new File(String.format("%s/%s.jpg", folder.getAbsolutePath(), date));
        }
        return null;
    }

    /**
     *
     * @param file nombre del archivo
     * @return Object de la ubicacion del archivo
     */
    public static File getThumbPath(String file) {
        File folder = new File("/storage/sdcard0/mx.itson.macondo/thumb/");
        if (folder.exists() || folder.mkdir()) {
            return new File("/storage/sdcard0/mx.itson.macondo/thumb/" + file);
        }else {
            return null;
        }
    }
}
