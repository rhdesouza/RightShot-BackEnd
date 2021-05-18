package rightShot.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import rightShot.entity.Ncm;
import rightShot.repository.INcm;

@Slf4j
@Service
public class NcmService {

	@Autowired
	private INcm iNcm;

	public Boolean lerArquivoNcm(MultipartFile fileUpload) throws IOException {
		try {

			if (iNcm.findAll().isEmpty())
				return false;

			File fileTemp = new File("src/main/resources/targetFile.tmp");
			try (OutputStream os = new FileOutputStream(fileTemp)) {
				os.write(fileUpload.getBytes());
			}

			Scanner myReader = new Scanner(fileTemp);
			String[] linhaArquivo = new String[11];

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				linhaArquivo = data.split(",");
				if (linhaArquivo.length == 10) {
					iNcm.save(new Ncm(linhaArquivo[0], linhaArquivo[1], linhaArquivo[2], linhaArquivo[3],
							linhaArquivo[4], linhaArquivo[5], linhaArquivo[6], linhaArquivo[7], linhaArquivo[8],
							linhaArquivo[9]));
				}
			}
			myReader.close();

			// Registro defult
			iNcm.save(new Ncm("99999999", "NCM não encontrado", "NCM não encontrado", null, null, null, null, null,
					null, null));

			// Apagar arquivo dps de criado.
			fileTemp.delete();
			return true;
		} catch (FileNotFoundException e) {
			log.error(e.toString());
			return false;
		}

	}

	public List<Ncm> buscarNcmPorId(String numNcm) {
		try {
			List<Ncm> ncm = new ArrayList<>();
			ncm = iNcm.listarNcmPorIdLike(numNcm);
			return ncm;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return Collections.emptyList();

	}

	public Boolean isUploaderNcm() {
		try {
			Boolean retorno = iNcm.findAll().isEmpty();
			return retorno;
		} catch (Exception e) {
			log.error(e.toString());
			return false;
		}
	}

}
