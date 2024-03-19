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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RequestMapping(path = "/api/address", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(
            @Valid @RequestBody AddressDto addressDto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        addressService.createAddress(username, addressDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AppConstants.STATUS_201, "Address" + AppConstants.MESSAGE_201));
    }

    @GetMapping("/fetch/{address-id}")
    public ResponseEntity<AddressDto> fetchAddress(
            @PathVariable(name = "address-id") Long addressId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        AddressDto addressDto = addressService.fetchAddress(username, addressId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(
            @Valid @RequestBody AddressDto addressDto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isUpdated = addressService.updateAddress(username, addressDto);

        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete/{address-id}")
    public ResponseEntity<ResponseDto> deleteAccount(
            @PathVariable(name = "address-id") Long addressId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isDeleted = addressService.deleteAddress(username, addressId);

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

