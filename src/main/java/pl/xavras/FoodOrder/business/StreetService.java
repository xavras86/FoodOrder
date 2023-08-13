package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.xavras.FoodOrder.business.dao.StreetDAO;
import pl.xavras.FoodOrder.domain.Street;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Slf4j
public class StreetService {

    private final StreetDAO streetDAO;


    public List<Street> findAll() {
        return streetDAO.findAll();
    }


    public Street findById(Integer streetId) {
        return streetDAO.findByStreetId(streetId)
                .orElseThrow(() -> new RuntimeException("Street with name [%s] doest not exists"
                        .formatted(streetId)));
    }

    public Page<Street> findAll(Pageable pageable) {
        return streetDAO.findAll(pageable);

    }

    public List<Integer> generatePageNumbers(int currentPage, int totalPages) {
        int numToShow = 5;
        int start = Math.max(1, currentPage - numToShow / 2);
        int end = Math.min(totalPages, start + numToShow - 1);

        return IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());
    }
}