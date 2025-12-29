package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.model.FollowModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.FollowRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FollowService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public void seguirVendedor(Long userId, Long userIdToFollow) {
        UserModel buyer = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário " + userId + " não encontrado"));

        UserModel seller = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new NoSuchElementException("Usuário " + userIdToFollow + " não encontrado"));

        if(buyer.getTipo() != UserTipo.BUYER) {
            throw new IllegalStateException("O usuário " + userId + " não é um comprador");
        }

        if(seller.getTipo() != UserTipo.SELLER) {
            throw new IllegalStateException("O usuário " + userIdToFollow + " não é um vendedor");
        }

        FollowModel followModel = new FollowModel();
        followModel.setFollower(buyer);
        followModel.setSeller(seller);

        followRepository.save(followModel);
    }

    public void unfollowSeller(Long buyerId, Long sellerId) {
        UserModel buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new NoSuchElementException("Usuário " + buyerId + " não encontrado"));

        UserModel seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new NoSuchElementException("Usuário " + sellerId + " não encontrado"));

        if(buyer.getTipo() != UserTipo.BUYER) {
            throw new IllegalStateException("O usuário " + buyerId + " não é um comprador");
        }

        if(seller.getTipo() != UserTipo.SELLER) {
            throw new IllegalStateException("O usuário " + sellerId + " não é um vendedor");
        }

        Long deletados = followRepository.deleteByFollowerIdAndSellerId(buyerId, sellerId);

        if (deletados == 0) {
            throw new NoSuchElementException("Follow não encontrado para comprador = " + buyerId + " e vendedor = " + sellerId);
        }
    }
}
