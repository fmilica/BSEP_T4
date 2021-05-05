import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CSR } from 'src/app/model/csr.model';
import { CsrService } from 'src/app/services/csr.service';

@Component({
  selector: 'app-create-csr',
  templateUrl: './create-csr.component.html',
  styleUrls: ['./create-csr.component.sass']
})
export class CreateCsrComponent implements OnInit {

  newCSRForm: FormGroup;

  countryValues = "AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BQ|BA|BW|BV|BR|IO|" +
      "BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CW|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|" +
      "ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GG|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|" +
      "IM|IL|IT|JM|JP|JE|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|" +
      "FM|MD|MC|MN|ME|MS|MA|MZ|MM|NA|NR|NP|NL|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|" +
      "RO|RU|RW|BL|SH|KN|LC|MF|PM|VC|WS|SM|ST|SA|SN|RS|SC|SL|SG|SX|SK|SI|SB|SO|ZA|GS|SS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|" +
      "TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW"

  constructor(
    private csrService : CsrService,
    private toastr: ToastrService,
    private router: Router
  ) { 
    this.newCSRForm = new FormGroup({
      commonName: new FormControl('', [Validators.required, Validators.pattern("[a-z.]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      name: new FormControl('', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"),
      Validators.minLength(1), Validators.maxLength(50)]),
      surname: new FormControl('', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"),
      Validators.minLength(1), Validators.maxLength(50)]),
      organizationName: new FormControl('', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"),
      Validators.minLength(1), Validators.maxLength(50)]),
      organizationUnit: new FormControl('', [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"),
      Validators.minLength(1), Validators.maxLength(50)]),
      country: new FormControl('AF', [Validators.required, Validators.minLength(2), Validators.maxLength(2),
      Validators.pattern("AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BQ|BA|BW|BV|BR|IO|" +
      "BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CW|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|" +
      "ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GG|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|" +
      "IM|IL|IT|JM|JP|JE|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|" +
      "FM|MD|MC|MN|ME|MS|MA|MZ|MM|NA|NR|NP|NL|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|" +
      "RO|RU|RW|BL|SH|KN|LC|MF|PM|VC|WS|SM|ST|SA|SN|RS|SC|SL|SG|SX|SK|SI|SB|SO|ZA|GS|SS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|" +
      "TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW")]),
      email: new FormControl('', [Validators.required, Validators.email,
      Validators.minLength(1), Validators.maxLength(50)]),
    });
  }

  ngOnInit(): void {
  }

  onSubmit(createCsrDirective: FormGroupDirective) {
    if (this.newCSRForm.invalid) {
      return;
    }

    const csr: CSR = new CSR (
      this.newCSRForm.value.commonName,
      this.newCSRForm.value.name,
      this.newCSRForm.value.surname,
      this.newCSRForm.value.organizationName,
      this.newCSRForm.value.organizationUnit,
      this.newCSRForm.value.country,
      this.newCSRForm.value.email
    );

    this.csrService.createCsr(csr)
    .subscribe(
      response => {
        this.toastr.success('Successfully created csr!');
        this.router.navigate(['homepage/csr']);
        this.newCSRForm.reset();
        createCsrDirective.resetForm();
      },
      error => {
        this.toastr.success('Successfully created csr!');
        this.newCSRForm.reset();
        createCsrDirective.resetForm();
      });
  }

  getFieldErrorMessage(fieldName: string): string {
    if (this.newCSRForm.controls[fieldName].touched) {
      if (this.newCSRForm.controls[fieldName].hasError('required')) {
        return 'Required field'
      } else if (this.newCSRForm.controls[fieldName].hasError('maxLength')) {
        return 'Field must not be more than 50 characters long'
      } else if (this.newCSRForm.controls[fieldName].hasError('pattern')) {
        return 'Invalid format'
      } else if (this.newCSRForm.controls[fieldName].hasError('email')) {
        return 'Invalid email format'
      }
    }
    return '';
  }


}
