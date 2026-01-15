package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.model.FollowModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.FollowRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class FollowService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public void seguirVendedor(Long userId, Long userIdToFollow) {
        UserModel buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + userId + " não encontrado"));

        UserModel seller = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + userIdToFollow + " não encontrado"));

        if (buyer.getTipo() != UserTipo.BUYER) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário " + userId + " não é um comprador");
        }

        if (seller.getTipo() != UserTipo.SELLER) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário " + userIdToFollow + " não é um vendedor");
        }

        FollowModel followModel = new FollowModel();
        followModel.setFollower(buyer);
        followModel.setSeller(seller);

        followRepository.save(followModel);
    }

    @Transactional
    public void unfollowSeller(Long buyerId, Long sellerId) {
        UserModel buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + buyerId + " não encontrado"));

        UserModel seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + sellerId + " não encontrado"));

        if (buyer.getTipo() != UserTipo.BUYER) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário " + buyerId + " não é um comprador");
        }

        if (seller.getTipo() != UserTipo.SELLER) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário " + sellerId + " não é um vendedor");
        }

        Long deletados = followRepository.deleteByFollowerIdAndSellerId(buyerId, sellerId);

        if (deletados == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow não encontrado para comprador = " + buyerId + " e vendedor = " + sellerId);
        }
    }
}
