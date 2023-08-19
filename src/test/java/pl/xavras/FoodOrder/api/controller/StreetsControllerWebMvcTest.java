package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = StreetsController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class StreetsControllerWebMvcTest {


}