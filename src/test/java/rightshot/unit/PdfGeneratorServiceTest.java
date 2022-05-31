package rightshot.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import rightshot.entity.Cliente;
import rightshot.entity.FotoCliente;
import rightshot.service.PdfGeneratorService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class PdfGeneratorServiceTest {

    @MockBean
    private PdfGeneratorService pdfGeneratorService;

    @BeforeEach
    void preparaMock() {
    }

    @Test
    public void QuandoGerarPdf_Ok(){

    }

}
