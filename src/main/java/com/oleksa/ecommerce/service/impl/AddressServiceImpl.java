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

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public List<AddressDto> fetchAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        if (addresses != null) {
            return AddressMapper.mapToAddressDtoList(addresses);
        }
        return List.of();
    }

    @Override
    public AddressDto fetchAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressId)
        );
        return AddressMapper.mapToAddressDto(address);
    }

    @Override
    public AddressDto createAddress(AddressDto addressDto) {
        Address address = AddressMapper.mapToAddress(addressDto);
        User user = userRepository.findById(addressDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", addressDto.getUserId())
        );
        address.setUser(user);
        addressRepository.save(address);
        return AddressMapper.mapToAddressDto(address);
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto) {
        User user = userRepository.findById(addressDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", addressDto.getUserId())
        );
        Address address = addressRepository.findById(addressDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", addressDto.getId())
        );
        Address newAddress = AddressMapper.mapToAddress(addressDto);
        newAddress.setUser(user);
        Address updatedAddress = addressRepository.save(newAddress);
        return AddressMapper.mapToAddressDto(updatedAddress);
    }

    @Override
    public boolean deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address", "addressId", addressId);
        }
        addressRepository.deleteById(addressId);
        return true;
    }
}
