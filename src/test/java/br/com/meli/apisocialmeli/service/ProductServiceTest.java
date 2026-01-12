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
import java.time.LocalDateTime;
import java.util.*;

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

    @Test
    void getFollowedSuppliersRecentProducts404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long sellerId = 21L;
        String order = "date_asc";

        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.getFollowedSuppliersRecentProducts(sellerId, order)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("Usuário " + sellerId + " não encontrado"));
        verify(userRepository).findById(sellerId);
    }

    @Test
    void getFollowedSuppliersRecentProductsDeveLancar422QuandoBuyerIdEhSellerId() {
        // Arrange (preparação)
        Long sellerId = 1L;
        String order = "date_asc";

        UserModel user = new UserModel();
        user.setTipo(UserTipo.SELLER);

        when(userRepository.findById(sellerId)).thenReturn(Optional.of(user));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> productService.getFollowedSuppliersRecentProducts(sellerId, order)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertNotNull(ex.getReason());
        assertTrue(ex.getReason().contains("O usuário " + sellerId + " não é um comprador"));
    }

    @Test
    void getFollowedSuppliersRecentProductsDeveRetornarPostsFollowingLastTwoWeeksResponseDtoOrderDataAsc() {
        // Arrange (preparação)
        Long buyerId = 1L;
        String order = "date_asc";

        UserModel buyer = new UserModel();
        buyer.setId(buyerId);
        buyer.setNome("Comprador X");
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller1 = new UserModel();
        seller1.setId(10L);
        seller1.setNome("Vendedor A");
        seller1.setTipo(UserTipo.SELLER);

        UserModel seller2 = new UserModel();
        seller2.setId(20L);
        seller2.setNome("Vendedor B");
        seller2.setTipo(UserTipo.SELLER);

        FollowModel f1 = new FollowModel();
        f1.setSeller(seller1);

        FollowModel f2 = new FollowModel();
        f2.setSeller(seller2);

        Set<FollowModel> following = new HashSet<>();
        following.add(f1);
        following.add(f2);
        buyer.setSeguindo(following);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));

        LocalDateTime now = LocalDateTime.now();

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setName("PS2");
        product.setType(ProductType.VIDEO_GAMES);
        product.setBrand("Meli Brand");
        product.setColor("PRETO");

        PostModel p1 = new PostModel();
        p1.setId(100L);
        p1.setSeller(seller1);
        p1.setCreatedAt(now.minusDays(12));
        p1.setProduto(product);

        PostModel p2 = new PostModel();
        p2.setId(101L);
        p2.setSeller(seller2);
        p2.setCreatedAt(now.minusDays(3));
        p2.setProduto(product);

        PostModel p3 = new PostModel();
        p3.setId(103L);
        p3.setSeller(seller1);
        p3.setCreatedAt(now.minusDays(8));
        p3.setProduto(product);

        when(postRepository.findBySellerIdInAndCreatedAtBetween(
                anyList(),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(Arrays.asList(p2, p1, p3));

        // Act (execução)
        PostsFollowingLastTwoWeeksResponseDto dto = productService.getFollowedSuppliersRecentProducts(buyerId, order);

        // Assert/Verify (verificações)
        assertNotNull(dto);
        assertEquals(buyerId, dto.getUserId());

        List<PostResponseDto> postsDto = dto.getPosts();
        assertEquals(3, postsDto.size());

        assertEquals(p1.getCreatedAt(), postsDto.get(0).getCreatedAt());
        assertEquals(p3.getCreatedAt(), postsDto.get(1).getCreatedAt());
        assertEquals(p2.getCreatedAt(), postsDto.get(2).getCreatedAt());

        verify(userRepository).findById(buyerId);
        verify(postRepository).findBySellerIdInAndCreatedAtBetween(
                argThat(ids -> ids != null && ids.size() == 2 && ids.containsAll(List.of(10L, 20L))),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
        verifyNoMoreInteractions(userRepository, postRepository);
    }

    @Test
    void getFollowedSuppliersRecentProductsDeveRetornarPostsFollowingLastTwoWeeksResponseDtoOrderDataDesc() {
        // Arrange (preparação)
        Long buyerId = 1L;
        String order = "date_desc";

        UserModel buyer = new UserModel();
        buyer.setId(buyerId);
        buyer.setNome("Comprador X");
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller1 = new UserModel();
        seller1.setId(10L);
        seller1.setNome("Vendedor A");
        seller1.setTipo(UserTipo.SELLER);

        UserModel seller2 = new UserModel();
        seller2.setId(20L);
        seller2.setNome("Vendedor B");
        seller2.setTipo(UserTipo.SELLER);

        FollowModel f1 = new FollowModel();
        f1.setSeller(seller1);

        FollowModel f2 = new FollowModel();
        f2.setSeller(seller2);

        Set<FollowModel> following = new HashSet<>();
        following.add(f1);
        following.add(f2);
        buyer.setSeguindo(following);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));

        LocalDateTime now = LocalDateTime.now();

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setName("PS2");
        product.setType(ProductType.VIDEO_GAMES);
        product.setBrand("Meli Brand");
        product.setColor("PRETO");

        PostModel p1 = new PostModel();
        p1.setId(100L);
        p1.setSeller(seller1);
        p1.setCreatedAt(now.minusDays(12));
        p1.setProduto(product);

        PostModel p2 = new PostModel();
        p2.setId(101L);
        p2.setSeller(seller2);
        p2.setCreatedAt(now.minusDays(3));
        p2.setProduto(product);

        PostModel p3 = new PostModel();
        p3.setId(103L);
        p3.setSeller(seller1);
        p3.setCreatedAt(now.minusDays(8));
        p3.setProduto(product);

        when(postRepository.findBySellerIdInAndCreatedAtBetween(
                anyList(),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(Arrays.asList(p2, p1, p3));

        // Act (execução)
        PostsFollowingLastTwoWeeksResponseDto dto = productService.getFollowedSuppliersRecentProducts(buyerId, order);

        // Assert/Verify (verificações)
        assertNotNull(dto);
        assertEquals(buyerId, dto.getUserId());

        List<PostResponseDto> postsDto = dto.getPosts();
        assertEquals(3, postsDto.size());

        assertEquals(p1.getCreatedAt(), postsDto.get(2).getCreatedAt());
        assertEquals(p3.getCreatedAt(), postsDto.get(1).getCreatedAt());
        assertEquals(p2.getCreatedAt(), postsDto.get(0).getCreatedAt());

        verify(userRepository).findById(buyerId);
        verify(postRepository).findBySellerIdInAndCreatedAtBetween(
                argThat(ids -> ids != null && ids.size() == 2 && ids.containsAll(List.of(10L, 20L))),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
        verifyNoMoreInteractions(userRepository, postRepository);
    }
}
