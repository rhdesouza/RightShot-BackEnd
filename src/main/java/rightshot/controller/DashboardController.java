package rightshot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightshot.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@GetMapping("/qtdAirsoftSituacao")
	public ResponseEntity<List<Object>> qtdAirsoftSituacao() {
		return Optional.ofNullable(dashboardService.buscarQtdAirsoftPorSituacao())
				.map(result -> new ResponseEntity<List<Object>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Object>>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/qtdClienteSituacao")
	public ResponseEntity<List<Object>> qtdClienteSituacao() {
		return Optional.ofNullable(dashboardService.buscarQtdClientePorSituacao())
				.map(result -> new ResponseEntity<List<Object>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Object>>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/qtdFornecedorSituacao")
	public ResponseEntity<List<Object>> qtdFornecedoresSituacao() {
		return Optional.ofNullable(dashboardService.buscarQtdFornecedoresPorSituacao())
				.map(result -> new ResponseEntity<List<Object>>(result, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<List<Object>>(HttpStatus.NOT_FOUND));
	}

}
