package br.com.meli.apisocialmeli.repository;

import br.com.meli.apisocialmeli.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
