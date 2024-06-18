package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.User;
import com.oleksa.ecommerce.exception.ResourceNotFoundException;
import com.oleksa.ecommerce.exception.UnauthorizedAccessException;
import com.oleksa.ecommerce.mapper.AddressMapper;
import com.oleksa.ecommerce.repository.AddressRepository;
import com.oleksa.ecommerce.repository.UserRepository;
import com.oleksa.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressDto createAddress(String email, AddressDto addressDto) {

        Address address = AddressMapper.mapToAddress(addressDto);

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        address.setUser(user);

        addressRepository.save(address);

        return AddressMapper.mapToAddressDto(address);
    }

    @Override
    public AddressDto fetchAddress(String email, Long addressId) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", email)
        );

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

        if ((long) address.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Address", "email", email);
        }

        return AddressMapper.mapToAddressDto(address);
    }

    @Override
    public boolean updateAddress(String email, AddressDto addressDto) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        Address address = addressRepository.findById(addressDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressDto.getId())
        );

        if ((long) address.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Address", "email", email);
        }

        Address newAddress = AddressMapper.mapToAddress(addressDto);
        newAddress.setUser(user);

        addressRepository.save(newAddress);

        return true;
    }

    @Override
    public boolean deleteAddress(String email, Long addressId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

        if ((long) address.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Address", "email", email);
        }

        addressRepository.deleteById(addressId);

        return true;
    }
}
