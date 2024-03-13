package com.oleksa.ecommerce.service;

import com.oleksa.ecommerce.entity.Address;
import com.oleksa.ecommerce.entity.Country;
import com.oleksa.ecommerce.entity.State;

import java.util.Optional;

public interface AddressService {
    Optional<Address> getAddressById(Long id);
    Optional<State> getStateById(Long id);
    Optional<Country> getCountryById(Long id);

}
