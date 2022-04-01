package Tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath = "configs//Configuration.properties";


    public ConfigFileReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getApplicationUrl() {
        String url = properties.getProperty("url");
        if (url != null) return url.toString();
        else throw new RuntimeException("url not specified in the Configuration.properties file.");
    }
    public String getPassword() {
        String password = properties.getProperty("password");
        if (password != null) return password.toString();
        else throw new RuntimeException("Password not specified in the Configuration.properties file.");
    }
    public String getUsername() {
        String username = properties.getProperty("username");
        if (username != null) return username.toString();
        else throw new RuntimeException("Username not specified in the Configuration.properties file.");
    }
}