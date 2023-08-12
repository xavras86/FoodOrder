package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.CustomerDAO;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.infrastructure.database.entity.CustomerEntity;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.CustomerJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.CustomerEntityMapper;
import pl.xavras.FoodOrder.infrastructure.security.UserJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CustomerRepository implements CustomerDAO {

    private final CustomerJpaRepository customerJpaRepository;

    private final CustomerEntityMapper customerEntityMapper;

    private final UserJpaRepository userRepository;




    @Override
    public List<Customer> findAll(){
        return customerJpaRepository.findAll().stream()
                .map(customerEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        CustomerEntity toSave = customerEntityMapper.mapToEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(toSave);
        return customerEntityMapper.mapFromEntity(saved);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerJpaRepository.findByEmail(email)
                .map(customerEntityMapper::mapFromEntity);
    }
    @Override
    public Customer findLoggedCustomer(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var loggedEmail = userRepository.findByUserName(username).getEmail();
        return customerJpaRepository.findByEmail(loggedEmail)
                .map(customerEntityMapper::mapFromEntity)
                .orElseThrow(() -> new RuntimeException("something went terribly wrong with security :( no customer related to current user email [%s] "
                        .formatted(loggedEmail)));
    }


}
