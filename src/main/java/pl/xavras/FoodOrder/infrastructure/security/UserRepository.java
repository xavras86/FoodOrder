package pl.xavras.FoodOrder.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.UserDAO;
import pl.xavras.FoodOrder.domain.User;

import java.util.Set;

@Repository
@AllArgsConstructor
public class UserRepository implements UserDAO {

    public final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final RoleRepository roleRepository;

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



