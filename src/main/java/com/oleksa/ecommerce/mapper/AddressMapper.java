package com.oleksa.ecommerce.mapper;

import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.entity.Address;

import java.util.List;

public class AddressMapper {

    public static AddressDto mapToAddressDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .zipCode(address.getZipCode())
                .userId(address.getUser().getId())
                .build();
    }

    public static Address mapToAddress(AddressDto addressDto) {

        return Address.builder()
                .id(addressDto.getId())
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .country(addressDto.getCountry())
                .zipCode(addressDto.getZipCode())
                .build();
    }

    public static List<AddressDto> mapToAddressDtoList(List<Address> addresses) {
        return addresses.stream()
                .map(AddressMapper::mapToAddressDto)
                .toList();
    }
}
