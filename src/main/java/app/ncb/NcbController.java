package app.ncb;

import config.ConfigProperty;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class NcbController {

    @RequestMapping("/ncb")
    public String ncbIndex() throws IOException {
        ConfigProperty config = ConfigProperty.getInstance();
        return String.format("Resources Using for NCB Report templateNcb = %s fileLocation = %s", config.getTemplateNcb(), config.getHeader());
    }

    @RequestMapping(name = "/ncb/gen", method = RequestMethod.GET)
    public ResponseEntity<byte[]> index() throws JRException, IOException {

        NcbGenerator generator = NcbGenerator.getInstance().getInstance();
        byte[] out = null;

        out = generator.generateNcbPdf();

        return ResponseEntity
                .ok()
                // Specify content type as PDF
                .header("Content-Type", "application/pdf; charset=UTF-8")
                // Tell browser to display PDF if it can
                .header("Content-Disposition", "inline; filename=\"" + "ncb" + ".pdf\"")
                .body(out);
    }

}
