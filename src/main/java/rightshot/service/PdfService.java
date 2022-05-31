package rightshot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class PdfService {

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public byte[] getPdfTest() {
        Context context = new Context();
        context.setVariable("name", "Rafael Henrique");

        return this.pdfGeneratorService.downloadPdfFromHtml("example", context);
    }
}
