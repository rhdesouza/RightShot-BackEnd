package rightShot.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import rightShot.entity.Produto;

import java.util.List;

public interface DocumentationProdutoController {

    @Operation(summary = "Adiciona um produto.", description = "Adiciona um produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adicionou um produto",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))}),
            @ApiResponse(responseCode = "401", description = "Não autorizado;", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não localizado", content = @Content)
    })
    ResponseEntity<Produto> addProduto(@RequestBody final Produto produto);

    @Operation(summary = "Retorna uma lista de produtos", description = "Retorna uma lista de produtos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou uma lista de Produtos."),
            @ApiResponse(responseCode = "401", description = "Não autorizado;", content = @Content),
    })
    ResponseEntity<List<Produto>> getAllProdutos();

    @Operation(summary = "Desativa um produto", description = "Desativa um produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto desativado."),
            @ApiResponse(responseCode = "401", description = "Não autorizado;", content = @Content),
    })
    ResponseEntity<Produto> desativarProduto(@Parameter(description = "Id do produto") @PathVariable(name = "idProduto") Long idProduto);

    @Operation(summary = "Busca um produto", description = "Busca um prduto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retornou um produto"),
            @ApiResponse(responseCode = "401", description = "Não autorizado;", content = @Content),
    })
    ResponseEntity<Produto> buscarProdutoEspecifico(@Parameter(description = "Id do produto") @PathVariable(name = "idProduto") Long idProduto);

}
