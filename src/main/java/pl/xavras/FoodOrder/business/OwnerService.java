package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.xavras.FoodOrder.business.dao.OwnerDAO;
import pl.xavras.FoodOrder.domain.Owner;

@Service
@AllArgsConstructor
@Slf4j
public class OwnerService {

    private final OwnerDAO ownerDAO;

    @Transactional
    public Owner saveOwner(Owner owner) {
        return ownerDAO.saveOwner(owner);
    }


    public Owner activeOwner() {
        return ownerDAO.findLoggedOwner();
    }

}