package rightShot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rightShot.entity.Estoque;

public interface IEstoque extends JpaRepository<Estoque, Long>{

}
