package com.oleksa.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.service.AddressService;
import com.oleksa.ecommerce.service.AuthenticationService;
import com.oleksa.ecommerce.service.JwtService;
import com.oleksa.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AddressController.class})
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    private static final String BASE_ADDRESSES_URL = "/api/addresses";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    @Test
    void shouldFetchAddressesByUserIdSuccessfully_WhenValidUserIdIsGiven() throws Exception {
        Long userId = 1L;
        List<AddressDto> addressDtoList = new ArrayList<>();
        when(addressService.fetchAddressesByUserId(userId)).thenReturn(addressDtoList);

        mockMvc.perform(get(BASE_ADDRESSES_URL + "/fetch-by-user-id/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(addressDtoList.size())));

        verify(addressService, times(1)).fetchAddressesByUserId(userId);
    }

    @Test
    void shouldFetchAddressSuccessfully_WhenValidAddressIdIsGiven() throws Exception {
        Long addressId = 1L;
        AddressDto addressDto = AddressDto.builder().build();
        when(addressService.fetchAddress(addressId)).thenReturn(addressDto);

        mockMvc.perform(get(BASE_ADDRESSES_URL + "/fetch/" + addressId))
                .andExpect(status().isOk());

        verify(addressService, times(1)).fetchAddress(addressId);
    }

    @Test
    void shouldCreateAddressSuccessfully_WhenValidAddressIsGiven() throws Exception {
        AddressDto addressDto = AddressDto.builder().build();
        when(addressService.createAddress(any(AddressDto.class))).thenReturn(addressDto);

        mockMvc.perform(post(BASE_ADDRESSES_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDto)))
                .andExpect(status().isCreated());

        verify(addressService, times(1)).createAddress(any(AddressDto.class));
    }

    @Test
    void shouldUpdateAddressSuccessfully_WhenValidAddressIsGiven() throws Exception {
        AddressDto addressDto = AddressDto.builder().build();
        when(addressService.updateAddress(any(AddressDto.class))).thenReturn(addressDto);

        mockMvc.perform(put(BASE_ADDRESSES_URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressDto)))
                .andExpect(status().isOk());

        verify(addressService, times(1)).updateAddress(any(AddressDto.class));
    }

    @Test
    void shouldDeleteAddressSuccessfully_WhenValidAddressIdIsGiven() throws Exception {
        Long addressId = 1L;
        when(addressService.deleteAddress(addressId)).thenReturn(true);

        mockMvc.perform(delete(BASE_ADDRESSES_URL + "/delete/" + addressId))
                .andExpect(status().isOk());

        verify(addressService, times(1)).deleteAddress(addressId);
    }

    @Test
    void shouldReturnExpectationFailed_WhenAddressDeletionFails() throws Exception {
        Long addressId = 1L;
        when(addressService.deleteAddress(addressId)).thenReturn(false);

        mockMvc.perform(delete(BASE_ADDRESSES_URL + "/delete/" + addressId))
                .andExpect(status().isExpectationFailed());

        verify(addressService, times(1)).deleteAddress(addressId);
    }
}