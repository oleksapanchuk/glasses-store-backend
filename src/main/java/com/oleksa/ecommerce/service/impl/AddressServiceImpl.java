package com.oleksa.ecommerce.service.impl;

import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.Country;
import com.oleksa.ecommerce.entity.State;
import com.oleksa.ecommerce.repository.AddressRepository;
import com.oleksa.ecommerce.repository.CountryRepository;
import com.oleksa.ecommerce.repository.StateRepository;
import com.oleksa.ecommerce.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;


    @Override
    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public Optional<State> getStateById(Long id) {
        return stateRepository.findById(id);
    }

    @Override
    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }


}
