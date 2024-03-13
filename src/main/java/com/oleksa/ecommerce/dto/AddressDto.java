package com.oleksa.ecommerce.dto;

import com.oleksa.ecommerce.entity.Country;
import com.oleksa.ecommerce.entity.Order;
import com.oleksa.ecommerce.entity.State;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private Long state;
    private Long country;
    private String zipCode;
}
