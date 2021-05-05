package bsep.tim4.hospitalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CSRDto {

    @NotBlank(message = "Common name cannot be empty.")
    @Size( min = 1, max = 50, message = "Common name is too long")
    @Pattern(regexp = "[a-z.]+", message = "Common name is not valid")
    String commonName;

    @NotBlank(message = "Name cannot be empty.")
    @Size( min = 1, max = 50, message = "Name is too long")
    @Pattern(regexp = "[A-Z][a-zA-Z]+", message = "Name is not valid")
    String name;

    @NotBlank(message = "Surname cannot be empty.")
    @Size( min = 1, max = 50, message = "Surname is too long")
    @Pattern(regexp = "[A-Z][a-zA-Z]+", message = "Surname is not valid")
    String surname;

    @NotBlank(message = "Organization name cannot be empty.")
    @Size( min = 1, max = 50, message = "Organization name is too long")
    @Pattern(regexp = "[A-Z][a-zA-Z]+", message = "Organization name is not valid")
    String organizationName;

    @NotBlank(message = "Organization unit cannot be empty.")
    @Size( min = 1, max = 50, message = "Organization unit is too long")
    @Pattern(regexp = "[A-Z][a-zA-Z]+", message = "Organization unit is not valid")
    String organizationUnit;

    @NotBlank(message = "Country cannot be empty.")
    @Size( min = 2, max = 2, message = "Country should be exactly two characters long")
    @Pattern(regexp = "AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BQ|BA|BW|BV|BR|IO|" +
            "BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CW|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|" +
            "ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GG|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|" +
            "IM|IL|IT|JM|JP|JE|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|" +
            "FM|MD|MC|MN|ME|MS|MA|MZ|MM|NA|NR|NP|NL|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|" +
            "RO|RU|RW|BL|SH|KN|LC|MF|PM|VC|WS|SM|ST|SA|SN|RS|SC|SL|SG|SX|SK|SI|SB|SO|ZA|GS|SS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|" +
            "TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW", message = "Country is not valid")
    String country;

    @NotBlank(message = "Email cannot be empty.")
    @Size( min = 1, max = 50, message = "Email is too long")
    @Email(message = "Email is not valid")
    String email;
}
