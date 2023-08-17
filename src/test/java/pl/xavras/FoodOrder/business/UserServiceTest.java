package pl.xavras.FoodOrder.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.business.dao.UserDAO;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.Owner;
import pl.xavras.FoodOrder.domain.User;
import pl.xavras.FoodOrder.util.DomainFixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private OwnerService ownerService;

    @Mock
    private CustomerService customerService;

    @Test
    public void thatRegisterNewUserOwner() {
        User user = DomainFixtures.someUser1().withRole("OWNER");


        Owner owner = Owner.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();

        when(userDAO.registerNewUser(any(User.class))).thenReturn(user);

        userService.registerNewUser(user);

        verify(ownerService).saveOwner(owner);
        verify(customerService, never()).saveCustomer(any(Customer.class));
    }

    @Test
    public void testRegisterNewUserCustomer() {
        User user = DomainFixtures.someUser1().withRole("CUSTOMER");

        Customer customer = Customer.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();

        when(userDAO.registerNewUser(any(User.class))).thenReturn(user);

        userService.registerNewUser(user);

        verify(customerService).saveCustomer(customer);
        verify(ownerService, never()).saveOwner(any(Owner.class));
    }

    @Test
    public void testRegisterNewUserForOtherRole() {
        User user = DomainFixtures.someUser1().withRole("SOME_OTHER_ROLE");

        when(userDAO.registerNewUser(any(User.class))).thenReturn(user);

        userService.registerNewUser(user);

        verify(ownerService, never()).saveOwner(any(Owner.class));
        verify(customerService, never()).saveCustomer(any(Customer.class));
    }
}