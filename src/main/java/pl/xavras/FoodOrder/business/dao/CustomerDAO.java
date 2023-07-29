package pl.xavras.FoodOrder.business.dao;

import pl.xavras.FoodOrder.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {



    List<Customer> findAll();


    Customer saveCustomer(Customer customer);


    Optional<Customer> findByEmail(String email);

    Customer findLoggedCustomer();
}
