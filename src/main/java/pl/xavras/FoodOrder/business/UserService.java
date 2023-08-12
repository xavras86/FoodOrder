package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.UserDAO;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.User;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserDAO userDAO;
    private final OwnerService ownerService;
    private final CustomerService customerService;


    @Transactional
    public void registerNewUser(User user) {
        userDAO.registerNewUser(user);
        if ("OWNER".equals(user.getRole())) {
            Owner owner = Owner.builder()
                    .name(user.getName())
                    .surname(user.getSurname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();
            ownerService.saveOwner(owner);
        }
        if ("CUSTOMER".equals(user.getRole())) {
            Customer customer = Customer.builder()
                    .name(user.getName())
                    .surname(user.getSurname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();
            customerService.saveCustomer(customer);
        }
    }
}