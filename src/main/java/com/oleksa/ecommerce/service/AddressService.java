package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> fetchAddressesByUserId(Long userId);

    AddressDto fetchAddress(Long addressId);

    AddressDto createAddress(AddressDto addressDto);

    AddressDto updateAddress(AddressDto addressDto);

    boolean deleteAddress(Long addressId);

}
