package Engine.Utils;

import java.io.File;

public class EngineUtils {

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return (fileName.substring(fileName.lastIndexOf(".")+1)).toLowerCase();
        else return "";
    }
}
