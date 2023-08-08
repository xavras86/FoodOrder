package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.xavras.FoodOrder.business.dao.MenuItemDAO;
import pl.xavras.FoodOrder.business.dao.OrderDAO;
import pl.xavras.FoodOrder.domain.*;
import pl.xavras.FoodOrder.domain.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class MenuItemService {

    private final MenuItemDAO menuItemDAO;


    public MenuItem saveMenuItem(MenuItem menuItemToSave, Restaurant restaurant) {
        return menuItemDAO.addMenuItem(menuItemToSave,restaurant);
    }
}