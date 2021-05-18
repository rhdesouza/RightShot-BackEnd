package rightShot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rightShot.entity.Menu;
import rightShot.repository.IMenu;

@Service
public class MenuService {

	@Autowired
	private IMenu iMenuDao;

	public List<Menu> getAllMenu() {
		return iMenuDao.listarMenuAtivo();
	}

}
