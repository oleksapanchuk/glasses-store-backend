package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.service.AddressService;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AddressController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    private AddressDto addressDto;
    private String username;

    @BeforeEach
    void setUp() {
        addressDto = AddressDto.builder()
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .country("USA")
                .zipCode("10001")
                .build();
        username = "testUser";
        // Create an Authentication object
        Authentication auth = new TestingAuthenticationToken(username, null);
        // Set the Authentication object in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldCreateAddressSuccessfully_WhenValidAddressIsGiven() throws Exception {
        // Assume that createAddress returns some kind of result. Adjust this as needed.
        when(addressService.createAddress(username, addressDto)).thenReturn(addressDto);

        mockMvc.perform(post("/api/address/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDto)))
                .andExpect(status().isCreated());

        verify(addressService, times(1)).createAddress(username, addressDto);
    }

    @Test
    void shouldReturnAddressDto_WhenValidAddressIdIsGiven() throws Exception {
        Long addressId = 1L;
        when(addressService.fetchAddress(username, addressId)).thenReturn(addressDto);

        mockMvc.perform(get("/api/address/fetch/" + addressId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street", is(addressDto.getStreet())))
                .andExpect(jsonPath("$.city", is(addressDto.getCity())))
                .andExpect(jsonPath("$.state", is(addressDto.getState())))
                .andExpect(jsonPath("$.country", is(addressDto.getCountry())))
                .andExpect(jsonPath("$.zipCode", is(addressDto.getZipCode())));

        verify(addressService, times(1)).fetchAddress(username, addressId);
    }

    @Test
    void shouldUpdateAddressSuccessfully_WhenValidAddressIsGiven() throws Exception {
        addressDto.setId(1L);
        when(addressService.updateAddress(username, addressDto)).thenReturn(true);

        mockMvc.perform(put("/api/address/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDto)))
                .andExpect(status().isOk());

        verify(addressService, times(1)).updateAddress(username, addressDto);
    }

    @Test
    void shouldDeleteAddressSuccessfully_WhenValidAddressIdIsGiven() throws Exception {
        Long addressId = 1L;
        when(addressService.deleteAddress(username, addressId)).thenReturn(true);

        mockMvc.perform(delete("/api/address/delete/" + addressId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(addressService, times(1)).deleteAddress(username, addressId);
    }
}
