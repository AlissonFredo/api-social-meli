package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.model.FollowModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.FollowRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
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
}
