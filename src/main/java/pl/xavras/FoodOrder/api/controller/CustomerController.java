package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.xavras.FoodOrder.api.dto.CustomerDTO;
import pl.xavras.FoodOrder.api.dto.mapper.CustomerMapper;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.domain.Customer;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class CustomerController {



    public static final String CUSTOMERS = "/customers";
    public static final String CUSTOMERS_ADD = "/customers/add";
    public static final String CUSTOMER_BY_EMAIL = "/customer/show/{email}";
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping(CUSTOMERS)
    public String customers(Model model) {
        Customer activeCustomer = customerService.activeCustomer();
        List<CustomerDTO> allCustomers = customerService.findAll().stream()
                .map(customerMapper::map).toList();
        model.addAttribute("customers", allCustomers);
        model.addAttribute("customerDTO", new CustomerDTO());
        model.addAttribute("activeCustomer", activeCustomer);
        return "customers";
    }
    @PostMapping(CUSTOMERS_ADD)
    public String addCustomer(
            @ModelAttribute("customerDTO") CustomerDTO customerDTO
    ) {
        Customer newCustomer = customerMapper.map(customerDTO);
        customerService.saveCustomer(newCustomer);

        return "redirect:customers";
    }

    @GetMapping(CUSTOMER_BY_EMAIL)
    public String showCustomerDetails(@PathVariable String email, Model model) {

        CustomerDTO customerDetails = customerMapper.map(customerService.findByEmail(email));

        model.addAttribute("customer", customerDetails);

        return "customerDetails";
    }
}

