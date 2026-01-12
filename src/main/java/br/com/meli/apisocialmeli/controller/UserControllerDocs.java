package br.com.meli.apisocialmeli.controller;

import br.com.meli.apisocialmeli.dto.BuyerFollowingResponseDto;
import br.com.meli.apisocialmeli.dto.SellerFollowersResponseDto;
import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Users", description = "Operações de users")
public interface UserControllerDocs {
    @Operation(
            summary = "Seguir vendedor",
            description = "Permite que um usuário com tipo BUYER siga um usuário com tipo SELLER",
            operationId = "seguirVendedor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Follow criado com sucesso",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Buyer inexistente", value = "{\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 10 não encontrado\",\"path\":\"/users/10/follow/5\",\"timestamp\":\"2026-01-12T10:00:00Z\"}"),
                                    @ExampleObject(name = "Seller inexistente", value = "{\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/users/10/follow/5\",\"timestamp\":\"2026-01-12T10:00:00Z\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Regra de negócio violada (tipo de usuário inválido)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Não é comprador", value = "{\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 10 não é um comprador\",\"path\":\"/users/10/follow/5\",\"timestamp\":\"2026-01-12T10:00:00Z\"}"),
                                    @ExampleObject(name = "Não é vendedor", value = "{\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/users/10/follow/5\",\"timestamp\":\"2026-01-12T10:00:00Z\"}")
                            }
                    )
            )
    })
    ResponseEntity<?> seguirVendedor(
            @Parameter(description = "ID do comprador (deve ser do tipo BUYER)", example = "10", schema = @Schema(type = "integer", format = "int64")) Long userId,
            @Parameter(description = "ID do vendedor a ser seguido (deve ser do tipo SELLER)", example = "5", schema = @Schema(type = "integer", format = "int64")) Long userIdToFollow
    );

    @Operation(
            summary = "Obter total de seguidores do vendedor",
            description = "Retorna o total de seguidores de um usuário do tipo SELLER",
            operationId = "obterTotalSeguidoresDoVendedor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserFollowersCountDto.class),
                            examples = @ExampleObject(name = "Exemplo de resposta", value = "{\"userId\":5,\"userName\":\"Loja XPTO\",\"followersCount\":123}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Usuário inexistente", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/users/5/followers/count\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um vendedor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Não é vendedor", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/users/5/followers/count\"}")
                    )
            )
    })
    ResponseEntity<?> obterTotalSeguidoresDoVendedor(@Parameter(description = "ID do vendedor (deve ser do tipo SELLER)", example = "5") Long userId);

    @Operation(
            summary = "Listar seguidores do vendedor",
            description = "Retorna a lista de seguidores (BUYERs) de um usuário do tipo SELLER, com opção de ordenação por nome.",
            operationId = "listarSeguidoresDoVendedor"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SellerFollowersResponseDto.class),
                            examples = @ExampleObject(name = "Exemplo de resposta", value = "{\"userId\":5,\"userName\":\"Loja XPTO\",\"followers\":[{\"userId\":101,\"userName\":\"Ana\"},{\"userId\":102,\"userName\":\"Bruno\"}]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Usuário inexistente", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/users/5/followers/list\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um vendedor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Não é vendedor", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/users/5/followers/list\"}")
                    )
            )
    })
    ResponseEntity<?> listarSeguidoresDoVendedor(
            @Parameter(description = "ID do vendedor (deve ser do tipo SELLER)", example = "5") Long userId,
            @Parameter(description = "Ordenação dos seguidores por nome", example = "name_asc", schema = @Schema(type = "string", allowableValues = {"name_asc", "name_desc"}, defaultValue = "name_asc")) String order
    );

    @Operation(
            summary = "Listar vendedores seguidos por um comprador",
            description = "Retorna a lista de vendedores (SELLERs) seguidos por um usuário do tipo BUYER, com opção de ordenação por nome.",
            operationId = "listarVendedoresSeguidosPorUsuario"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BuyerFollowingResponseDto.class),
                            examples = @ExampleObject(name = "Exemplo de resposta", value = "{\"userId\":10,\"userName\":\"Ana\",\"followed\":[{\"userId\":201,\"userName\":\"Loja XPTO\"},{\"userId\":202,\"userName\":\"Mercado 123\"}]}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Usuário inexistente", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 10 não encontrado\",\"path\":\"/users/10/followed/list\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "O usuário informado não é um comprador",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Não é comprador", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 10 não é um comprador\",\"path\":\"/users/10/followed/list\"}")
                    )
            )
    })
    ResponseEntity<?> listarVendedoresSeguidosPorUsuario(
            @Parameter(description = "ID do comprador (deve ser do tipo BUYER)", example = "10") Long userId,
            @Parameter(description = "Ordenação por nome", example = "name_asc", schema = @Schema(type = "string", allowableValues = {"name_asc", "name_desc"}, defaultValue = "name_asc")) String order
    );

    @Operation(
            summary = "Deixar de seguir vendedor",
            description = "Permite que um usuário do tipo BUYER pare de seguir um usuário do tipo SELLER",
            operationId = "unfollowSeller"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Unfollow realizado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário/Follow não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Buyer inexistente", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 10 não encontrado\",\"path\":\"/users/10/unfollow/5\"}"),
                                    @ExampleObject(name = "Seller inexistente", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuário 5 não encontrado\",\"path\":\"/users/10/unfollow/5\"}"),
                                    @ExampleObject(name = "Follow não encontrado", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":404,\"error\":\"Not Found\",\"message\":\"Follow não encontrado para comprador = 10 e vendedor = 5\",\"path\":\"/users/10/unfollow/5\"}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Regra de negócio violada (tipo inválido)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Não é comprador", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 10 não é um comprador\",\"path\":\"/users/10/unfollow/5\"}"),
                                    @ExampleObject(name = "Não é vendedor", value = "{\"timestamp\":\"2026-01-12T10:00:00Z\",\"status\":422,\"error\":\"Unprocessable Entity\",\"message\":\"O usuário 5 não é um vendedor\",\"path\":\"/users/10/unfollow/5\"}")
                            }
                    )
            )
    })
    ResponseEntity<?> unfollowSeller(
            @Parameter(description = "ID do comprador (deve ser do tipo BUYER)", example = "10") Long userId,
            @Parameter(description = "ID do vendedor a deixar de seguir (deve ser do tipo SELLER)", example = "5") Long userIdTounfollow
    );
}
