package app.notice;

import config.ConfigProperty;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NoticeGen {


    private String[] monthList = {"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายร", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};
    //make it factory class
    private static NoticeGen instance = null;


    public static NoticeGen getInstance() {
        if (instance == null) {
            instance = new NoticeGen();
        }
        return instance;
    }

    public byte[] generateMultiplePage(List<Notice> noticeResponse) throws JRException, IOException {
        //init
        byte[] bytes = null;

        List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
        List<ReportThread> threadList = new ArrayList<>();

        int threadPoolSize = 3;
        int size = noticeResponse.size();
        double divided = Math.ceil(size / threadPoolSize);

        int count = 0;
        for(int i = 0; i<threadPoolSize ;i++){
            if(count+divided<=size){
                List<Notice> notices = new ArrayList();
                ReportThread thread = new ReportThread();
                for(int j = 0; j<divided; j++){
                    notices.add(noticeResponse.get(count));
                    count++;
                }
                thread.setNoticeResponse(notices);
                thread.setJasperPrintList(jasperPrints);
                threadList.add(thread);
                System.out.println("add to thread list");
            }
            else {
                List<Notice> notices = new ArrayList();
                ReportThread thread = new ReportThread();
                for(i=0; i<size-count;i++){
                    notices.add(noticeResponse.get(count));
                    count++;
                }
                thread.setNoticeResponse(notices);
                thread.setJasperPrintList(jasperPrints);
                threadList.add(thread);
                System.out.println("add to thread list");
            }
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        for(ReportThread thread : threadList){
            executorService.submit(thread);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        //export as byte array
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        configuration.setMetadataTitle("ITTP");
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        bytes = out.toByteArray();

        return bytes;
    }

    public byte[] generateSingleNoticePdf(Notice notice) throws JRException, IOException {
        JasperPrint jasperPrint = null;
        JRDataSource dataSource = new JREmptyDataSource();
        JasperReport report = null;
        Map<String, Object> parameters = null;

        byte[] bytes = null;
        String noticeUrl = getReportType(notice);
        report = JasperCompileManager.compileReport(noticeUrl);
        parameters = injectParameter(notice);
        jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
        bytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return bytes;

    }

    private HashMap<String, Object> injectParameter(Notice notice) throws IOException {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("imageUrl", ConfigProperty.getInstance().getHeader());
        parameters.put("reportId", notice.getReportId());
        String address1 = String.format("%s %s %s %s", notice.getBuilding(), notice.getHouseId(), notice.getSoi(), notice.getRoad());
        String address2 = String.format("%s %s %s %s", notice.getTambol(), notice.getAmphoe(), notice.getDistrict(), notice.getPostId());
        parameters.put("address1", address1);
        parameters.put("address2", address2);
        parameters.put("dueDate", notice.getDueDate().toString());
        parameters.put("dueMonth", monthList[notice.getDueMonth() - 1]);
        parameters.put("dueYear", notice.getDueYear().toString());
        parameters.put("fromDate", notice.getFromDate().toString());
        parameters.put("fromMonth", monthList[notice.getFromMonth() - 1]);
        parameters.put("fromYear", notice.getFromYear().toString());
        parameters.put("reportDate", notice.getReportDate().toString());
        parameters.put("reportMonth", monthList[notice.getReportMonth() - 1]);
        parameters.put("reportYear", notice.getReportYear().toString());
        parameters.put("minPayment", notice.getMinPayment());
        parameters.put("followingCost", notice.getFollowingCost());
        parameters.put("totalPayment", notice.getTotalPayment());
        parameters.put("remainingPayment", notice.getRemainingPayment());
        parameters.put("indeptDay", notice.getIndeptDay());
        parameters.put("loanDate", notice.getLoanDate().toString());
        parameters.put("loanMonth", notice.getLoanMonth().toString());
        parameters.put("loanYear", notice.getLoanYear().toString());
        parameters.put("totalLoan", notice.getTotalLoan());
        return parameters;
    }

    private String getReportType(Notice notice) throws IOException {
        String noticeUrl = "";
        switch (notice.getNoticeType()) {
            case "n1": {
                noticeUrl = ConfigProperty.getInstance().getTemplateN1();
                break;
            }
            case "n2": {
                noticeUrl = ConfigProperty.getInstance().getTemplateN2();
                break;
            }
            case "n3": {
                noticeUrl = ConfigProperty.getInstance().getTemplateN3();
                break;
            }
            case "n4": {
                noticeUrl = ConfigProperty.getInstance().getTemplateN4();
                break;
            }
            case "n5": {
                noticeUrl = ConfigProperty.getInstance().getTemplateN5();
                break;
            }
        }
        return noticeUrl;
    }

    private class ReportThread extends Thread {

        List<JasperPrint> jasperPrintList;
        List<Notice> noticeResponse;

        public List<JasperPrint> getJasperPrintList() {
            return jasperPrintList;
        }

        public void setJasperPrintList(List<JasperPrint> jasperPrintList) {
            this.jasperPrintList = jasperPrintList;
        }

        public List<Notice> getNoticeResponse() {
            return noticeResponse;
        }

        public void setNoticeResponse(List<Notice> noticeResponse) {
            this.noticeResponse = noticeResponse;
        }

        public void run() {
            try {
                //generate jasper print from array of response
                System.out.println("Thread Created");
                for (Notice notice : noticeResponse) {
                    JRDataSource dataSource = new JREmptyDataSource();
                    String noticeUrl = getReportType(notice);
                    JasperReport report = JasperCompileManager.compileReport(noticeUrl);
                    Map<String, Object> parameters = injectParameter(notice);
                    JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
                    jasperPrintList.add(print);
                    System.out.println(jasperPrintList.size());
                }
            } catch (Exception e) {
                System.out.println("Exception is caught");
            }
        }
    }

}
