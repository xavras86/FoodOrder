package pl.xavras.FoodOrder.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.OwnerDAO;
import pl.xavras.FoodOrder.business.dao.UserDAO;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.User;
import pl.xavras.FoodOrder.infrastructure.database.entity.OwnerEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.OwnerJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.OwnerEntityMapper;

import java.util.Optional;
import java.util.Set;

@Repository
@AllArgsConstructor
public class UserRepository implements UserDAO {

    private final UserJpaRepository userJpaRepository;
    private final RoleRepository roleRepository;

    public final PasswordEncoder passwordEncoder;


    @Override
    public void registerNewUser(User user) {
        RoleEntity role = roleRepository.findByRole(user.getRole());
        UserEntity userToSave = UserEntity.builder()
                .userName(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .active(true)
                .roles(Set.of(role))
                .build();
        userJpaRepository.save(userToSave);


    }
}



