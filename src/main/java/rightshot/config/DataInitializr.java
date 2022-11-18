package rightshot.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import rightshot.domain.Const;
import rightshot.entity.*;
import rightshot.repository.FornecedorRepository;
import rightshot.repository.InfoRSCRepository;
import rightshot.repository.MenuRepository;
import rightshot.repository.INFRepository;
import rightshot.repository.NcmRepository;
import rightshot.repository.NfItensRepository;
import rightshot.repository.NfPagamentoRepository;
import rightshot.repository.ProdutoRepository;
import rightshot.repository.TipoProdutoRepository;
import rightshot.repository.RoleRepository;
import rightshot.repository.UserRepository;
import rightshot.service.FornecedorService;
import rightshot.service.NcmService;


//@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = Logger.getLogger(DataInitializr.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MenuRepository iMenuDao;

    @Autowired
    TipoProdutoRepository iTipoProduto;

    @Autowired
    INFRepository iNF;

    @Autowired
    FornecedorRepository iFornecedor;

    @Autowired
    NfPagamentoRepository iNfPagamento;

    @Autowired
    NfItensRepository iNfItens;

    @Autowired
    ProdutoRepository iProduto;

    @Autowired
    NcmRepository iNcm;

    @Autowired
    NcmService ncmService;

    @Autowired
    private InfoRSCRepository iInfoRSC;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {

        this.initialInfoSRC();

        this.inicializaRoles();

        this.initialUserAdmin();

        this.createMenuInicial();

        if (iTipoProduto.findAll().isEmpty()) {
            createTipoProduto();
        }

        if (iProduto.findAll().isEmpty()) {
            this.createProduto();
        }

        if (fornecedorRepository.findAll().isEmpty()) {
            this.mockFornecedores();
        }

    }

    /**
     * Inicializa as roles conforme arquivo de Constantes.
     */
    private void inicializaRoles() {
        try {
            Field[] roles = Const.class.getDeclaredFields();
            for (int i = 0; i < roles.length; i++) {
                if (roleRepository.findByName(roles[i].getName()) == null) {
                    String role = roles[i].getName();
                    Modulo modulo = Modulo.valueOf(role.split("_")[1]);
                    Role rol;

                    if (role.split("_").length > 2) {
                        ModuloAcao moduloAcao = ModuloAcao.valueOf(role.split("_")[2]);
                        rol = new Role(role, modulo, moduloAcao);
                    } else {
                        rol = new Role(role, modulo);
                    }

                    roleRepository.save(rol);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar/criar ROLES. Error: {0}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Inicializa o acesso Admin.
     */
    private void initialUserAdmin() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            User user = new User("Administrador", "Admin", "email@email.com", passwordEncoder.encode("admin"),
                    Arrays.asList(roleRepository.getOneRoleByName(Const.ROLE_ADMIN_ADMIN)), SituacaoUser.ATIVO);
            userRepository.save(user);
        }
    }

    /**
     * Cria o menu Inicial para consumo do front-end.
     */
    public void createMenuInicial() {
        if ((iMenuDao.findAll()).isEmpty()) {
            List<Menu> menus = new ArrayList<>();

            menus.add(new Menu("Principal", "DashBoard", "/dashboard", "dashboard", 0, false));
            menus.add(new Menu("Clientes", "Clientes", "/cliente-list", "local_library", 2, false));
            menus.add(new Menu("Fornecedores", "Fornecedores", "/fornecedor-list", "business", 3, false));
            menus.add(new Menu("Vendas", "Vendas", "/vendas", "attach_money", 4, true));
            menus.add(new Menu("Estoque/Produto", "Estoque/Produto", "/estoque", "widgets", 5, false));
            menus.add(new Menu("Financeiro", "Financeiro", "/financeiro", "attach_money", 6, false));

            iMenuDao.saveAll(menus);
        }
    }

    private void initialInfoSRC() {
        if (!iInfoRSC.findById(1).isPresent()) {
            InfoRSC info = new InfoRSC();
            info.setId(1);
            info.setCnpj("27729054000149");
            info.setNomeEmpresa("RIGHT SHOT AIRSOFT CLUB COMERCIO E ALUGUEL DE ARTIGOS ESPORTIVOS LTDA");
            info.setNomeFantasia("RIGHT SHOT CLUB");
            info.setTelefone1("3130222326");
            info.setEmailEmpresa("rightshotclub@gmail.com");
            info.setSocio1("LINCOLN LISBOA RAMOS");
            info.setEmailSocio1("rhdesouza@hotmail.com");
            info.setSocio2("RAMON LISBOA RAMOS");
            info.setEmailSocio1("rhdesouza@hotmail.com");

            iInfoRSC.save(info);
        }
    }

    public void createTipoProduto() {
        List<TipoProduto> tps = new ArrayList<>();
        tps.add(new TipoProduto("Airsoft", UnidadeTipoProduto.UN, UnidadeTipoProduto.UN));
        tps.add(new TipoProduto("Alvos Papel A3", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.UN));
        tps.add(new TipoProduto("Alvos Papel A4", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.UN));
        tps.add(new TipoProduto("Camisa Promo", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.UN));
        tps.add(new TipoProduto("Chaveiro", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.UN));
        tps.add(new TipoProduto("Munição BBS", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.KG));
        tps.add(new TipoProduto("Munição BBK", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.KG));
        tps.add(new TipoProduto("Flecha para arco V. UNID", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.KG));
        tps.add(new TipoProduto("Flecha para arco V. PCTE", UnidadeTipoProduto.PCTE, UnidadeTipoProduto.KG));

        iTipoProduto.saveAll(tps);
    }

    public void createProduto() {
        List<Produto> lp = new ArrayList<>();

        if (iNcm.findAll().isEmpty()) {
            iNcm.save(new Ncm("99999999", "CATEGORIA - NCM", "DESCRICAO - NCM", "nt", "01/01/2121", "", "uTrib",
                    "descricaoUTrib", "gtinProducao", "gtinHomologacao"));
        }

        lp.add(new Produto("25207657", "AIRSOFT RIFLE CYMA M4 CQB RIS ET (CM506S) ELET.6MM",
                iTipoProduto.findByTipo("Airsoft").orElseThrow(), 2l,
                iNcm.findById("99999999").isPresent() ? iNcm.findById("99999999").orElseThrow() : null));

        lp.add(new Produto("25207658", "AIRSOFT RIFLE CYMA M4A1 CUSTOM ET(CM515S) ELET.6MM",
                iTipoProduto.findByTipo("Airsoft").orElseThrow(), 2l, iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("25206700", "AIRSOFT RIFLE GEG CM16 CARBINE ELET. 6MM Num de Serie:0201416",
                iTipoProduto.findByTipo("Airsoft").orElseThrow(), 2l, iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("25207300", "ESFERA PLAST BBK 0,20G BRANCA (4000UN)6MM",
                iTipoProduto.findByTipo("Munição BBK").orElseThrow(), 2l, iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("25206068", "CHUMBINHO ROSSI SPORT 5,5MM (125 UN)", iTipoProduto.findByTipo("Munição BBS").orElseThrow(), 5l,
                iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("25201103", "FLECHA FIBRA PRETA 30p", iTipoProduto.findByTipo("Flecha para arco V. UNID").orElseThrow(), 20l,
                iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("1524502051", "PIST PRESSAO KWC 24/7 SLIDE METAL MOLA CAL.4,5MM",
                iTipoProduto.findById(2L).orElseThrow(), 1l, iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("1424501051", "PIST PRES WINGUN W125B CO2 4,5MM", iTipoProduto.findByTipo("Airsoft").orElseThrow(), 1l,
                iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("25207628", "AIRSOFT PIST. VG P.1918 MOLA 6MM", iTipoProduto.findByTipo("Airsoft").orElseThrow(), 1l,
                iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("25206744", "AIRSOFT RIFLE CYMA SCAR LABS (CM067BK) ELET 6MM",
                iTipoProduto.findByTipo("Airsoft").orElseThrow(), 2l, iNcm.findById("99999999").orElseThrow()));

        lp.add(new Produto("1515155", "CAMISA CUSTOM RIGHT SHOT CLUB",
                iTipoProduto.findByTipo("Camisa Promo").orElseThrow(), 2l, iNcm.findById("99999999").orElseThrow()));

        iProduto.saveAll(lp);
    }

    public boolean mockFornecedores() {
        if (!fornecedorRepository.existsById(1L)) {
            List<Fornecedor> fornecedores = new ArrayList<>();

            Fornecedor fonecedor1 = new Fornecedor("André e Renata Construções ME", "André e Renata Construções ME",
                    "59833557000156", "1111111", SituacaoFornecedor.Ativo);
            fornecedores.add(fonecedor1);

            Fornecedor fonecedor2 = new Fornecedor("Fernando e Juliana Informática Ltda",
                    "Fernando e Juliana Informática Ltda", "58669813000159", "2111111", SituacaoFornecedor.Ativo);
            fornecedores.add(fonecedor2);

            Fornecedor fonecedor3 = new Fornecedor("Patrícia e Henry Entulhos Ltda", "Patrícia e Henry Entulhos Ltda",
                    "34005392000101", "3111111", SituacaoFornecedor.Ativo);
            fornecedores.add(fonecedor3);

            Fornecedor fonecedor4 = new Fornecedor("Agatha e Letícia Consultoria Financeira Ltda",
                    "Agatha e Letícia Consultoria Financeira Ltda", "29335997000112", "1138974504",
                    SituacaoFornecedor.Ativo);
            fornecedores.add(fonecedor4);

            Fornecedor fonecedor5 = new Fornecedor("AMADEO ROSSI IMP. E DIST. DE ARTIGOS ESPORTIVOS LTDA",
                    "AMADEO ROSSI IMP. E DIST. DE ARTIGOS ESPORTIVOS LTDA", "96735105000168", "35912100",
                    SituacaoFornecedor.Ativo);
            fornecedores.add(fonecedor5);

            fornecedorRepository.saveAll(fornecedores);
            return true;
        } else {
            return false;
        }
    }

}
