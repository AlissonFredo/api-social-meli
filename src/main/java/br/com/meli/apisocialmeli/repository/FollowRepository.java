package br.com.meli.apisocialmeli.repository;

import br.com.meli.apisocialmeli.model.FollowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowModel, Long> {
    Long deleteByFollowerIdAndSellerId(Long followerId, Long sellerId);
}
