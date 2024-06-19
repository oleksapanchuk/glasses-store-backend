package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.repository.AddressRepository;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldFetchAddressesByUserIdSuccessfully_WhenAddressesExist() {
        Long userId = 1L;
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setUser(new User());
        addresses.add(address);
        when(addressRepository.findAllByUserId(userId)).thenReturn(addresses);

        List<AddressDto> result = addressService.fetchAddressesByUserId(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyList_WhenNoAddressesExistForUser() {
        Long userId = 1L;
        when(addressRepository.findAllByUserId(userId)).thenReturn(null);

        List<AddressDto> result = addressService.fetchAddressesByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFetchAddressSuccessfully_WhenAddressExists() {
        Long addressId = 1L;
        Address address = new Address();
        address.setUser(new User());
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        AddressDto result = addressService.fetchAddress(addressId);

        assertNotNull(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenAddressDoesNotExist() {
        Long addressId = 1L;
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.fetchAddress(addressId));
    }

    @Test
    void shouldCreateAddressSuccessfully_WhenValidAddressDtoIsGiven() {
        AddressDto addressDto = AddressDto.builder().build();
        addressDto.setUserId(1L);
        User user = new User();
        Address address = new Address();
        when(userRepository.findById(addressDto.getUserId())).thenReturn(Optional.of(user));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDto result = addressService.createAddress(addressDto);

        assertNotNull(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserDoesNotExistOnCreateAddress() {
        AddressDto addressDto = AddressDto.builder().build();
        addressDto.setUserId(1L);
        when(userRepository.findById(addressDto.getUserId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.createAddress(addressDto));
    }

    @Test
    void shouldUpdateAddressSuccessfully_WhenValidAddressDtoIsGiven() {
        AddressDto addressDto = AddressDto.builder().build();
        addressDto.setUserId(1L);
        addressDto.setId(1L);
        User user = new User();
        Address address = new Address();
        address.setUser(user);
        when(userRepository.findById(addressDto.getUserId())).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressDto.getId())).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDto result = addressService.updateAddress(addressDto);

        assertNotNull(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserDoesNotExistOnUpdateAddress() {
        AddressDto addressDto = AddressDto.builder().build();
        addressDto.setUserId(1L);
        when(userRepository.findById(addressDto.getUserId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(addressDto));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenAddressDoesNotExistOnUpdateAddress() {
        AddressDto addressDto = AddressDto.builder().build();
        addressDto.setUserId(1L);
        addressDto.setId(1L);
        User user = new User();
        when(userRepository.findById(addressDto.getUserId())).thenReturn(Optional.of(user));
        when(addressRepository.findById(addressDto.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress(addressDto));
    }

    @Test
    void shouldDeleteAddressSuccessfully_WhenAddressExists() {
        Long addressId = 1L;
        when(addressRepository.existsById(addressId)).thenReturn(true);

        boolean result = addressService.deleteAddress(addressId);

        assertTrue(result);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenAddressDoesNotExistOnDelete() {
        Long addressId = 1L;
        when(addressRepository.existsById(addressId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress(addressId));
    }
}
