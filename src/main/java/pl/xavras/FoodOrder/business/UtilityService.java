package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class UtilityService {

    public List<Integer> generatePageNumbers(int currentPage, int totalPages) {
        int numToShow = 5;
        int start = Math.max(1, currentPage - numToShow / 2);
        int end = Math.min(totalPages, start + numToShow - 1);

        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }

    public PageRequest createPagable(int pageSize, int pageNumber, String sortBy, String sortDirection) {
        return PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.fromString(sortDirection), sortBy));
    }
}
