package rightShot.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rightShot.dto.ClienteDTO;
import rightShot.dto.VendaPageableDTO;
import rightShot.entity.SituacaoVenda;
import rightShot.entity.Venda;
import rightShot.entity.VendaItens;
import rightShot.exception.RegraDeNegocioException;
import rightShot.repository.VendaRepository;
import rightShot.vo.PageVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class VendaService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public VendaRepository vendaRepository;

    @Autowired
    public EstoqueService estoqueService;

    @Autowired
    public EmailService emailService;

    @Autowired
    private UtilService utilService;

    public Venda saveVenda(Venda venda) throws Exception {
        var vendaSalva = new Venda();
        try {
            this.regraDeNegocio_naoEPossivelEdiarVenda(venda);
            this.regraDeNegocio_confereQuantidadeEstoque(venda);
            this.regraDeNegocio_confereValorCabecalhoVenda(venda);
            if (venda.getEmailEnviado() == null)
                venda.setEmailEnviado(false);

            vendaSalva = vendaRepository.save(venda);

            if (Boolean.FALSE.equals(vendaSalva.getEmailEnviado()))
                this.enviaEmailVendaCliente(vendaSalva.getId());


            return vendaSalva;
        } catch (RegraDeNegocioException r) {
            throw new RegraDeNegocioException(r.getMessage());
        } catch (Exception e) {
            if (venda.getId() != null)
                vendaRepository.deleteById(venda.getId());
            log.error(e.toString());
            throw new Exception(e.getMessage());
        }
    }

    private void regraDeNegocio_naoEPossivelEdiarVenda(@NotNull Venda venda) {
        if (venda.getId() != null) {
            throw new RegraDeNegocioException(
                    new StringBuilder().append("A nota fiscal de id ")
                            .append(venda.getId())
                            .append(" não poderá ser editada. Cancele a atual e abra uma nova.").toString());
        }
    }

    public void regraDeNegocio_confereQuantidadeEstoque(@NotNull Venda venda) {
        venda.getVendaItens().forEach(item -> {
            if (!estoqueService.validaEstoquePorProduto(item.getProduto().getId(), (long) item.getQtd()))
                throw new RegraDeNegocioException(
                        new StringBuilder().append("O produto ")
                                .append(item.getProduto().getDescricao())
                                .append(" não esta disponivél em estoque para quantidade desejada.").toString());
        });
    }

    public void regraDeNegocio_confereValorCabecalhoVenda(@NotNull Venda venda) {
        BigDecimal valorVenda = venda.getVendaItens().stream()
                .map(VendaItens::getValorVenda)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorDesconto = venda.getVendaItens().stream()
                .map(VendaItens::getValorDesconto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorTotalItens = venda.getVendaItens().stream()
                .map(VendaItens::getValorProduto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!valorVenda.equals(venda.getValorTotalVenda()) || !valorDesconto.equals(venda.getValorDescontoNota())
                || !valorTotalItens.equals(venda.getValorTotalItens()))
            throw new RegraDeNegocioException("Valor do cabeçalho não corresponde ao valor informado!");

    }

    public Boolean enviaEmailVendaCliente(Long idVenda) {
        var venda = vendaRepository.findByIdVenda(idVenda)
                .orElseThrow(() -> new RegraDeNegocioException("Número da venda não localizado."));
        return this.emailService.notificacaoVendaCliente(venda);
    }

    public PageVO<VendaPageableDTO> getAllVendaPageable(@NotNull PageVO<VendaPageableDTO> pageVo) {
        try {
            final TypedQuery<VendaPageableDTO> query = entityManager.createNamedQuery("VendaPageableDTO", VendaPageableDTO.class);
            String where = "";

            String sqlQuery = query.unwrap(Query.class).getQueryString();
            sqlQuery = sqlQuery.replace("HashWhereOrderBy",
                    " ORDER BY " + pageVo.getSort() + " " + pageVo.getSortDirection());

            if (pageVo.getFilterForm() != null) {
                where = this.utilService.gerarWhereParaFiltro(pageVo.getFilterForm());
            }
            sqlQuery = sqlQuery.replace("HashWhereFilter", where);

            final javax.persistence.Query newQuery = entityManager.createNativeQuery(sqlQuery,
                    "VendaPageableDTOResultMapping");

            if (pageVo.isChangedQuery())
                pageVo.setTotalElements(this.utilService.CountQuery(newQuery, new HashMap<>()));

            newQuery.setFirstResult(pageVo.getPageIndex() * pageVo.getPageSize());
            newQuery.setMaxResults(pageVo.getPageSize());

            List<VendaPageableDTO> retorno = Collections.checkedList(newQuery.getResultList(), ClienteDTO.class);
            pageVo.setContent(retorno);

            return pageVo;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    public Integer cancelarVendaCliente(Long idVenda) {
        var venda = vendaRepository.findByIdVenda(idVenda)
                .orElseThrow(() -> new RegraDeNegocioException("Número da venda não localizado."));

        if (venda.getSituacaoVenda().equals(SituacaoVenda.CANCELADO))
            throw new RegraDeNegocioException("A venda informada já está cancelada.");

        venda.setSituacaoVenda(SituacaoVenda.CANCELADO);
        Venda save = vendaRepository.save(venda);

        return save.getSituacaoVenda().ordinal();
    }

    public Venda buscarVendaPorId(Long idVenda) {
        var venda = vendaRepository.findByIdVenda(idVenda)
                .orElseThrow(() -> new RegraDeNegocioException("Venda não localizada."));

        return venda;
    }

}
