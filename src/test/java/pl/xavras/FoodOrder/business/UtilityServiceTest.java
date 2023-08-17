package pl.xavras.FoodOrder.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UtilityServiceTest {

    @InjectMocks
    private UtilityService utilityService;


    @Test
    void testGeneratePageNumbers() {
        //given
        int currentPage = 3;
        int totalPages = 10;

        List<Integer> expectedPageNumbers = List.of(1, 2, 3, 4, 5);
        List<Integer> actualPageNumbers = utilityService.generatePageNumbers(currentPage, totalPages);
        //when
        assertEquals(expectedPageNumbers, actualPageNumbers);
    }

    @Test
    void testCreatePagable() {
        //given
        int pageSize = 10;
        int pageNumber = 2;
        String sortBy = "name";
        String sortDirection = "ASC";
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        PageRequest expectedPageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);

        //when
        PageRequest actualPageRequest = utilityService.createPagable(pageSize, pageNumber, sortBy, sortDirection);

        //then
        assertEquals(expectedPageRequest, actualPageRequest);
    }
}