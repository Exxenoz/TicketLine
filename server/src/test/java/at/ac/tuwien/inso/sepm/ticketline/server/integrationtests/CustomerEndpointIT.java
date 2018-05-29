package at.ac.tuwien.inso.sepm.ticketline.server.integrationtests;

import at.ac.tuwien.inso.sepm.ticketline.rest.address.BaseAddressDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageRequestDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.page.PageResponseDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.BaseAddress;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.integrationtests.base.BaseIT;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;

public class CustomerEndpointIT extends BaseIT {

    private static final String CUSTOMER_ENDPOINT = "/customer";
    private static final String SAVE_PATH = "/create";
    private static final String UPDATE_PATH = "/update";

    private static final long TEST_CUSTOMER_ID = 1L;
    private static final String TEST_CUSTOMER_FIRSTNAME = "Florian";
    private static final String TEST_CUSTOMER_LASTNAME = "Flo";
    private static final String TEST_CUSTOMER_TELEPHONE = "+43699 1111 1111";
    private static final String TEST_CUSTOMER_EMAIL = "flo@flo.flo";
    private static final BaseAddressDTO TEST_CUSTOMER_BASEADDRESS = new BaseAddressDTO(
            "Ring", "Vienna", "Austria", "1010");
    private static final BaseAddress TEST_CUSTOMER_BASEADDRESS_ENTITY = new BaseAddress(
        "Ring", "Vienna", "Austria", "1010");

    @MockBean
    CustomerRepository customerRepository;

    @Test
    public void findAllCustomersUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(new PageRequestDTO(0, Integer.MAX_VALUE, Sort.Direction.ASC, null))
            .when().post(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    //TODO: generics not transfered to reponse. doesn't match
    @Test
    public void findAllCustomersAsUser() {
        List<Customer> customers = Collections.singletonList(
            Customer.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS_ENTITY)
                .build());
        Page<Customer> customerPage = new PageImpl(customers);

        BDDMockito.
            given(customerRepository.findAll(any(Pageable.class))).
            willReturn(customerPage);
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(new PageRequestDTO(0, Integer.MAX_VALUE, Sort.Direction.ASC, null))
            .when().post(CUSTOMER_ENDPOINT)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));

        List<CustomerDTO> customersDTOList = Collections.singletonList(
            CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS)
                .build());

        HashMap<String, Object> json = response.jsonPath().get();
        CustomerDTO responseCustomerDTO = jsonToCustomerDTO(json);

        PageResponseDTO<CustomerDTO> restPageResponse = new PageResponseDTO<>(List.of(responseCustomerDTO), (int)json.get("totalPages"));
        PageResponseDTO<CustomerDTO> testPageReponse = new PageResponseDTO<>(customersDTOList, 1);
        Assert.assertThat(restPageResponse, is(testPageReponse));
    }

    // plenty room for improvement
    private CustomerDTO jsonToCustomerDTO(HashMap<String, Object> json) {
        ArrayList restResponseCustomerList = (ArrayList) json.get("content");
        HashMap<String, String> reponseContentHashmap = (HashMap<String, String>) restResponseCustomerList.get(0);

        BaseAddressDTO responseBaseAddress = jsonToBaseAddressDTO((HashMap<String, Object>) restResponseCustomerList.get(0));

        return CustomerDTO.builder()
            .id(Long.valueOf(String.valueOf(reponseContentHashmap.get("id"))))
            .firstName(reponseContentHashmap.get("firstName"))
            .lastName(reponseContentHashmap.get("lastName"))
            .telephoneNumber(reponseContentHashmap.get("telephoneNumber"))
            .email(reponseContentHashmap.get("email"))
            .address(responseBaseAddress)
            .build();
    }

    // plenty room for improvement
    private BaseAddressDTO jsonToBaseAddressDTO(HashMap<String, Object> reponseContentHashmap) {
        HashMap<String, Object> responseBaseAddressHashmap = (HashMap<String, Object>) reponseContentHashmap.get("baseAddress");
        return new BaseAddressDTO(
            (String) responseBaseAddressHashmap.get("street"),
            (String) responseBaseAddressHashmap.get("city"),
            (String) responseBaseAddressHashmap.get("country"),
            (String) responseBaseAddressHashmap.get("postalCode"));
    }

    @Test
    public void saveCustomerUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS)
                .build())
            .when().post(CUSTOMER_ENDPOINT + SAVE_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void saveCustomerAsUser() {
        BDDMockito.
            given(customerRepository.save(any(Customer.class))).
            willReturn(Customer.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS_ENTITY)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(CustomerDTO.builder()
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS)
                .build())
            .when().post(CUSTOMER_ENDPOINT + SAVE_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(CustomerDTO.class), is(CustomerDTO.builder()
            .id(TEST_CUSTOMER_ID)
            .firstName(TEST_CUSTOMER_FIRSTNAME)
            .lastName(TEST_CUSTOMER_LASTNAME)
            .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
            .email(TEST_CUSTOMER_EMAIL)
            .address(TEST_CUSTOMER_BASEADDRESS)
            .build()));
    }

    @Test
    public void updateCustomerUnauthorizedAsAnonymous() {
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS)
                .build())
            .when().post(CUSTOMER_ENDPOINT + UPDATE_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void updateCustomerAsUser() {
        BDDMockito.
            given(customerRepository.save(any(Customer.class))).
            willReturn(Customer.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS_ENTITY)
                .build());
        Response response = RestAssured
            .given()
            .contentType(ContentType.JSON)
            .header(HttpHeaders.AUTHORIZATION, validUserTokenWithPrefix)
            .body(CustomerDTO.builder()
                .id(TEST_CUSTOMER_ID)
                .firstName(TEST_CUSTOMER_FIRSTNAME)
                .lastName(TEST_CUSTOMER_LASTNAME)
                .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
                .email(TEST_CUSTOMER_EMAIL)
                .address(TEST_CUSTOMER_BASEADDRESS)
                .build())
            .when().post(CUSTOMER_ENDPOINT + UPDATE_PATH)
            .then().extract().response();
        Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK.value()));
        Assert.assertThat(response.as(CustomerDTO.class), is(CustomerDTO.builder()
            .id(TEST_CUSTOMER_ID)
            .firstName(TEST_CUSTOMER_FIRSTNAME)
            .lastName(TEST_CUSTOMER_LASTNAME)
            .telephoneNumber(TEST_CUSTOMER_TELEPHONE)
            .email(TEST_CUSTOMER_EMAIL)
            .address(TEST_CUSTOMER_BASEADDRESS)
            .build()));
    }
}
