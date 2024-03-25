package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UnauthorizedAccessException;
import com.oleksa.ecommerce.mapper.AddressMapper;
import com.oleksa.ecommerce.repository.AddressRepository;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    private AddressDto addressDto;

    @BeforeEach
    void setup() {
        addressDto = AddressDto.builder()
                .id(1L)
                .city("testCity")
                .country("testCountry")
                .street("testStreet")
                .zipCode("testZipCode")
                .build();
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.save(any())).thenReturn(address);

        AddressDto result = addressService.createAddress("testUsername", addressDto);

        assertNotNull(result);
        assertEquals(addressDto, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnCreateAddress() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.createAddress("testUsername", addressDto)
        );
    }

    @Test
    void shouldFetchAddressSuccessfully() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);
        address.setUser(user);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        AddressDto result = addressService.fetchAddress("testUsername", 1L);

        assertNotNull(result);
        assertEquals(addressDto, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnFetchAddress() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.fetchAddress("testUsername", 1L)
        );
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFoundOnFetchAddress() {
        User user = new User();
        user.setUsername("testUsername");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.fetchAddress("testUsername", 1L));
    }

    @Test
    void shouldThrowExceptionWhenUnauthorizedAccessOnFetchAddress() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);
        address.setUser(new User());
        address.getUser().setId(2L);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        assertThrows(UnauthorizedAccessException.class,
                () -> addressService.fetchAddress("testUsername", 1L)
        );
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);
        address.setUser(user);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));
        when(addressRepository.save(any())).thenReturn(address);

        boolean result = addressService.updateAddress("testUsername", addressDto);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnUpdateAddress() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress("testUsername", addressDto));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFoundOnUpdateAddress() {
        User user = new User();
        user.setUsername("testUsername");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.updateAddress("testUsername", addressDto));
    }

    @Test
    void shouldThrowExceptionWhenUnauthorizedAccessOnUpdateAddress() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);
        address.setUser(new User());
        address.getUser().setId(2L);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        assertThrows(UnauthorizedAccessException.class, () -> addressService.updateAddress("testUsername", addressDto));
    }

    @Test
    void shouldDeleteAddressSuccessfully() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);
        address.setUser(user);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        boolean result = addressService.deleteAddress("testUsername", 1L);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDeleteAddress() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress("testUsername", 1L));
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFoundOnDeleteAddress() {
        User user = new User();
        user.setUsername("testUsername");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addressService.deleteAddress("testUsername", 1L));
    }

    @Test
    void shouldThrowExceptionWhenUnauthorizedAccessOnDeleteAddress() {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = new User();
        user.setUsername("testUsername");
        user.setId(1L);
        address.setUser(new User());
        address.getUser().setId(2L);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(addressRepository.findById(anyLong())).thenReturn(Optional.of(address));

        assertThrows(UnauthorizedAccessException.class, () -> addressService.deleteAddress("testUsername", 1L));
    }
}
