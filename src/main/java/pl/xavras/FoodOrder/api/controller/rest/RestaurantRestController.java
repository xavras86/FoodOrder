package pl.xavras.FoodOrder.api.controller.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.xavras.FoodOrder.api.dto.AddressDTO;
import pl.xavras.FoodOrder.api.dto.CustomerDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantDTO;
import pl.xavras.FoodOrder.api.dto.RestaurantsDTO;
import pl.xavras.FoodOrder.api.dto.mapper.AddressMapper;
import pl.xavras.FoodOrder.api.dto.mapper.CustomerMapper;
import pl.xavras.FoodOrder.api.dto.mapper.RestaurantMapper;
import pl.xavras.FoodOrder.business.CustomerService;
import pl.xavras.FoodOrder.business.OwnerService;
import pl.xavras.FoodOrder.business.RestaurantService;
import pl.xavras.FoodOrder.domain.Address;
import pl.xavras.FoodOrder.domain.Customer;
import pl.xavras.FoodOrder.domain.Restaurant;

import java.net.URI;

@RestController
@Slf4j
@RequestMapping
@AllArgsConstructor
public class RestaurantRestController {
    public static final String RESTAURANTS = "/api/restaurants";
    public static final String RESTAURANTS_NAME = "/api/restaurants/{restaurantName}";

    private final RestaurantService restaurantService;

    private final CustomerMapper customerMapper;

    private final CustomerService customerService;


    private final RestaurantMapper restaurantMapper;

    private final AddressMapper addressMapper;

    //localhost:8888/foodorder/api/restaurants
    @GetMapping(RESTAURANTS)
    public RestaurantsDTO restaurantsList() {
        return RestaurantsDTO.of(restaurantService.findAll().stream()
                .map(restaurantMapper::map)
                .toList());
    }
    //localhost:8888/foodorder/api/restaurants{restaurantName}

    @GetMapping(RESTAURANTS_NAME)
    public RestaurantDTO restaurantDetails(@PathVariable String restaurantName) {
        return restaurantMapper.map(restaurantService.findByName(restaurantName));

    }

    @PostMapping("/api")
    public ResponseEntity<Restaurant> addRestaurant(
            @RequestBody RestaurantDTO restaurantDTO) {

        Restaurant newRestaurant = restaurantMapper.map(restaurantDTO);
//        Address newAddress = addressMapper.map(addressDTO);
        Restaurant restaurant = restaurantService.saveNewRestaurant(newRestaurant, Address.builder().country("Polska").city("Wroclaw").street("Polna").buildingNumber("22").build());
        return ResponseEntity
                .created(URI.create(RESTAURANTS + "/" + restaurant.getRestaurantId()))
                .build();
    }

    @PostMapping("/api/customer")
    public ResponseEntity<Customer> addCustomer(
            @RequestBody CustomerDTO customerDTO) {

        Customer newCustomer = customerMapper.map(customerDTO);
        Customer customer = customerService.saveCustomer(newCustomer);
        return ResponseEntity
                .created(URI.create("/api/customer/" + customer.getCustomerId()))
                .build();
    }




