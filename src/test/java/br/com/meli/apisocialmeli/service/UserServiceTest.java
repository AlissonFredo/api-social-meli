package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.BuyerFollowingResponseDto;
import br.com.meli.apisocialmeli.dto.SellerFollowersResponseDto;
import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.model.FollowModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void obterTotalSeguidoresDoVendedorDeveRetornarDtoComContagemQuandoVendedorExiste() {
        // Arrange (preparação)
        Long userId = 10L;

        UserModel seller = new UserModel();
        seller.setId(userId);
        seller.setNome("Vendedor A");
        seller.setTipo(UserTipo.SELLER);

        Set<FollowModel> seguidores = new HashSet<>();
        seguidores.add(new FollowModel());
        seguidores.add(new FollowModel());
        seguidores.add(new FollowModel());
        seller.setSeguidores(seguidores);

        when(userRepository.findById(userId)).thenReturn(Optional.of(seller));

        // Act (execução)
        UserFollowersCountDto dto = userService.obterTotalSeguidoresDoVendedor(userId);

        // Assert/Verify (verificações)
        assertEquals(userId, dto.getUserId());
        assertEquals("Vendedor A", dto.getUserName());
        assertEquals(Integer.valueOf(3), dto.getFollowersCount());
        verify(userRepository).findById(userId);
    }

    @Test
    void obterTotalSeguidoresDoVendedorDeveLancar404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.obterTotalSeguidoresDoVendedor(userId));

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Usuário " + userId + " não encontrado"));
        verify(userRepository).findById(userId);
    }

    @Test
    void obterTotalSeguidoresDoVendedorDeveLancar422QuandoUsuarioEhBuyer() {
        // Arrange (preparação)
        Long userId = 2L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.BUYER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(buyer));

        // Act (execução)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.obterTotalSeguidoresDoVendedor(userId));

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("O usuário " + userId + " não é um vendedor"));
        verify(userRepository).findById(userId);
    }

    @Test
    void listarSeguidoresDoVendedorDeveLancar404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long userId = 99L;
        String order = "name_asc";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.listarSeguidoresDoVendedor(userId, order));

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Usuário " + userId + " não encontrado"));
        verify(userRepository).findById(userId);
    }

    @Test
    void listarSeguidoresDoVendedorDeveLancar422QuandoUsuarioEhBuyer() {
        // Arrange (preparação)
        Long userId = 99L;
        String order = "name_asc";

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.BUYER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(buyer));

        // Act (execução)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.listarSeguidoresDoVendedor(userId, order));

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("O usuário " + userId + " não é um vendedor"));
        verify(userRepository).findById(userId);
    }

    @Test
    void listarSeguidoresDoVendedorDeveRetornarDtoComListagemAscendenteDeSeguidoresDoVendedor() {
        // Arrange (preparação)
        Long userId = 10L;
        String order = "name_asc";

        UserModel seller = new UserModel();
        seller.setId(userId);
        seller.setNome("Vendedor A");
        seller.setTipo(UserTipo.SELLER);

        UserModel u1 = new UserModel();
        u1.setId(1L);
        u1.setNome("Carlos");

        UserModel u2 = new UserModel();
        u2.setId(2L);
        u2.setNome("Ana");

        UserModel u3 = new UserModel();
        u3.setId(3L);
        u3.setNome("Bruna");

        FollowModel f1 = new FollowModel();
        f1.setFollower(u1);

        FollowModel f2 = new FollowModel();
        f2.setFollower(u2);

        FollowModel f3 = new FollowModel();
        f3.setFollower(u3);

        Set<FollowModel> seguidores = new HashSet<>();
        seguidores.add(f1);
        seguidores.add(f2);
        seguidores.add(f3);
        seller.setSeguidores(seguidores);

        when(userRepository.findById(userId)).thenReturn(Optional.of(seller));

        // Act (execução)
        SellerFollowersResponseDto dto = userService.listarSeguidoresDoVendedor(userId, order);

        // Assert/Verify (verificações)
        assertEquals(userId, dto.getUserId());
        assertEquals("Vendedor A", dto.getUserName());
        assertEquals(Integer.valueOf(3), dto.getFollowers().size());

        assertEquals("Ana", dto.getFollowers().get(0).getUserName());
        assertEquals("Bruna", dto.getFollowers().get(1).getUserName());
        assertEquals("Carlos", dto.getFollowers().get(2).getUserName());

        verify(userRepository).findById(userId);
    }

    @Test
    void listarSeguidoresDoVendedorDeveRetornarDtoComListagemDescendenteDeSeguidoresDoVendedor() {
        // Arrange (preparação)
        Long userId = 10L;
        String order = "name_desc";

        UserModel seller = new UserModel();
        seller.setId(userId);
        seller.setNome("Vendedor A");
        seller.setTipo(UserTipo.SELLER);

        UserModel u1 = new UserModel();
        u1.setId(1L);
        u1.setNome("Carlos");

        UserModel u2 = new UserModel();
        u2.setId(2L);
        u2.setNome("Ana");

        UserModel u3 = new UserModel();
        u3.setId(3L);
        u3.setNome("Bruna");

        FollowModel f1 = new FollowModel();
        f1.setFollower(u1);

        FollowModel f2 = new FollowModel();
        f2.setFollower(u2);

        FollowModel f3 = new FollowModel();
        f3.setFollower(u3);

        Set<FollowModel> seguidores = new HashSet<>();
        seguidores.add(f1);
        seguidores.add(f2);
        seguidores.add(f3);
        seller.setSeguidores(seguidores);

        when(userRepository.findById(userId)).thenReturn(Optional.of(seller));

        // Act (execução)
        SellerFollowersResponseDto dto = userService.listarSeguidoresDoVendedor(userId, order);

        // Assert/Verify (verificações)
        assertEquals(userId, dto.getUserId());
        assertEquals("Vendedor A", dto.getUserName());
        assertEquals(Integer.valueOf(3), dto.getFollowers().size());

        assertEquals("Carlos", dto.getFollowers().get(0).getUserName());
        assertEquals("Bruna", dto.getFollowers().get(1).getUserName());
        assertEquals("Ana", dto.getFollowers().get(2).getUserName());

        verify(userRepository).findById(userId);
    }

    @Test
    void listarVendedoresSeguidosPorUsuarioDeveLancar404QuandoUsuarioNaoExiste() {
        // Arrange (preparação)
        Long userId = 99L;
        String order = "name_asc";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.listarVendedoresSeguidosPorUsuario(userId, order));

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Usuário " + userId + " não encontrado"));
        verify(userRepository).findById(userId);
    }

    @Test
    void listarVendedoresSeguidosPorUsuarioDeveLancar422QuandoUsuarioEhSeller() {
        // Arrange (preparação)
        Long userId = 99L;
        String order = "name_asc";

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.SELLER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(buyer));

        // Act (execução)
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> userService.listarVendedoresSeguidosPorUsuario(userId, order));

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("O usuário " + userId + " não é um comprador"));
        verify(userRepository).findById(userId);
    }

    @Test
    void listarVendedoresSeguidosPorUsuarioDeveRetornarDtoComListagemAscendenteDeVendedoresDoComprador() {
        // Arrange (preparação)
        Long userId = 10L;
        String order = "name_asc";

        UserModel buyer = new UserModel();
        buyer.setId(userId);
        buyer.setNome("Comprador A");
        buyer.setTipo(UserTipo.BUYER);

        UserModel u1 = new UserModel();
        u1.setId(1L);
        u1.setNome("Carlos");

        UserModel u2 = new UserModel();
        u2.setId(2L);
        u2.setNome("Ana");

        UserModel u3 = new UserModel();
        u3.setId(3L);
        u3.setNome("Bruna");

        FollowModel f1 = new FollowModel();
        f1.setSeller(u1);

        FollowModel f2 = new FollowModel();
        f2.setSeller(u2);

        FollowModel f3 = new FollowModel();
        f3.setSeller(u3);

        Set<FollowModel> following = new HashSet<>();
        following.add(f1);
        following.add(f2);
        following.add(f3);
        buyer.setSeguindo(following);

        when(userRepository.findById(userId)).thenReturn(Optional.of(buyer));

        // Act (execução)
        BuyerFollowingResponseDto dto = userService.listarVendedoresSeguidosPorUsuario(userId, order);

        // Assert/Verify (verificações)
        assertEquals(userId, dto.getUserId());
        assertEquals("Comprador A", dto.getUserName());
        assertEquals(Integer.valueOf(3), dto.getFollowed().size());

        assertEquals("Ana", dto.getFollowed().get(0).getUserName());
        assertEquals("Bruna", dto.getFollowed().get(1).getUserName());
        assertEquals("Carlos", dto.getFollowed().get(2).getUserName());

        verify(userRepository).findById(userId);
    }

    @Test
    void listarVendedoresSeguidosPorUsuarioDeveRetornarDtoComListagemDescendenteDeVendedoresDoComprador() {
        // Arrange (preparação)
        Long userId = 10L;
        String order = "name_desc";

        UserModel buyer = new UserModel();
        buyer.setId(userId);
        buyer.setNome("Comprador A");
        buyer.setTipo(UserTipo.BUYER);

        UserModel u1 = new UserModel();
        u1.setId(1L);
        u1.setNome("Carlos");

        UserModel u2 = new UserModel();
        u2.setId(2L);
        u2.setNome("Ana");

        UserModel u3 = new UserModel();
        u3.setId(3L);
        u3.setNome("Bruna");

        FollowModel f1 = new FollowModel();
        f1.setSeller(u1);

        FollowModel f2 = new FollowModel();
        f2.setSeller(u2);

        FollowModel f3 = new FollowModel();
        f3.setSeller(u3);

        Set<FollowModel> following = new HashSet<>();
        following.add(f1);
        following.add(f2);
        following.add(f3);
        buyer.setSeguindo(following);

        when(userRepository.findById(userId)).thenReturn(Optional.of(buyer));

        // Act (execução)
        BuyerFollowingResponseDto dto = userService.listarVendedoresSeguidosPorUsuario(userId, order);

        // Assert/Verify (verificações)
        assertEquals(userId, dto.getUserId());
        assertEquals("Comprador A", dto.getUserName());
        assertEquals(Integer.valueOf(3), dto.getFollowed().size());

        assertEquals("Carlos", dto.getFollowed().get(0).getUserName());
        assertEquals("Bruna", dto.getFollowed().get(1).getUserName());
        assertEquals("Ana", dto.getFollowed().get(2).getUserName());

        verify(userRepository).findById(userId);
    }
}
