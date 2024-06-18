package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.constants.AppConstants;
import com.oleksa.ecommerce.dto.AddressDto;
import com.oleksa.ecommerce.dto.ResponseDto;
import com.oleksa.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@AllArgsConstructor
@RequestMapping(path = "/api/addresses", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/fetch-by-user-id/{user-id}")
    public ResponseEntity<List<AddressDto>> fetchAddressesByUserId(
            @PathVariable(name = "user-id") Long userId
    ) {

        List<AddressDto> addresses = addressService.fetchAddressesByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addresses);
    }

    @GetMapping("/fetch/{address-id}")
    public ResponseEntity<AddressDto> fetchAddress(
            @PathVariable(name = "address-id") Long addressId
    ) {
        AddressDto addressDto = addressService.fetchAddress(addressId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressDto);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAddress(
            @Valid @RequestBody AddressDto addressDto
    ) {
        addressService.createAddress(addressDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AppConstants.STATUS_201, "Address" + AppConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<AddressDto> updateAddress(
            @Valid @RequestBody AddressDto addressDto
    ) {

        AddressDto updatedAddress = addressService.updateAddress(addressDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedAddress);
    }

    @DeleteMapping("/delete/{address-id}")
    public ResponseEntity<ResponseDto> deleteAddress(
            @PathVariable(name = "address-id") Long addressId
    ) {
        boolean isDeleted = addressService.deleteAddress(addressId);

        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_DELETE));
        }
    }

}

