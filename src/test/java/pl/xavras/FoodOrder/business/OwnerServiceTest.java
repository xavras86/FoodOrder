package pl.xavras.FoodOrder.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.business.dao.OwnerDAO;
import pl.xavras.FoodOrder.domain.Owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static pl.xavras.FoodOrder.util.DomainFixtures.someOwner1;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private OwnerDAO ownerDAO;



    @Test
    public void testSaveOwner() {
        // Given
        Owner owner = someOwner1().withName("Jan").withEmail("jan@owner.pl");
        when(ownerDAO.saveOwner(owner)).thenReturn(owner);

        // When
        Owner result = ownerService.saveOwner(owner);

        // Then
        assertNotNull(result);
        assertEquals("jan@owner.pl", result.getEmail());
        verify(ownerDAO, times(1)).saveOwner(owner);
    }

    @Test
    public void testActiveCustomer() {
        // Given
        Owner owner = someOwner1().withName("Jan").withEmail("jan@owner.pl");
        when(ownerDAO.findLoggedOwner()).thenReturn(owner);

        // When
        Owner result = ownerService.activeOwner();

        // Then
        assertNotNull(result);
        assertEquals("Jan", result.getName());
    }
}