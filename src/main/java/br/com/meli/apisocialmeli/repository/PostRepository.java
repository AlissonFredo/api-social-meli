package br.com.meli.apisocialmeli.repository;

import br.com.meli.apisocialmeli.model.PostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostModel, Long> {
}
