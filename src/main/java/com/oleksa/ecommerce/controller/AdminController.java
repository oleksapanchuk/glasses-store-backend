package com.oleksa.ecommerce.controller;

import com.oleksa.ecommerce.constants.AppConstants;
import com.oleksa.ecommerce.dto.OrderDto;
import com.oleksa.ecommerce.dto.ResponseDto;
import com.oleksa.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.oleksa.ecommerce.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@AllArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/fetch-orders-by-tracking-number/{tracking-number}")
    public ResponseEntity<Page<OrderDto>> fetchOrderById(
            @PathVariable(name = "tracking-number") String trackingNumber
    ) {

        Page<OrderDto> orderDtoPage = adminService.fetchOrdersByTrackingNumber(trackingNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderDtoPage);
    }

    @PatchMapping("/update-order-status")
    public ResponseEntity<ResponseDto> fetchOrderById(
            @RequestBody UpdateOrderStatusRequest updateOrderStatusRequest
    ) {

        boolean isUpdated = adminService.updateOrderStatus(
                updateOrderStatusRequest.getOrderId(),
                updateOrderStatusRequest.getOrderStatus()
        );

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

}