    //"reczne" tworzenie ResponseEntity
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Location",EMPLOYEES + EMPLOYEE_ID_RESULT.formatted(created.getEmployeeId()));
//
//        return new ResponseEntity<>(
//                headers,
//                HttpStatus.CREATED
//        );


//    @GetMapping(RESTAURANTS_NAME
//
//    )
//    public RestaurantDTO restaurantDetails(@PathVariable String restaurantName ) {
//        return restaurantMapper.map(restaurantService.findByName(restaurantName));
//
//    }

//    //localhost:8888/foodorder/api/restaurants{restaurantName}
//    @GetMapping(RESTAURANTS_ID)
//    public OrdersDTO ordersCompleted(@PathVariable String restaurantName ) {
//        return restaurantMapper.map(restaurantService.findByName(restaurantName));
//
//    }


//    //localhost:8190/zajavka/employees/{employeeId}
//    @GetMapping(value = EMPLOYEE_ID) //domyślenia zwraca JSON
//    public EmployeeDTO employeesDetails(@PathVariable Integer employeeId) {
//        return employeeRepository.findById(employeeId)
//                .map(employeeMapper::map)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
//                ));
//    }
//
//    //localhost:8190/zajavka/employees/{employeeId}
//    @GetMapping(value = EMPLOYEE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
//    public EmployeeDTO employeesDetailsAsJson(@PathVariable Integer employeeId) {
//        return employeeRepository.findById(employeeId)
//                .map(employeeMapper::map)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
//                ));
//    }
//
//    //localhost:8190/zajavka/employees/{employeeId}
//    @GetMapping(value = EMPLOYEE_ID, produces = MediaType.APPLICATION_XML_VALUE)
//    public EmployeeDTO employeesDetailsAsXml(@PathVariable Integer employeeId) {
//        return employeeRepository.findById(employeeId)
//                .map(employeeMapper::map)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
//                ));
//    }
//
////    @GetMapping(value = EMPLOYEE_ID,
////            produces = {
////                    MediaType.APPLICATION_JSON_VALUE,
////                    MediaType.APPLICATION_XML_VALUE
////
////            } //wspolna metoda dla roznych formatow odpowiedzi
////    )
////    public EmployeeDTO employeesDetails2(@PathVariable Integer employeeId) {
////        return employeeRepository.findById(employeeId)
////                .map(employeeMapper::map)
////                .orElseThrow(() -> new EntityNotFoundException(
////                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeId)
////                ));
////    }
//
//    @PostMapping
//    // POST NIE JEST IDEMPOTENTNY! - stan serwera/bazy ulega zmianie przy kolejnych wywołaniach metozy z tym samym ciałem, powstają kolejne wpisy w bazie
//    @Transactional //jeżeli mamy warstwę serwisów to metoda powinna być transactional na poziomie serwisu
//    public ResponseEntity<EmployeeDTO> addEmployee(
//            @Valid @RequestBody EmployeeDTO employeeDTO) {
//        EmployeeEntity employeeEntity = EmployeeEntity.builder()
//                .name(employeeDTO.getName())
//                .surname(employeeDTO.getSurname())
//                .salary(employeeDTO.getSalary())
//                .phone(employeeDTO.getPhone())
//                .email(employeeDTO.getEmail())
//                .build();
//        EmployeeEntity created = employeeRepository.save(employeeEntity);
//
//        //"reczne" tworzenie ResponseEntity
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("Location",EMPLOYEES + EMPLOYEE_ID_RESULT.formatted(created.getEmployeeId()));
////
////        return new ResponseEntity<>(
////                headers,
////                HttpStatus.CREATED
////        );
//
//
//        return ResponseEntity
//                .created(URI.create(EMPLOYEES + EMPLOYEE_ID_RESULT.formatted(created.getEmployeeId())))
//                .build();
//    }
//    //przykladowe wywolanie curl:
//    //curl -i -H "Content-Type: application/json" -X POST http://localhost:8190/zajavka/employees -d "{\"name\":\"Marcin\",\"surname\":\"Sikora\",\"salary\":52322.00,\"phone\":\"+42 111 222 666\",\"email\":\"sikomar.zajavka@pl\"}"
//
//    @PutMapping(EMPLOYEE_ID)
//    @Transactional
//    //put powinien być idempotentny - stan bazy nie ulega zmianie przy wielokrotnym zapytaniu z tym samym ciałem
//    //    @RequestMapping(method = RequestMethod.PUT)
//    public ResponseEntity<?> updateEmployee( // rownie dobrze moze być void
//                                             @PathVariable Integer employeeId,
//                                             @Valid @RequestBody EmployeeDTO employeeDTO
//    ) {
//        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Employee not found, employeeId [%s]".formatted(employeeId)
//                ));
//        existingEmployee.setName(employeeDTO.getName());
//        existingEmployee.setSurname(employeeDTO.getSurname());
//        existingEmployee.setSalary(employeeDTO.getSalary());
//        existingEmployee.setPhone(employeeDTO.getPhone());
//        existingEmployee.setEmail(employeeDTO.getEmail());
//        employeeRepository.save(existingEmployee);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping(EMPLOYEE_ID) //jest idempotentny - wielikrotne usuniecie nie zmienia stanu bazy
//    public ResponseEntity<?> deleteEmployee(
//            @PathVariable Integer employeeId
//    ) {
//        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Employee not found, employeeId [%s]".formatted(employeeId)
//                ));
//        employeeRepository.deleteById(existingEmployee.getEmployeeId());
//        return ResponseEntity.noContent().build();
//
//        //alternatywnie:
////        @DeleteMapping(EMPLOYEE_ID)
////        public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
////            try {
////                employeeRepository.deleteById(employeeId);
////                return ResponseEntity.ok().build();
////            } catch (Exception e) {
////                return ResponseEntity.notFound().build();
////            }
////        }
//        //albo:
////        Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(employeeId);
////        if (employeeOpt.isPresent()) {
////            employeeRepository.deleteById(employeeId);
////            return ResponseEntity.noContent().build();
////        } else {
////            return ResponseEntity.notFound().build();
////        }
//    }
//
//    @PatchMapping(EMPLOYEE_UPDATE_SALARY)
//    //może być idempotentny ale nie musi, modyfikacja przy dodanie elemnentu - nei jest idempotentna - zamawuiam kolejna kawe vs zamieniam sernik na tort
//    public ResponseEntity<?> updateEmployeeSalary(
//            @PathVariable Integer employeeId,
//            @RequestParam(required = false, defaultValue = "10000.00") BigDecimal newSalary //dla request param mozna ustalic ze jest required albo że ma default value (w przypadku gdy nie podamy
//    ) {
//        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Employee not found, employeeId [%s]".formatted(employeeId)
//                ));
//        existingEmployee.setSalary(newSalary);
//        employeeRepository.save(existingEmployee);
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping(EMPLOYEE_UPDATE_PET)
//    public ResponseEntity<?> updateEmployeeWithPet(
//            @PathVariable Integer employeeId,
//            @PathVariable Integer petId
//    ) {
//        EmployeeEntity existingEmployee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "EmployeeEntity not found, employeeId: {%s}".formatted(employeeId)
//                ));
//        Pet petFromStore = petDao.getPet(Long.valueOf(petId))
//                .orElseThrow(() -> new RuntimeException(
//                        "Pet with Id : [%s], cound not be retrieved".formatted(petId)
//                ));
//        PetEntity newPet = PetEntity.builder()
//                .petStorePetId(petFromStore.getId())
//                .name(petFromStore.getName())
//                .category(petFromStore.getCategory())
//                .employee(existingEmployee)
//                .build();
//
//        petRepository.save(newPet);
//        Set<PetEntity> pets = existingEmployee.getPets();
////        log.info("Logowanie petow przed dodaniem : " +pets.toString());
//        pets.add(newPet);
////        log.info("Logowanie petow set petow: " +pets + " pety pracownika: " + existingEmployee.getPets());
////        existingEmployee.setPets(pets);
//
////
////        EmployeeEntity existingEmployee2 = employeeRepository.findById(employeeId)
////                .orElseThrow(() -> new EntityNotFoundException(
////                        "EmployeeEntity not found, employeeId: {%s}".formatted(employeeId)
////                ));
//
////        List<PetEntity> allPets = petRepository.findAll();
////
////        Set<PetEntity> pets2 = existingEmployee2.getPets();
////        System.out.println("from pets: " + allPets);
////        System.out.println("PETS: " + pets2);
//
//        //dodatnie peta do exiting employee ? - wg mnie nie (kod powyzej)
//
//        return ResponseEntity.ok().build();
//
//    }
//
//
//    //wykorzysatanie naglowkow:
//    //curl -i -H "Accept: Application/json" -H "httpStatus: 204" -X GET http://localhost:8191/zajavka/employees/test-header
//
//    @GetMapping(value = "test-header")
//    public ResponseEntity<?> testHeader(
//            @RequestHeader(value = HttpHeaders.ACCEPT) MediaType accept,
//            @RequestHeader(value = "httpStatus", required = true) int httpStatus
//    ) {
//        return ResponseEntity
//                .status(httpStatus)
//                .header("X-my-header", accept.toString())
//                .body("Accepted: " + accept);
//    }


//    @PostMapping
////    @RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<?> addEmployee (
//            @RequestParam(value = "name") String name,
//            @RequestParam(value = "surname") String surname,
//            @RequestParam(value = "salary") String salary
//    ){
//        //metoda tworzaca i zapisujaca do bazy obiekt pracownika
//        //POST http://localhost:8080/employees?name=Karol&surname=Zajavka&salary=12345
//    }


//    @GetMapping("/{employeeId}")
////    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity<?> showEmployeeDetails(@PathVariable Integer employeeId) {
//        //metoda zwracajaca pracownika o zdefiniowanym Id
//        //GET http://localhost:8080/employees/1
//    }
//
//    @PutMapping("/{employeeId}")
//    //    @RequestMapping(method = RequestMethod.PUT)
//    public ResponseEntity<?> updateEmployee(
//            @PathVariable Integer employeeId,
//            @RequestParam String name,
//            @RequestParam String surname,
//            @RequestParam String salary
//    ){
//        //metoda aktualizujaca obiekt pracownika
//        //PUT http://localhost:8080/1&name=Karol&surname=Zajavka&salary=12345
//    }
//
//    @PatchMapping("/{employeeId}")
////    @RequestMapping(method = RequestMethod.PATCH)
//    public ResponseEntity<?> partiallyUpdateEmployee(@PathVariable Integer employeeId){
//
//    }
//
//
//
//    @DeleteMapping("/{employeeId}")
////    @RequestMapping(method = RequestMethod.DELETE)
//
//    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
//        //metoda usuwająca pracownika
//        // DELETE http://localhost:8080/employees/1
//    }


}
