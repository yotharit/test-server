package app.ncb;

import app.mail.EmailServiceImpl;

import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class NcbController {

    @RequestMapping(value = "/ncb/pdf",method = RequestMethod.POST)
    public ResponseEntity<byte[]> generatePdf(@RequestBody Ncb ncb) throws JRException, IOException {

        NcbGenerator generator = NcbGenerator.getInstance().getInstance();
        byte[] out = null;

        out = generator.generateNcbPdf(ncb);

        return ResponseEntity
                .ok()
                // Specify content type as PDF
                .header("Content-Type", "application/pdf; charset=UTF-8")
                // Tell browser to display PDF if it can
                .header("Content-Disposition", "inline; filename=\"" + "ncb" + ".pdf\"")
                .body(out);
    }

    @RequestMapping(value = "/ncb/email",method = RequestMethod.POST)
    public String generateEmail(@RequestBody Ncb ncb) throws JRException, IOException {

        NcbGenerator generator = NcbGenerator.getInstance().getInstance();
        byte[] out = null;

        if(ncb.getSendingEmail() != null){
            out = generator.generateNcbPdf(ncb);
            EmailServiceImpl emailService = new EmailServiceImpl();
            emailService.sendMessageWithAttachment(ncb.getSendingEmail(),"NCB-Email","Test NCB SENDING","ncb.pdf",out);

            return "Email Sent to : " + ncb.getSendingEmail();
        }
        else
            return "Did not sent the Email!";
    }

}
