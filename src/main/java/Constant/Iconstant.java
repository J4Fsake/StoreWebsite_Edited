package Constant;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Iconstant {
    private static Properties props = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream(".env");
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String GOOGLE_CLIENT_ID = props.getProperty("GOOGLE_CLIENT_ID");
    public static final String GOOGLE_CLIENT_SECRET = props.getProperty("GOOGLE_CLIENT_SECRET");
    public static final String GOOGLE_REDIRECT_URI = props.getProperty("GOOGLE_REDIRECT_URI");
    public static final String GOOGLE_GRANT_TYPE = props.getProperty("GOOGLE_GRANT_TYPE");
    public static final String GOOGLE_LINK_GET_TOKEN = props.getProperty("GOOGLE_LINK_GET_TOKEN");
    public static final String GOOGLE_LINK_GET_USER_INFO = props.getProperty("GOOGLE_LINK_GET_USER_INFO");

    public static final String CSV_PATH = props.getProperty("CSV_PATH");
    public static final String UPLOAD_DIRECTORY = props.getProperty("UPLOAD_DIRECTORY");
    public static final String FILE_TEMP_PATH = props.getProperty("FILE_TEMP_PATH");
}
