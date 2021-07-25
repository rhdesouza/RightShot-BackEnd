package rightShot.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import rightShot.domain.Const;
import rightShot.dto.EstoqueDTO;
import rightShot.entity.Estoque;
import rightShot.service.EstoqueService;
import rightShot.vo.PageVO;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    EstoqueService estoqueService;

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE})
    @GetMapping("/getAll")
    public ResponseEntity<List<Estoque>> buscaEstoque() {
        return Optional.ofNullable(estoqueService.getAllEstoque())
                .map(result -> new ResponseEntity<List<Estoque>>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE})
    @GetMapping("/getAllSintetico")
    public ResponseEntity<List<EstoqueDTO>> buscaEstoqueSintetico() {
        return Optional.ofNullable(estoqueService.getAllEstoqueSintetico())
                .map(result -> new ResponseEntity<List<EstoqueDTO>>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_ESTOQUE})
    @PostMapping("/getAllSinteticoPageable")
    public ResponseEntity<PageVO<EstoqueDTO>> buscaEstoqueSinteticoPageable(
            @RequestBody final PageVO<EstoqueDTO> pageVo) {
        return Optional.ofNullable(estoqueService.getAllEstoqueSintetivoPageable(pageVo))
                .map(result -> new ResponseEntity<PageVO<EstoqueDTO>>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("disponivel/{idProduto}/{qtd}")
    public ResponseEntity<Boolean> validaEstoquePorProduto(@NotNull @PathVariable Long idProduto,
                                                           @NotNull @PathVariable Long qtd) {
        return Optional.ofNullable(estoqueService.validaEstoquePorProduto(idProduto, qtd))
                .map(result -> new ResponseEntity<Boolean>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<Boolean>(HttpStatus.NOT_FOUND));
    }

}
