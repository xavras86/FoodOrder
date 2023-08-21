package pl.xavras.FoodOrder.infrastructure.database.repository;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.xavras.FoodOrder.domain.MenuItem;
import pl.xavras.FoodOrder.infrastructure.database.repository.jpa.MenuItemJpaRepository;
import pl.xavras.FoodOrder.infrastructure.database.repository.mapper.MenuItemEntityMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemRepositoryTest {
    @Mock
    private MenuItemJpaRepository menuItemJpaRepository;

    @Mock
    private MenuItemEntityMapper menuItemEntityMapper;

    @InjectMocks
    private MenuItemRepository repository;

    @Test
    public void testFindAll() {
        // given
        when(menuItemJpaRepository.findAll()).thenReturn(new ArrayList<>());

        // when
        List<MenuItem> result = repository.findAll();

        // then
        assertEquals(0, result.size());
    }


}