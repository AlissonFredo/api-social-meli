package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.SellerFollowersResponseDto;
import br.com.meli.apisocialmeli.dto.UserDto;
import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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

    public SellerFollowersResponseDto listarSeguidoresDoVendedor(Long userId) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuário " + userId + " não encontrado"));

        if (userModel.getTipo() == UserTipo.BUYER) {
            throw new IllegalStateException("O usuário " + userId + " não é um vendedor");
        }

        List<UserDto> followers = userModel.getSeguidores()
                .stream()
                .map(f -> {
                    UserDto userDto = new UserDto();
                    userDto.setUserId(f.getSeller().getId());
                    userDto.setUserName(f.getSeller().getNome());
                    return userDto;
                })
                .collect(Collectors.toList());

        SellerFollowersResponseDto sellerFollowersResponseDto = new SellerFollowersResponseDto();
        sellerFollowersResponseDto.setUserId(userModel.getId());
        sellerFollowersResponseDto.setUserName(userModel.getNome());
        sellerFollowersResponseDto.setFollowers(followers);

        return sellerFollowersResponseDto;
    }
}
