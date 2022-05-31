package rightshot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rightshot.audit.Auditable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Audited
public class User extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String nomeCompleto;

    private String user;

    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;

    private Date dateChangePassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NotAudited
    private List<Role> roles;

    private String fileName;
    private String fileType;
    private Long size;
    @Lob
    private byte[] data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoUser situacao;

    public User() {
    }

    public User(String name, String email, SituacaoUser situacao) {
        super();
        this.user = name;
        this.email = email;
        this.situacao = situacao;
    }

    public User(User user) {
        super();
        this.nomeCompleto = user.getNomeCompleto();
        this.user = user.getUser();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.id = user.getId();
        this.situacao = user.getSituacao();
    }


    public User(String nomeCompleto, String user, String email, String password, List<Role> roles,
                SituacaoUser situacao) {
        super();
        this.nomeCompleto = nomeCompleto;
        this.user = user;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.situacao = situacao;
    }

    public User(String nomeCompleto, String user, String email, String password, List<Role> roles,
                SituacaoUser situacao, Date dateChangePassword) {
        this.nomeCompleto = nomeCompleto;
        this.user = user;
        this.email = email;
        this.roles = roles;
        this.password = password;
        this.situacao = situacao;
        this.dateChangePassword = dateChangePassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public SituacaoUser getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoUser situacao) {
        this.situacao = situacao;
    }

    public String toJson() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(this);
        return json;
    }

}
