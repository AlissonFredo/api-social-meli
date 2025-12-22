package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.UserFollowersCountDto;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserFollowersCountDto obterTotalSeguidoresDoVendedor(Long userId) {
        UserModel userModel = userRepository.getReferenceById(userId);
        UserFollowersCountDto userFollowersCountDto = new UserFollowersCountDto();
        userFollowersCountDto.setUserId(userModel.getId());
        userFollowersCountDto.setUserName(userModel.getNome());
        userFollowersCountDto.setFollowersCount(userModel.getSeguidores().size());
        return userFollowersCountDto;
    }
}
