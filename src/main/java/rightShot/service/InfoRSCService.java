package rightShot.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import rightShot.entity.InfoRSC;
import rightShot.repository.InfoRSCRepository;

@Slf4j
@Service
public class InfoRSCService {

	@Autowired
	public InfoRSCRepository iInfoRSC;

	public InfoRSC salvar(InfoRSC info) {
		try {
			if (info.getId() == null)
				return null;

			return iInfoRSC.save(info);
		} catch (Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}

	public InfoRSC getInfoRSC() {
		try {
			return iInfoRSC.findById(1).orElse(null);
		} catch (Exception ex) {
			log.error(ex.toString());
			return null;
		}
	}

	public String[] getEmailSocios() {
		InfoRSC info = iInfoRSC.findById(1).get();

		ArrayList<String> ar = new ArrayList<>();
		ar.add(info.getEmailSocio1());

		if (info.getEmailSocio2() != null)
			ar.add(info.getEmailSocio2());

		if (ar.size() > 1)
			return new String[] { info.getEmailSocio1(), info.getEmailSocio2() };
		else
			return new String[] { info.getEmailSocio1() };

	}

}
