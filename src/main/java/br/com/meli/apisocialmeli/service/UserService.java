package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserFollowersCountDto obterTotalSeguidoresDoVendedor(Long userId) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário " + userId + " não encontrado"));

        if (userModel.getTipo() == UserTipo.BUYER) {
            throw new IllegalStateException("O usuário " + userId + " não é um vendedor");
        }

        UserFollowersCountDto userFollowersCountDto = new UserFollowersCountDto();
        userFollowersCountDto.setUserId(userModel.getId());
        userFollowersCountDto.setUserName(userModel.getNome());
        userFollowersCountDto.setFollowersCount(userModel.getSeguidores().size());
        return userFollowersCountDto;
    }
}
