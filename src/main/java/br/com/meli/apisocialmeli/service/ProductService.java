package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.PostRequestDto;
import br.com.meli.apisocialmeli.dto.PostResponseDto;
import br.com.meli.apisocialmeli.dto.PostsFollowingLastTwoWeeksResponseDto;
import br.com.meli.apisocialmeli.model.*;
import br.com.meli.apisocialmeli.repository.PostRepository;
import br.com.meli.apisocialmeli.repository.ProductRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PostRepository postRepository;

    public PostResponseDto cadastrarProduto(PostRequestDto productDto) {
        UserModel seller = userRepository.findById(productDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + productDto.getUserId() + " não encontrado"));

        if (seller.getTipo() == UserTipo.BUYER) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário " + productDto.getUserId() + " não é um vendedor");
        }

        ProductModel productModel = new ProductModel();
        productModel.setName(productDto.getProduct().getProductName());
        productModel.setType(productDto.getProduct().getType());
        productModel.setBrand(productDto.getProduct().getBrand());
        productModel.setColor(productDto.getProduct().getColor());
        productModel.setNotes(productDto.getProduct().getNotes());

        ProductModel productSalved = productRepository.save(productModel);

        PostModel post = new PostModel();
        post.setCategory(productDto.getCategory());
        post.setPrice(productDto.getPrice());
        post.setSeller(seller);
        post.setProduto(productSalved);

        PostModel postSalved = postRepository.save(post);

        return new PostResponseDto(postSalved);
    }

    public PostsFollowingLastTwoWeeksResponseDto getFollowedSuppliersRecentProducts(Long userId) {
        UserModel buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário " + userId + " não encontrado"));

        if (buyer.getTipo() == UserTipo.SELLER) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário " + userId + " não é um comprador");
        }

        List<Long> sellerIds = buyer.getSeguindo()
                .stream()
                .map(follow -> follow.getSeller().getId())
                .toList();

        LocalDateTime dayNow = LocalDateTime.now();
        LocalDateTime thirteenDaysAgo = dayNow.minusDays(14);

        List<PostModel> posts = postRepository.findBySellerIdInAndCreatedAtBetween(sellerIds, thirteenDaysAgo, dayNow);

        List<PostResponseDto> postsDto = posts
                .stream()
                .map(PostResponseDto::new)
                .toList();

        return new PostsFollowingLastTwoWeeksResponseDto(buyer.getId(), postsDto);
    }
}

