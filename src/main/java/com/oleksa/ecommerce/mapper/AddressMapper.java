package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddressMapper {

    private final AddressService addressService;

    public AddressDto mapToAddressDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState().getId())
                .country(address.getCountry().getId())
                .zipCode(address.getZipCode())
                .build();
    }

    public Address mapToAddress(AddressDto addressDto) {

        return Address.builder()
                .id(addressDto.getId())
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressService.getStateById(addressDto.getState()).orElseThrow())
                .country(addressService.getCountryById(addressDto.getCountry()).orElseThrow())
                .zipCode(addressDto.getZipCode())
                .build();
    }
}
