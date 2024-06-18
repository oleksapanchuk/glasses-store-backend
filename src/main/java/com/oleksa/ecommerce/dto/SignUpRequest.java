package com.oleksa.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Registration Request")
public class SignUpRequest {

    @Schema(description = "First Name", example = "Jon")
    @Size(min = 5, max = 100, message = "First Name must contain from 5 to 100 characters")
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @Schema(description = "Last Name", example = "Doe")
    @Size(min = 5, max = 100, message = "Last Name must contain from 5 to 100 characters")
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Schema(description = "E-mail address", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "The email address must contain between 5 and 255 characters")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "Email address must be in the format user@example.com")
    private String email;

    @Schema(description = "Password", example = "my_1secret1_password")
    @Size(max = 100, message = "Password length must be no more than 100 characters")
    private String password;
}
