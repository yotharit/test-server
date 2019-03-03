package app.notice;

import app.mail.EmailServiceImpl;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class NoticeController {

//    @RequestMapping(value = "/notice/single/pdf",method = RequestMethod.POST)
//    public ResponseEntity<byte[]> generatePdf(@RequestBody Notice notice) throws JRException, IOException {
//        NoticeGen generator = NoticeGen.getInstance().getInstance();
//        byte[] out = null;
//        out = generator.generateSingleNoticePdf(notice);
//        return ResponseEntity
//                .ok()
//                // Specify content type as PDF
//                .header("Content-Type", "application/pdf; charset=UTF-8")
//                // Tell browser to display PDF if it can
//                .header("Content-Disposition", "inline; filename=\"" + notice.getNoticeType() + ".pdf\"")
//                .body(out);
//    }

    @RequestMapping(value = "/notice/email",method = RequestMethod.POST)
    public String generateEmail(@RequestBody NoticeRequest request) throws JRException, IOException {
        NoticeGen generator = NoticeGen.getInstance().getInstance();
        byte[] out = null;
        if(request.getEmail() != null){
            out = generator.generateMultiplePage(request.getNotice());
            EmailServiceImpl emailService = new EmailServiceImpl();
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            emailService.sendMessageWithAttachment(request.getEmail(),
                    "จดหมายติดตามหนี้ " + formater.format(today),
                    "ติดตามทวงหนี้",
                    "report-"+formater.format(today)+".pdf",out);

            return "Email Sent to : " + request.getEmail();
        }
        else
            return "Did not sent the Email!";
    }

    @RequestMapping(value = "/notice/pdf",method = RequestMethod.POST)
    public ResponseEntity<byte[]> generateFromArray(@RequestBody NoticeRequest request) throws JRException, IOException {
        byte[] out = null;
        NoticeGen generator = NoticeGen.getInstance().getInstance();
        out = generator.generateMultiplePage(request.getNotice());

        return ResponseEntity
                .ok()
                // Specify content type as PDF
                .header("Content-Type", "application/pdf; charset=UTF-8")
                // Tell browser to display PDF if it can
                .header("Content-Disposition", "inline; filename=\"" + "report" + ".pdf\"")
                .body(out);
    }
}
