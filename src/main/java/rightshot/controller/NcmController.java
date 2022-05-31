package rightshot.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import rightshot.entity.Ncm;
import rightshot.service.NcmService;

@RestController
@RequestMapping("/ncm")
public class NcmController {

	@Autowired
	NcmService ncmService;

	@GetMapping("/one/{ncm}")
	public ResponseEntity<List<Ncm>> buscarNcmPorId(@PathVariable(name = "ncm") String ncm) {
		return Optional.ofNullable(ncmService.buscarNcmPorId(ncm))
				.map(result -> new ResponseEntity<List<Ncm>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Ncm>>(HttpStatus.NOT_FOUND));
	}

	@PostMapping(value = ("/setArquivoNcm"), headers = "content-type=multipart/form-data")
	public ResponseEntity<Boolean> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
		return Optional.ofNullable(ncmService.lerArquivoNcm(file))
				.map(result -> new ResponseEntity<Boolean>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND));

	}

	@GetMapping("/isUploadNcm")
	public ResponseEntity<Boolean> isUploadNcm() {
		return Optional.ofNullable(ncmService.isUploaderNcm())
				.map(result -> new ResponseEntity<Boolean>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND));
	}

}
