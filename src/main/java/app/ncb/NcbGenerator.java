package app.ncb;

import config.ConfigProperty;
import net.sf.jasperreports.engine.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NcbGenerator {

    //make it factory class
    private static NcbGenerator instance = null;

    public static NcbGenerator getInstance() {
        if (instance == null) {
            instance = new NcbGenerator();
        }
        return instance;
    }

    public byte[] generateNcbPdf(Ncb ncb) throws JRException, IOException {


        byte[] bytes = null;

        JasperReport report = null;
        Map<String, Object> parameters = null;
        JRDataSource dataSource = null;
        JasperPrint jasperPrint = null;

        report = JasperCompileManager.compileReport(ConfigProperty.getInstance().getTemplateNcb());
        parameters = new HashMap<String, Object>();
        parameters.put("date", ncb.getDate());
        parameters.put("month", ncb.getMonth());
        parameters.put("year", ncb.getYear());
        parameters.put("name", ncb.getName());


        System.out.println(ncb.getDate() + ncb.getName());
        parameters.put("ImageUrl", ConfigProperty.getInstance().getHeader());

        dataSource = new JREmptyDataSource();
        jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);

        bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return bytes;

    }
}
