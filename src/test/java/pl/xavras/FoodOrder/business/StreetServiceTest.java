package pl.xavras.FoodOrder.business;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pl.xavras.FoodOrder.business.dao.StreetDAO;
import pl.xavras.FoodOrder.domain.Restaurant;
import pl.xavras.FoodOrder.domain.RestaurantStreet;
import pl.xavras.FoodOrder.domain.Street;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static pl.xavras.FoodOrder.util.DomainFixtures.*;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class StreetServiceTest {

    @InjectMocks
    private StreetService streetService;

    @Mock
    private StreetDAO streetDAO;

    @Mock
    private RestaurantService restaurantService;


    @Test
    void testFindAll() {

        //given
        List<Street> expectedStreets = List.of(someStreet1(), someStreet2(), someStreet3());

        when(streetDAO.findAll()).thenReturn(expectedStreets);

        //when
        List<Street> actualStreets = streetService.findAll();

        //then
        assertEquals(expectedStreets, actualStreets);
    }

    @Test
    void testFindById() {

        //given
        Street expectedStreet = someStreet1().withStreetId(1);

        when(streetDAO.findByStreetId(1)).thenReturn(Optional.of(expectedStreet));

        //when
        Street actualStreet = streetService.findById(1);

        //then
        assertEquals(expectedStreet, actualStreet);
    }


}