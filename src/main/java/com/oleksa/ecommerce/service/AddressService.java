package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.dto.AddressDto;

public interface AddressService {
    AddressDto createAddress(String username, AddressDto addressDto);

    AddressDto fetchAddress(String username, Long addressId);

    boolean updateAddress(String username, AddressDto addressDto);

    boolean deleteAddress(String username, Long addressId);
}
