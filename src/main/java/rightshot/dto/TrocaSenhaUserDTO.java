package rightshot.dto;

import lombok.Data;

@Data
public class TrocaSenhaUserDTO {

    private Long idUser;
    private String confirmPass;
    private String newPass;

}
