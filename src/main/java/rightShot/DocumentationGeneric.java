package rightShot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import rightShot.entity.Cliente;

public interface DocumentationGeneric {

    @Operation(summary = "Teste override",description = "Teste override!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou um Cliente a ser pesquisado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cliente.class))}),
            @ApiResponse(responseCode = "401", description = "Não autorizado;", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não localizado", content = @Content)
    })
    public ResponseEntity<Cliente> buscarClientePorId(Long idCliente);



}
