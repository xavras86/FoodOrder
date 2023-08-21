package pl.xavras.FoodOrder.infrastructure.database.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.xavras.FoodOrder.business.dao.StreetDAO;
import pl.xavras.FoodOrder.domain.Street;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.StreetJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.StreetEntityMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StreetRepository implements StreetDAO {

    private final StreetJpaRepository streetJpaRepository;
    private final StreetEntityMapper streetEntityMapper;

    @Override
    public List<Street> findAll() {
        return streetJpaRepository.findAll().stream()
                .map(streetEntityMapper::mapFromEntity).toList();
    }

    @Override
    public Optional<Street> findByStreetId(Integer streetId) {
        return streetJpaRepository.findByStreetId(streetId)
                .map(streetEntityMapper::mapFromEntity);
    }

    @Override
    public Optional<Street> findByStreetName(String streetName) {
        return streetJpaRepository.findByStreetName(streetName)
                .map(streetEntityMapper::mapFromEntity);
    }

    @Override
    public Page<Street> findAll(Pageable pageable) {
        return (streetJpaRepository.findAll(pageable)).map(streetEntityMapper::mapFromEntity);
    }

}
