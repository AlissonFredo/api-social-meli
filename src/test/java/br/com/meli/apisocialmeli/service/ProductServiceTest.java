package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.*;
import br.com.meli.apisocialmeli.model.*;
import br.com.meli.apisocialmeli.repository.PostRepository;
import br.com.meli.apisocialmeli.repository.ProductRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void cadastrarProdutoDeveLancar404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long sellerId = 21L;

        PostRequestDto reqDto = new PostRequestDto();
        reqDto.setUserId(sellerId);

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.cadastrarProduto(reqDto)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("Usuário " + sellerId + " não encontrado"));
        verify(userRepository).findById(sellerId);
    }

    @Test
    void cadastrarProdutoDeveLancar422QuandoSellerIdEhBuyerId() {
        // Arrange (preparação)
        Long buyerId = 1L;

        PostRequestDto reqDto = new PostRequestDto();
        reqDto.setUserId(buyerId);

        UserModel user = new UserModel();
        user.setTipo(UserTipo.BUYER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.cadastrarProduto(reqDto)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("O usuário " + buyerId + " não é um vendedor"));
    }

    @Test
    void cadastrarProdutoDeveRetornarPostResponseDto() {
        // Arrange (preparação)
        Long sellerId = 21L;

        UserModel user = new UserModel();
        user.setId(sellerId);
        user.setTipo(UserTipo.SELLER);
        user.setNome("Vendedor A");

        ProductRequestDto productReqDto = new ProductRequestDto();
        productReqDto.setProductName("PS2");
        productReqDto.setType(ProductType.VIDEO_GAMES);
        productReqDto.setBrand("Meli Brand");
        productReqDto.setColor("PRETO");
        productReqDto.setNotes("");

        PostRequestDto reqDto = new PostRequestDto();
        reqDto.setUserId(sellerId);
        reqDto.setCategory(100);
        reqDto.setPrice(BigDecimal.valueOf(100.90));
        reqDto.setProduct(productReqDto);

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(user));

        when(productRepository.save(any(ProductModel.class))).thenAnswer(inv -> {
            ProductModel p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        when(postRepository.save(any(PostModel.class))).thenAnswer(inv -> {
            PostModel p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });

        ArgumentCaptor<ProductModel> productCaptor = ArgumentCaptor.forClass(ProductModel.class);
        ArgumentCaptor<PostModel> postCaptor = ArgumentCaptor.forClass(PostModel.class);

        // Act (execução)
        PostResponseDto resDto = productService.cadastrarProduto(reqDto);

        // Assert/Verify (verificações)
        verify(userRepository).findById(sellerId);
        verify(productRepository).save(productCaptor.capture());
        verify(postRepository).save(postCaptor.capture());
        verifyNoMoreInteractions(userRepository, productRepository, postRepository);

        ProductModel productToSave = productCaptor.getValue();
        assertEquals("PS2", productToSave.getName());
        assertEquals(ProductType.VIDEO_GAMES, productToSave.getType());
        assertEquals("Meli Brand", productToSave.getBrand());
        assertEquals("PRETO", productToSave.getColor());
        assertEquals("", productToSave.getNotes());

        PostModel postToSave = postCaptor.getValue();
        assertEquals(100, postToSave.getCategory());
        assertEquals(0, postToSave.getPrice().compareTo(new BigDecimal("100.90")));
        assertEquals(sellerId, postToSave.getSeller().getId());
        assertNotNull(postToSave.getProduto());
        assertEquals(1L, postToSave.getProduto().getId());

        assertEquals(99L, resDto.getId());
    }

    @Test
    void obterTotalPordutosPromoVendedorDeveLancar404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long sellerId = 21L;

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.obterTotalPordutosPromoVendedor(sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("Usuário " + sellerId + " não encontrado"));
        verify(userRepository).findById(sellerId);
    }

    @Test
    void obterTotalPordutosPromoVendedorDeveLancar422QuandoSellerIdEhBuyerId() {
        // Arrange (preparação)
        Long buyerId = 1L;

        UserModel user = new UserModel();
        user.setTipo(UserTipo.BUYER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.obterTotalPordutosPromoVendedor(buyerId)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("O usuário " + buyerId + " não é um vendedor"));
    }

    @Test
    void obterTotalPordutosPromoVendedorDeveRetornarTotalProdutosPromoResponnseDto() {
        // Arrange (preparação)
        Long sellerId = 1L;

        UserModel user = new UserModel();
        user.setTipo(UserTipo.SELLER);
        user.setNome("Vendedor A");
        user.setId(sellerId);

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(user));
        when(postRepository.countBySellerIdAndHasPromoTrue(sellerId)).thenReturn(3);

        // Act (execução)
        TotalProdutosPromoResponnseDto dto = productService.obterTotalPordutosPromoVendedor(sellerId);

        // Assert/Verify (verificações)
        assertEquals(sellerId, dto.getUserId());
        assertEquals("Vendedor A", dto.getUserName());
        assertEquals(Integer.valueOf(3), dto.getPromoProductsCount());
        verify(userRepository).findById(sellerId);
        verify(postRepository).countBySellerIdAndHasPromoTrue(sellerId);
    }

    @Test
    void cadastraProdutoPromocional404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long sellerId = 21L;

        PostPromoPubRequestDto reqDto = new PostPromoPubRequestDto();
        reqDto.setUserId(sellerId);

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.cadastraProdutoPromocional(reqDto)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("Usuário " + sellerId + " não encontrado"));
        verify(userRepository).findById(sellerId);
    }

    @Test
    void cadastraProdutoPromocionalDeveLancar422QuandoSellerIdEhBuyerId() {
        // Arrange (preparação)
        Long buyerId = 1L;

        PostPromoPubRequestDto reqDto = new PostPromoPubRequestDto();
        reqDto.setUserId(buyerId);

        UserModel user = new UserModel();
        user.setTipo(UserTipo.BUYER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(user));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.cadastraProdutoPromocional(reqDto)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("O usuário " + buyerId + " não é um vendedor"));
    }

    @Test
    void cadastraProdutoPromocionalDeveRetornarPostPromoPubResponseDto() {
        // Arrange (preparação)
        Long sellerId = 21L;

        UserModel user = new UserModel();
        user.setId(sellerId);
        user.setTipo(UserTipo.SELLER);
        user.setNome("Vendedor A");

        ProductRequestDto productReqDto = new ProductRequestDto();
        productReqDto.setProductName("PS2");
        productReqDto.setType(ProductType.VIDEO_GAMES);
        productReqDto.setBrand("Meli Brand");
        productReqDto.setColor("PRETO");
        productReqDto.setNotes("");

        PostPromoPubRequestDto reqDto = new PostPromoPubRequestDto();
        reqDto.setUserId(sellerId);
        reqDto.setCategory(100);
        reqDto.setPrice(BigDecimal.valueOf(100.90));
        reqDto.setProduct(productReqDto);
        reqDto.setHasPromo(true);
        reqDto.setDiscount(BigDecimal.valueOf(0.5));

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(user));

        when(productRepository.save(any(ProductModel.class))).thenAnswer(inv -> {
            ProductModel p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        when(postRepository.save(any(PostModel.class))).thenAnswer(inv -> {
            PostModel p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });

        ArgumentCaptor<ProductModel> productCaptor = ArgumentCaptor.forClass(ProductModel.class);
        ArgumentCaptor<PostModel> postCaptor = ArgumentCaptor.forClass(PostModel.class);

        // Act (execução)
        PostPromoPubResponseDto resDto = productService.cadastraProdutoPromocional(reqDto);

        // Assert/Verify (verificações)
        verify(userRepository).findById(sellerId);
        verify(productRepository).save(productCaptor.capture());
        verify(postRepository).save(postCaptor.capture());
        verifyNoMoreInteractions(userRepository, productRepository, postRepository);

        ProductModel productToSave = productCaptor.getValue();
        assertEquals("PS2", productToSave.getName());
        assertEquals(ProductType.VIDEO_GAMES, productToSave.getType());
        assertEquals("Meli Brand", productToSave.getBrand());
        assertEquals("PRETO", productToSave.getColor());
        assertEquals("", productToSave.getNotes());

        PostModel postToSave = postCaptor.getValue();
        assertEquals(100, postToSave.getCategory());
        assertEquals(0, postToSave.getPrice().compareTo(new BigDecimal("100.90")));
        assertEquals(sellerId, postToSave.getSeller().getId());
        assertNotNull(postToSave.getProduto());
        assertEquals(1L, postToSave.getProduto().getId());
        assertEquals(true, postToSave.getHasPromo());
        assertEquals(BigDecimal.valueOf(0.5), postToSave.getDiscount());
    }
}
