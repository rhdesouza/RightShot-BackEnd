package rightshot.service;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rightshot.entity.Fornecedor;
import rightshot.repository.FornecedorRepository;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Slf4j
@Service
public class FornecedorService {

    @Autowired
    FornecedorRepository fornecedorRepository;

    public List<Fornecedor> buscarTodosFornecedores() {
        return fornecedorRepository.findAll();
    }

    public List<String> buscarTodosFornecedoresRazaoSocial() {
        return fornecedorRepository.listarFornecedoresPorRazaoSocial();
    }

    public Page<Fornecedor> getFornecedorPageable(Fornecedor filter, Pageable pageable) {
        filter.setSituacao(null);
        if (filter == null) {
            return fornecedorRepository.findAll(pageable);
        } else {
            ExampleMatcher matcher = ExampleMatcher
                    .matchingAll()
                    .withMatcher("nomeFantasia", contains().ignoreCase())
                    .withMatcher("razaoSocial", contains().ignoreCase())
                    .withMatcher("cpfCnpj", contains().ignoreCase());

            return fornecedorRepository.findAll(
                    Example.of(filter, matcher),
                    pageable);
        }
    }

    public Fornecedor buscarFornecedorPorId(Long idFornecedor) {
        try {
            return fornecedorRepository.findById(idFornecedor)
                    .orElseThrow(() -> new NotFoundException("Fornecedor não localizado"));
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    public Fornecedor save(Fornecedor fornecedor) {
        try {
            Fornecedor forn = fornecedorRepository.save(fornecedor);
            return forn;
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return fornecedor;
    }

}