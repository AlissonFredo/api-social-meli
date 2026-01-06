package br.com.meli.apisocialmeli.service;

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
    void deveRetornarDtoComContagemQuandoVendedorExiste() {
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
    void deveLancar404QuandoUsuarioNaoExiste() {
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
    void deveLancar422QuandoUsuarioEhBuyer() {
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
}
