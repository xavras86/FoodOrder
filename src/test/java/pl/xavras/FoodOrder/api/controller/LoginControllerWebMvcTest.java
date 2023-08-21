package pl.xavras.FoodOrder.api.controller;

import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import pl.xavras.FoodOrder.api.dto.UserDTO;
import pl.xavras.FoodOrder.api.dto.mapper.UserMapper;
import pl.xavras.FoodOrder.business.UserService;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.xavras.FoodOrder.api.controller.LoginController.REGISTER;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = LoginController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class LoginControllerWebMvcTest {

    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;


    public static Stream<Arguments> thatPhoneValidationWorksCorrectlyRegisterUser() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(false, "4812504203260"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "+4812504203260"),
                Arguments.of(true, "+48 504 203 260")
        );
    }

    @ParameterizedTest
    @MethodSource
    void thatPhoneValidationWorksCorrectlyRegisterUser(Boolean correctPhone, String phone) throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = UserDTO.builder().phone(phone).build().asMap();
        parametersMap.forEach(parameters::add);

        //when, then
        if (correctPhone) {
            mockMvc.perform(post(REGISTER).params(parameters))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("/login"));
        } else {
            mockMvc.perform(post(REGISTER).params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(phone)))
                    .andExpect(view().name("error"));
        }
    }

}

