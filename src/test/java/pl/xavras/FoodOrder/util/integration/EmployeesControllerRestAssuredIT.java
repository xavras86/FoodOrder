package pl.xavras.FoodOrder.util.integration;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.xavras.FoodOrder.util.integration.configuration.RestAssuredIntegrationTestBase;


import java.util.Set;
import java.util.regex.Pattern;



public class EmployeesControllerRestAssuredIT
        extends RestAssuredIntegrationTestBase
//        implements EmployeesControllerTestSupport, WireMockTestSupport
{

//    @Test
//    void thatEmployeesListCanBeRetrievedCorrectly() {
//        //given
//        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
//        EmployeeDTO employee2 = DtoFixtures.someEmployee2();
//
//        //when
//        saveEmployee(employee1);
//        saveEmployee(employee2);
//
//        EmployeesDTO employeesDTO = listEmployees();
//
//        //then
//        Assertions.assertThat(employeesDTO.getEmployees())
//                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("employeeId")
//                .contains(employee1.withPets(Set.of()), employee2.withPets(Set.of()));
//
//    }
//
//
//    @Test
//    public void thatEmployeeCanBeCreatedCorrectly() {
//        //given
//        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
//
//        //when
//        ExtractableResponse<Response> response = saveEmployee(employee1);
//
//        //then
//        String responseAsString = response.body().asString();
//        Assertions.assertThat(responseAsString).isEmpty();
//        Assertions.assertThat(response.headers().get("Location").getValue())
//                .matches(Pattern.compile("/employees/\\d"));
//
//    }
//
//    @Test
//    void thatCreatedEmployeeCanBeRetrievedCorrectly() {
//
//        //given
//        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
//
//        //when
//        ExtractableResponse<Response> response = saveEmployee(employee1);
//        String employeeDetailsPath = response.headers().get("Location").getValue();
//
//        EmployeeDTO employee = getEmployee(employeeDetailsPath);
//
//        //then
//
//        Assertions.assertThat(employee)
//                .usingRecursiveComparison()
//                .ignoringFields("employeeId")
//                .isEqualTo(employee1.withPets(Set.of()));
//    }
//
////    @Test
////    void thatAddingPetToEmployeeWorksCorrectly() {
////
////        //given
////        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
////        PetDTO petDTO = DtoFixtures.somePet();
////
////        //when
////
////        updateEmployeeByPet(employee1.getEmployeeId(), petDTO.getPetId());
////
////        //then
////
////        Assertions.assertThat(employee1.getPets())
////                .containsExactly(petDTO);
////
////    }
//    @Test
//    void thatEmployeesCanBeUpdatedWithPetCorrectly() {
//        //given
//        long petId =4;
//        EmployeeDTO employee1 = DtoFixtures.someEmployee1();
//        ExtractableResponse<Response> response = saveEmployee(employee1);
//        String employeeDetailsPath = response.headers().get("Location").getValue();
//
//        EmployeeDTO retrievedEmployee = getEmployee(employeeDetailsPath);
//
//        stubForPet(wireMockServer, petId);
//
//        //when
//
//        updateEmployeeByPet(retrievedEmployee.getEmployeeId(), petId);
//
//        //then
//
//        EmployeeDTO employeeWithPet = getEmployeeById(retrievedEmployee.getEmployeeId());
//        System.out.println(" employeeWithPet.getPets() : " +employeeWithPet.getPets());
//
//
//
//        Assertions.assertThat(employeeWithPet)
//                .usingRecursiveComparison()
//                .ignoringFields("employeeId", "petId")
//                .isEqualTo(employee1.withPets(Set.of(somePet())));
//
//
//    }


}
