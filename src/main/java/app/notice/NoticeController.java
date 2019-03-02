package app.notice;

import app.Mail.EmailServiceImpl;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class NoticeController {

    @RequestMapping(value = "/notice/pdf",method = RequestMethod.POST)
    public ResponseEntity<byte[]> generatePdf(@RequestBody Notice notice) throws JRException, IOException {

        NoticeGen generator = NoticeGen.getInstance().getInstance();
        byte[] out = null;

        out = generator.generateSingleNoticePdf(notice);

        return ResponseEntity
                .ok()
                // Specify content type as PDF
                .header("Content-Type", "application/pdf; charset=UTF-8")
                // Tell browser to display PDF if it can
                .header("Content-Disposition", "inline; filename=\"" + notice.getNoticeType() + ".pdf\"")
                .body(out);
    }

    @RequestMapping(value = "/notice/email",method = RequestMethod.POST)
    public String generateEmail(@RequestBody Notice notice) throws JRException, IOException {

        NoticeGen generator = NoticeGen.getInstance().getInstance();
        byte[] out = null;

        if(notice.getEmail() != null){
            out = generator.generateSingleNoticePdf(notice);
            EmailServiceImpl emailService = new EmailServiceImpl();
            emailService.sendMessageWithAttachment(notice.getEmail(),"จดหมายติดตามหนี้","ติดตามทวงหนี้","จดหมายติดตามหนี้.pdf",out);

            return "Email Sent to : " + notice.getEmail();
        }
        else
            return "Did not sent the Email!";
    }

    @RequestMapping(value = "/notice/multiple/pdf",method = RequestMethod.POST)
    public ResponseEntity<byte[]> generateFromArray(@RequestBody List<Notice> noticeResponse) throws JRException, IOException {

        byte[] out = null;
        NoticeGen generator = NoticeGen.getInstance().getInstance();
        out = generator.generateMultiplePage(noticeResponse);

        return ResponseEntity
                .ok()
                // Specify content type as PDF
                .header("Content-Type", "application/pdf; charset=UTF-8")
                // Tell browser to display PDF if it can
                .header("Content-Disposition", "inline; filename=\"" + "report" + ".pdf\"")
                .body(out);
    }


}
