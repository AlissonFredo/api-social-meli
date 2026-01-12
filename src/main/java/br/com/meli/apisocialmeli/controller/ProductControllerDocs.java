package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Products", description = "Operações de products")
public interface ProductControllerDocs {
    @Operation(
            summary = "Publicar produto",
            description = "Cadastra um produto e cria um post associado ao vendedor (SELLER).",
            operationId = "cadastrarProduto"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostRequestDto.class),
                    examples = @ExampleObject(name = "Exemplo de requisição", value = "{\"userId\":5,\"product\":{\"productName\":\"Tênis Azul\",\"type\":\"FOOTWEAR\",\"brand\":\"Acme\",\"color\":\"Azul\",\"notes\":\"Edição limitada\"},\"category\":100,\"price\":199.9}")
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto publicado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class),
                            examples = @ExampleObject(name = "Produto publicado com sucesso", value = "{\"postId\":321,\"userId\":5,\"category\":100,\"price\":199.9,\"product\":{\"productName\":\"Tênis Azul\",\"type\":\"FOOTWEAR\",\"brand\":\"Acme\",\"color\":\"Azul\",\"notes\":\"Edição limitada\"}}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário (vendedor) não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Vendedor inexistente", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/publish\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um vendedor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Não é vendedor", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/publish\"}")
                    )
            )
    })
    ResponseEntity<?> cadastrarProduto(PostRequestDto product);

    @Operation(
            summary = "Listar posts recentes de vendedores seguidos",
            description = "Retorna os posts dos últimos 14 dias dos vendedores seguidos por um comprador (BUYER), com opção de ordenação por data.",
            operationId = "getFollowedSuppliersRecentProducts"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostsFollowingLastTwoWeeksResponseDto.class),
                            examples = @ExampleObject(name = "Exemplo 200", value = "{\"userId\":10,\"posts\":[{\"postId\":321,\"userId\":201,\"createdAt\":\"2026-01-11T14:20:00Z\",\"category\":100,\"price\":199.9,\"product\":{\"productName\":\"Tênis Azul\",\"type\":\"FOOTWEAR\",\"brand\":\"Acme\",\"color\":\"Azul\",\"notes\":\"Edição limitada\"}},{\"postId\":322,\"userId\":202,\"createdAt\":\"2026-01-09T10:00:00Z\",\"category\":200,\"price\":89.9,\"product\":{\"productName\":\"Camiseta Preta\",\"type\":\"CLOTHING\",\"brand\":\"Acme\",\"color\":\"Preto\",\"notes\":\"Algodão 100%\"}}]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo 404", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 10 não encontrado\",\"path\":\"/followed/10/list\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um comprador",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo 422", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 10 não é um comprador\",\"path\":\"/followed/10/list\"}")
                    )
            )
    })
    ResponseEntity<?> getFollowedSuppliersRecentProducts(
            @Parameter(description = "ID do comprador (deve ser do tipo BUYER)", example = "10") Long userId,
            @Parameter(description = "Ordenação por data", example = "date_asc", schema = @Schema(type = "string", allowableValues = {"date_asc", "date_desc"}, defaultValue = "date_asc")) String order
    );

    @Operation(
            summary = "Publicar produto promocional",
            description = "Cadastra um produto e cria um post promocional associado ao vendedor (SELLER).",
            operationId = "cadastraProdutoPromocional"
    )
    @RequestBody(
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostPromoPubRequestDto.class),
                    examples = @ExampleObject(name = "Exemplo de requisição", value = "{\"userId\":5,\"product\":{\"productName\":\"Tênis Azul\",\"type\":\"FOOTWEAR\",\"brand\":\"Acme\",\"color\":\"Azul\",\"notes\":\"Edição limitada\"},\"category\":100,\"price\":199.9,\"hasPromo\":true,\"discount\":0.15}")
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto promocional publicado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostPromoPubResponseDto.class),
                            examples = @ExampleObject(name = "Exemplo 200", value = "{\"postId\":456,\"userId\":5,\"category\":100,\"price\":199.9,\"hasPromo\":true,\"discount\":0.15,\"product\":{\"productName\":\"Tênis Azul\",\"type\":\"FOOTWEAR\",\"brand\":\"Acme\",\"color\":\"Azul\",\"notes\":\"Edição limitada\"}}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário (vendedor) não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo 404", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/promo-pub\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um vendedor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo 422", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/promo-pub\"}")
                    )
            )
    })
    ResponseEntity<?> cadastraProdutoPromocional(PostPromoPubRequestDto post);

    @Operation(
            summary = "Obter total de produtos promocionais do vendedor",
            description = "Retorna o total de posts/produtos com promoção de um vendedor (SELLER).",
            operationId = "obterTotalPordutosPromoVendedor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TotalProdutosPromoResponnseDto.class),
                            examples = @ExampleObject(name = "Exemplo 200", value = "{\"userId\":5,\"userName\":\"Loja XPTO\",\"promosCount\":12}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário (vendedor) não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo 404", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/promo-pub/count?userId=5\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um vendedor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Exemplo 422", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/promo-pub/count?userId=5\"}")
                    )
            )
    })
    ResponseEntity<?> obterTotalPordutosPromoVendedor(@Parameter(description = "ID do vendedor (SELLER)", example = "5") Long userId);
}
