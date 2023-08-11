package pl.xavras.FoodOrder.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.xavras.FoodOrder.business.dao.OwnerDAO;
import pl.xavras.FoodOrder.domain.Owner;

@Service
@AllArgsConstructor
@Slf4j
public class OwnerService {

    private final OwnerDAO ownerDAO;



    public Owner findByEmail(String email) {
        return ownerDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Owner with email [%s] doest don't exists".formatted(email)));

    }


    public Owner activeOwner(){
        return ownerDAO.findLoggedOwner();
    }

}