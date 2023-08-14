package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.CustomerDAO;
import pl.xavras.FoodOrder.domain.Customer;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerDAO customerDAO;

    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    public Customer findByEmail(String email) {
        return customerDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer with email [%s] doest don't exists".formatted(email)));

    }

    @Transactional
    public void saveCustomer(Customer customer) {
        customerDAO.saveCustomer(customer);
    }

    public Customer activeCustomer(){
        return customerDAO.findLoggedCustomer();
    }

}