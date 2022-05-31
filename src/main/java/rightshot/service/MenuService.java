package rightshot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rightshot.entity.Menu;
import rightshot.repository.MenuRepository;

@Service
public class MenuService {

	@Autowired
	private MenuRepository iMenuDao;

	public List<Menu> getAllMenu() {
		return iMenuDao.listarMenuAtivo();
	}

}
