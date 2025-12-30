package br.com.meli.apisocialmeli.repository;

import br.com.meli.apisocialmeli.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
    List<PostModel> findBySellerIdInAndCreatedAtBetween(List<Long> sellerIds, LocalDateTime start, LocalDateTime end);
}
