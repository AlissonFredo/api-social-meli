package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.model.FollowModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.FollowRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    FollowRepository followRepository;

    @InjectMocks
    FollowService followService;

    @Test
    void seguirVendedorDeveLancar404QuandoUsuarioBuyerNaoExiste() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 21L;

        when(userRepository.findById(buyerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.seguirVendedor(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("Usuário " + buyerId + " não encontrado"));
        verify(userRepository).findById(buyerId);
    }

    @Test
    void seguirVendedorDeveLancar404QuandoUsuarioSellerNaoExiste() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 21L;

        UserModel buyer = new UserModel();
        buyer.setId(buyerId);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.seguirVendedor(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("Usuário " + sellerId + " não encontrado"));
        verify(userRepository).findById(buyerId);
        verify(userRepository).findById(sellerId);
    }

    @Test
    void seguirVendedorDeveLancar422QuandoBuyerIdEhUmSellerId() {
        // Arrange (preparação)
        Long buyerId = 22L;
        Long sellerId = 21L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.SELLER);

        UserModel seller = new UserModel();
        seller.setTipo(UserTipo.SELLER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.seguirVendedor(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("O usuário " + buyerId + " não é um comprador"));
        verify(userRepository).findById(buyerId);
        verify(userRepository).findById(sellerId);
    }

    @Test
    void seguirVendedorDeveLancar422QuandoSellerIdEhUmBuyerId() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 2L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller = new UserModel();
        seller.setTipo(UserTipo.BUYER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.seguirVendedor(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("O usuário " + sellerId + " não é um vendedor"));
        verify(userRepository).findById(buyerId);
        verify(userRepository).findById(sellerId);
    }

    @Test
    void seguirVendedorDeveSalvarFollow() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 21L;

        UserModel buyer = new UserModel();
        buyer.setId(buyerId);
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller = new UserModel();
        seller.setId(sellerId);
        seller.setTipo(UserTipo.SELLER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Act (execução)
        assertDoesNotThrow(() -> followService.seguirVendedor(buyerId, sellerId));

        // Assert/Verify (verificações)
        ArgumentCaptor<FollowModel> captor = ArgumentCaptor.forClass(FollowModel.class);
        verify(followRepository, times(1)).save(captor.capture());

        FollowModel salvo = captor.getValue();
        assertSame(buyer, salvo.getFollower());
        assertSame(seller, salvo.getSeller());
    }

    @Test
    void unfollowSellerDeveLancar404QuandoUsuarioBuyerNaoExiste() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 21L;

        when(userRepository.findById(buyerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.unfollowSeller(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("Usuário " + buyerId + " não encontrado"));
        verify(userRepository).findById(buyerId);
    }

    @Test
    void unfollowSellerDeveLancar404QuandoUsuarioSellerNaoExiste() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 21L;

        UserModel buyer = new UserModel();
        buyer.setId(buyerId);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.empty());

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.unfollowSeller(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("Usuário " + sellerId + " não encontrado"));
        verify(userRepository).findById(sellerId);
    }

    @Test
    void unfollowSellerDeveLancar422QuandoBuyerIdEhUmSellerId() {
        // Arrange (preparação)
        Long buyerId = 22L;
        Long sellerId = 21L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.SELLER);

        UserModel seller = new UserModel();
        seller.setTipo(UserTipo.SELLER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.unfollowSeller(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("O usuário " + buyerId + " não é um comprador"));
        verify(userRepository).findById(buyerId);
        verify(userRepository).findById(sellerId);
    }

    @Test
    void unfollowSellerDeveLancar422QuandoSellerIdEhUmBuyerId() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 2L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller = new UserModel();
        seller.setTipo(UserTipo.BUYER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.unfollowSeller(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(422, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("O usuário " + sellerId + " não é um vendedor"));
        verify(userRepository).findById(buyerId);
        verify(userRepository).findById(sellerId);
    }

    @Test
    void unfollowSellerDeveRetornarUm() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 22L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller = new UserModel();
        seller.setTipo(UserTipo.SELLER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(followRepository.deleteByFollowerIdAndSellerId(buyerId, sellerId)).thenReturn(1L);

        // Act (execução)
        assertDoesNotThrow(() -> followService.unfollowSeller(buyerId, sellerId));

        // Assert/Verify (verificações)
        verify(followRepository, times(1)).deleteByFollowerIdAndSellerId(buyerId, sellerId);
    }

    @Test
    void unfollowSellerDeveLancar404QuandoUnFollowFalhar() {
        // Arrange (preparação)
        Long buyerId = 1L;
        Long sellerId = 22L;

        UserModel buyer = new UserModel();
        buyer.setTipo(UserTipo.BUYER);

        UserModel seller = new UserModel();
        seller.setTipo(UserTipo.SELLER);

        when(userRepository.findById(buyerId)).thenReturn(Optional.of(buyer));
        when(userRepository.findById(sellerId)).thenReturn(Optional.of(seller));
        when(followRepository.deleteByFollowerIdAndSellerId(buyerId, sellerId)).thenReturn(0L);

        // Act (execução)
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> followService.unfollowSeller(buyerId, sellerId)
        );

        // Assert/Verify (verificações)
        assertEquals(404, ex.getStatusCode().value());
        assert ex.getReason() != null;
        assertTrue(ex.getReason().contains("Follow não encontrado para comprador = " + buyerId + " e vendedor = " + sellerId));
        verify(followRepository, times(1)).deleteByFollowerIdAndSellerId(buyerId, sellerId);
    }
}
