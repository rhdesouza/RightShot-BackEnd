package rightShot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rightShot.repository.DashboardDao;

@Service
public class DashboardService {

	@Autowired
	DashboardDao dashboardDao;

	public List<Object> buscarQtdAirsoftPorSituacao() {
		List<Object> quantitativo = dashboardDao.getQtdAirsoftPorSituacao();
		return quantitativo;
	}

	public List<Object> buscarQtdClientePorSituacao() {
		List<Object> quantitativo = dashboardDao.getQtdClientesPorSituacao();
		return quantitativo;
	}

	public List<Object> buscarQtdFornecedoresPorSituacao() {
		List<Object> quantitativo = dashboardDao.getQtdFornecedoresPorSituacao();
		return quantitativo;
	}

}
