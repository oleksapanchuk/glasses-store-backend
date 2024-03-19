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
    public void createAddress(String username, AddressDto addressDto) {

        Address address = AddressMapper.mapToAddress(addressDto);

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );

        address.setUser(user);

        addressRepository.save(address);
    }

    @Override
    public AddressDto fetchAddress(String username, Long addressId) {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

        if ((long) address.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Address", "username", username);
        }

        return AddressMapper.mapToAddressDto(address);
    }

    @Override
    public boolean updateAddress(String username, AddressDto addressDto) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );

        Address address = addressRepository.findById(addressDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressDto.getId())
        );

        if ((long) address.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Address", "username", username);
        }

        Address newAddress = AddressMapper.mapToAddress(addressDto);
        newAddress.setUser(user);

        addressRepository.save(newAddress);

        return true;
    }

    @Override
    public boolean deleteAddress(String username, Long addressId) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User", "username", username)
        );

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );

        if ((long) address.getUser().getId() != user.getId()) {
            throw new UnauthorizedAccessException("Address", "username", username);
        }

        addressRepository.deleteById(addressId);

        return true;
    }
}
