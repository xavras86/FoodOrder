package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.OwnerDAO;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OwnerJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.CustomerEntityMapper;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.OwnerEntityMapper;
import pl.xavras.FoodOrder.infrastructure.security.UserRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OwnerRepository implements OwnerDAO {

    private final OwnerJpaRepository ownerJpaRepository;

    private final OwnerEntityMapper ownerEntityMapper;

    private final UserRepository userRepository;


    @Override
    public Optional<Owner> findByEmail(String email) {
        return ownerJpaRepository.findByEmail(email)
                .map(ownerEntityMapper::mapFromEntity);
    }
    @Override
    public Owner findLoggedOwner(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var loggedEmail = userRepository.findByUserName(username).getEmail();
        return ownerJpaRepository.findByEmail(loggedEmail)
                .map(ownerEntityMapper::mapFromEntity)
                .orElseThrow(() -> new RuntimeException("something went terribly wrong with security :( no owner related to current user email [%s] "
                        .formatted(loggedEmail)));
    }


}
