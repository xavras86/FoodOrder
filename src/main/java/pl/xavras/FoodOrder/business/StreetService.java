package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.StreetDAO;
import pl.xavras.FoodOrder.domain.Street;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StreetService {

    private final StreetDAO streetDAO;


    @Transactional
    public List<Street> findAll() {
        return streetDAO.findAll();
    }



}