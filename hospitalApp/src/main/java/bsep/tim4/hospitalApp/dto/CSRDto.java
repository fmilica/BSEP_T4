package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CSRDto {

    @NotBlank(message = "Common name cannot be empty.")
    String commonName;

    @NotBlank(message = "Name cannot be empty.")
    String name;

    @NotBlank(message = "Surname cannot be empty.")
    String surname;

    @NotBlank(message = "Organization name cannot be empty.")
    String organizationName;

    @NotBlank(message = "Organization unit cannot be empty.")
    String organizationUnit;

    @NotBlank(message = "Country cannot be empty.")
    String country;

    @NotBlank(message = "Email cannot be empty.")
    String email;

    @NotBlank(message = "Key password cannot be empty.")
    String keyPassword;
}
