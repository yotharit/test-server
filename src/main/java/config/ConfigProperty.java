package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperty {

    private static ConfigProperty instance = null;

    private ConfigProperty() throws IOException {
        readProp();
    }

    public static ConfigProperty getInstance() throws IOException {
        if (instance == null) {
            instance = new ConfigProperty();
        }
        return instance;
    }

    private String header;
    private String templateNcb;
    private InputStream inputStream;


    public String getHeader() {
        return header;
    }

    private void setHeader(String header) {
        this.header = header;
    }

    public String getTemplateNcb() {
        return templateNcb;
    }

    private void setTemplateNcb(String templateNcb) {
        this.templateNcb = templateNcb;
    }

    public void readProp() throws IOException {
        try {
            Properties prop = new Properties();
            String prop_names = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(prop_names);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file not found");
            }

            header = prop.getProperty("header");
            templateNcb = prop.getProperty("templateNcb");


        } catch (Exception e) {
            System.out.println("Exception : " + e.toString());
        } finally {
            inputStream.close();
        }
    }

}
