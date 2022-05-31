package rightshot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    public PdfGeneratorService(TemplateEngine templateEngine) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setPrefix("templates/");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        this.templateEngine = templateEngine;
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    /**
     * Gera o arquivo pdf para download
     *
     * @param templateName Template que será renderizado.
     * @param context      Informações que serão injetadas no template.
     * @return byte[]
     */
    public byte[] downloadPdfFromHtml(String templateName, Context context) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String templateHtml = templateEngine.process(templateName, context);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(templateHtml);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

}
