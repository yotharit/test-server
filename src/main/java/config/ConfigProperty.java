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
    private String templateN1;
    private String templateN2;
    private String templateN3;
    private String templateN4;
    private String templateN5;
    private int threadPoolSize;

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public String getTemplateN1() {
        return templateN1;
    }

    public void setTemplateN1(String templateN1) {
        this.templateN1 = templateN1;
    }

    public String getTemplateN2() {
        return templateN2;
    }

    public void setTemplateN2(String templateN2) {
        this.templateN2 = templateN2;
    }

    public String getTemplateN3() {
        return templateN3;
    }

    public void setTemplateN3(String templateN3) {
        this.templateN3 = templateN3;
    }

    public String getTemplateN4() {
        return templateN4;
    }

    public void setTemplateN4(String templateN4) {
        this.templateN4 = templateN4;
    }

    public String getTemplateN5() {
        return templateN5;
    }

    public void setTemplateN5(String templateN5) {
        this.templateN5 = templateN5;
    }

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
            templateN1 = prop.getProperty("templateN1");
            templateN2 = prop.getProperty("templateN2");
            templateN3 = prop.getProperty("templateN3");
            templateN4 = prop.getProperty("templateN4");
            templateN5 = prop.getProperty("templateN5");
            threadPoolSize = Integer.parseInt(prop.getProperty("threadPoolSize"));


        } catch (Exception e) {
            System.out.println("Exception : " + e.toString());
        } finally {
            inputStream.close();
        }
    }

}
