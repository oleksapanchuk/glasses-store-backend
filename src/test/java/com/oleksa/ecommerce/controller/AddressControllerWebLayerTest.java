package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.dto.ResponseDto;
import com.oleksa.ecommerce.mapper.AddressMapper;
import com.oleksa.ecommerce.security.JwtAuthenticationFilter;
import com.oleksa.ecommerce.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AddressController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AddressService addressService;

    AddressDto addressDto;

    @BeforeEach
    void setup() {
        addressDto = AddressDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .country("USA")
                .zipCode("10001")
                .build();
    }

    @Test
    void contextLoads() {
        System.out.println("contextLoads");
    }

    @Disabled
    @Test
    void shouldCreateAddressSuccessfully() throws Exception {
        // Arrange
        AddressDto address = addressDto;
        address.setId(1L);


//        Authentication auth = mock(Authentication.class);
//        when(auth.getName()).thenReturn("testUsername");
//        SecurityContextHolder.getContext().setAuthentication(auth);

//        when(addressService.createAddress(anyString(), any(AddressDto.class))).thenReturn(address);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/address/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addressDto));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
//        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
//        ResponseEntity<ResponseDto> createdAddress = new ObjectMapper().readValue(responseBodyAsString, ResponseEntity.class);



        // Assert
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }
}

//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
