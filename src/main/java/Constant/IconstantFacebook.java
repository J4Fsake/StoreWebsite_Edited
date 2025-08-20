package Constant;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class IconstantFacebook {
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

        public static final String FACEBOOK_CLIENT_ID = props.getProperty("FACEBOOK_CLIENT_ID");
        public static final String FACEBOOK_CLIENT_SECRET = props.getProperty("FACEBOOK_CLIENT_SECRET");
        public static final String FACEBOOK_REDIRECT_URI = props.getProperty("FACEBOOK_REDIRECT_URI");
        public static final String FACEBOOK_LINK_GET_TOKEN = props.getProperty("FACEBOOK_LINK_GET_TOKEN");
        public static final String FACEBOOK_LINK_GET_USER_INFO = props.getProperty("FACEBOOK_LINK_GET_USER_INFO");
}


