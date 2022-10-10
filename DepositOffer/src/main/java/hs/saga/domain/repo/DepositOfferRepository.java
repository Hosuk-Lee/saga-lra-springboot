package hs.saga.domain.repo;

import hs.saga.domain.entity.DepositOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepositOfferRepository extends JpaRepository<DepositOfferEntity,Long> {
    Optional<DepositOfferEntity> findByCustomerId(String customerId);
}
