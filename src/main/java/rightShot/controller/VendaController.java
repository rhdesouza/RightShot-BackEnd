package rightShot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import rightShot.domain.Const;
import rightShot.dto.VendaPageableDTO;
import rightShot.entity.Venda;
import rightShot.service.VendaService;
import rightShot.vo.PageVO;

import java.util.Optional;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_VENDA})
    @PostMapping("/getAllClientePageable")
    public ResponseEntity<PageVO<VendaPageableDTO>> getAllVendaPageable(@RequestBody final PageVO<VendaPageableDTO> pageVo) {
        try {
            return new ResponseEntity<PageVO<VendaPageableDTO>>(vendaService.getAllVendaPageable(pageVo), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_VENDA_NEW})
    @PostMapping("/add")
    public ResponseEntity<Venda> saveVenda(@RequestBody final Venda venda) throws Exception {
        return Optional.ofNullable(vendaService.saveVenda(venda))
                .map(result -> new ResponseEntity<Venda>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_VENDA_SEND})
    @PostMapping("/sendEmailVenda/{idVenda}")
    public ResponseEntity<Boolean> enviaEmailVendaCliente(@PathVariable(name = "idVenda") final Long idVenda) {
        return Optional.ofNullable(vendaService.enviaEmailVendaCliente(idVenda))
                .map(result -> new ResponseEntity<Boolean>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_VENDA_CANCEL})
    @PostMapping("/cancelarVenda/{idVenda}")
    public ResponseEntity<Integer> cancelarVenda(@PathVariable(name = "idVenda") final Long idVenda) {
        return Optional.ofNullable(vendaService.cancelarVendaCliente(idVenda))
                .map(result -> new ResponseEntity<Integer>(result, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Secured({Const.ROLE_ADMIN_ADMIN, Const.ROLE_VENDA})
    @GetMapping("/one/{idVenda}")
    public ResponseEntity<Venda> buscarVendaPorId(@PathVariable(name = "idVenda") Long idVenda){
        return Optional.ofNullable(vendaService.buscarVendaPorId(idVenda))
                .map(result-> new ResponseEntity<Venda>(result, HttpStatus.OK))
                .orElseGet(()-> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }


}
