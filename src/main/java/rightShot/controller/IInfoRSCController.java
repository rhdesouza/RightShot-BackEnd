package rightShot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightShot.domain.Const;
import rightShot.entity.InfoRSC;
import rightShot.service.InfoRSCService;

@RestController
@RequestMapping("/infoRSC")
public class IInfoRSCController {

	@Autowired
	private InfoRSCService infoSrcService;

	@Secured({ Const.ROLE_ADMIN_ADMIN })
	@PutMapping("/save")
	public ResponseEntity<InfoRSC> saveInfoSRC(@RequestBody final InfoRSC info) {
		return Optional.ofNullable(infoSrcService.salvar(info))
				.map(result -> new ResponseEntity<InfoRSC>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<InfoRSC>(HttpStatus.NOT_FOUND));
	}

	@Secured({ Const.ROLE_ADMIN_ADMIN })
	@GetMapping("/getInfoRSC")
	public ResponseEntity<InfoRSC> getInfoSRC() {
		return Optional.ofNullable(infoSrcService.getInfoRSC())
				.map(result -> new ResponseEntity<InfoRSC>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<InfoRSC>(HttpStatus.NOT_FOUND));
	}

}
