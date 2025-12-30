package br.com.meli.apisocialmeli.service;

import br.com.meli.apisocialmeli.dto.PostRequestDto;
import br.com.meli.apisocialmeli.dto.PostResponseDto;
import br.com.meli.apisocialmeli.model.PostModel;
import br.com.meli.apisocialmeli.model.ProductModel;
import br.com.meli.apisocialmeli.model.UserModel;
import br.com.meli.apisocialmeli.model.UserTipo;
import br.com.meli.apisocialmeli.repository.PostRepository;
import br.com.meli.apisocialmeli.repository.ProductRepository;
import br.com.meli.apisocialmeli.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("Usuário " + productDto.getUserId() + " não encontrado"));

        if (seller.getTipo() == UserTipo.BUYER) {
            throw new IllegalStateException("O usuário " + productDto.getUserId() + " não é um vendedor");
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
}
