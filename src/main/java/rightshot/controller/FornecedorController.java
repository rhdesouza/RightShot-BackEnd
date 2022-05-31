package rightshot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rightshot.domain.Const;
import rightshot.dto.FornecedorDTO;
import rightshot.entity.Fornecedor;
import rightshot.service.FornecedorService;
import rightshot.vo.PageVO;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

    @Autowired
    FornecedorService fornecedorService;

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR})
    @GetMapping("/all")
    @Cacheable
    public ResponseEntity<List<Fornecedor>> buscarTodosFornecedores() {
        return Optional.ofNullable(fornecedorService.buscarTodosFornecedores())
                .map(result -> new ResponseEntity<List<Fornecedor>>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<List<Fornecedor>>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR})
    @GetMapping("/one/{idFornecedor}")
    public ResponseEntity<Fornecedor> buscarFornecedorPorId(@PathVariable(name = "idFornecedor") Long idFornecedor) {
        return Optional.ofNullable(fornecedorService.buscarFornecedorPorId(idFornecedor))
                .map(result -> new ResponseEntity<Fornecedor>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<Fornecedor>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR_NEW, Const.ROLE_FORNECEDOR_EDIT})
    @PostMapping("/add")
    public ResponseEntity<Fornecedor> addFornecedor(@RequestBody final Fornecedor fornecedor) {
        return Optional.ofNullable(fornecedorService.save(fornecedor))
                .map(result -> new ResponseEntity<Fornecedor>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<Fornecedor>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR})
    @GetMapping("/allFornecedorRazaoSocial")
    @Cacheable
    public ResponseEntity<List<String>> getAllRazaoSocial() {
        return Optional.ofNullable(fornecedorService.buscarTodosFornecedoresRazaoSocial())
                .map(result -> new ResponseEntity<List<String>>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<List<String>>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_FORNECEDOR})
    @PostMapping("/getFornecedorPageable")
    @Cacheable
    public ResponseEntity<Page<Fornecedor>> getFornecedorNewPage(Pageable pageable, @RequestBody(required=false) Fornecedor filter) {
        return Optional.ofNullable(fornecedorService.getFornecedorPageable(filter, pageable))
                .map(result -> new ResponseEntity<Page<Fornecedor>>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
