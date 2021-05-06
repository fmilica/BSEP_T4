import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CertificateAdditionalInfo } from 'src/app/model/certificate-additional-info.model';
import { Certificate } from 'src/app/model/certificate.model';
import { CreateCertificate } from 'src/app/model/create-certificate.model';
import { CertificateService } from 'src/app/services/certificate.service';
import { CsrService } from 'src/app/services/csr.service';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.sass']
})
export class CreateCertificateComponent implements OnInit {

  subjectInfoForm: FormGroup;
  generalInfoForm: FormGroup;
  issuerInfoForm: FormGroup;

  //default certificateType 
  cerType = "END_USER"

  // validTo
  minDate: Date;
  maxDate: Date;

  // signingCertificates
  createCA = false;
  signingCertificates: Certificate[] = []
  // [
  //   {alias: 'root', serialNumber: 1, id: 1},
  //   {alias: 'ca-inter', serialNumber: 2, id: 2}
  // ]
  chosenSigningCertificate = {alias: '', commonName: ''}

  constructor(
    private csrService: CsrService,
    private certificateService: CertificateService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.subjectInfoForm = new FormGroup({
      commonName: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[a-z.]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      givenName: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      surname: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      organizationName: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      organizationUnit: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      country: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.minLength(2), Validators.maxLength(2),
      Validators.pattern("AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BQ|BA|BW|BV|BR|IO|" +
      "BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CW|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|" +
      "ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GG|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|" +
      "IM|IL|IT|JM|JP|JE|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|" +
      "FM|MD|MC|MN|ME|MS|MA|MZ|MM|NA|NR|NP|NL|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|" +
      "RO|RU|RW|BL|SH|KN|LC|MF|PM|VC|WS|SM|ST|SA|SN|RS|SC|SL|SG|SX|SK|SI|SB|SO|ZA|GS|SS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|" +
      "TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW")]), 
      email: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.email,
        Validators.minLength(1), Validators.maxLength(50)]),
    });
    this.generalInfoForm = new FormGroup({
      certificateType: new FormControl('END_USER', [Validators.required, Validators.pattern("CA_CERT|TLS_SERVER|TLS_CLIENT|END_USER")]),
      startDate: new FormControl({ value: new Date(), disabled: true}, [Validators.required]),
      endDate: new FormControl('', [Validators.required]),
      // key usage
      digitalSignature: new FormControl(''),
      nonRepudiation: new FormControl(''),
      keyEncipherment: new FormControl(''),
      dataEncipherment: new FormControl(''),
      keyAgreement: new FormControl(''),
      keyCertSign: new FormControl(''),
      cRLSign: new FormControl(''),
      encipherOnly: new FormControl(''),
      decipherOnly: new FormControl(''),
      // extended key usage
      serverAuth: new FormControl(''),
      clientAuth: new FormControl(''),
      codeSigning: new FormControl(''),
      emailProtection: new FormControl(''),
      timeStamping: new FormControl(''),
      ocspSigning: new FormControl('')
    });
    this.issuerInfoForm = new FormGroup({
      signingCertificate: new FormControl('', [Validators.required]),
      commonName: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[a-z.]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      givenName: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      surname: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      organizationName: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      organizationUnit: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.pattern("[A-Z][a-zA-Z]+"), 
      Validators.minLength(1), Validators.maxLength(50)]),
      country: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.minLength(2), Validators.maxLength(2),
      Validators.pattern("AF|AX|AL|DZ|AS|AD|AO|AI|AQ|AG|AR|AM|AW|AU|AT|AZ|BS|BH|BD|BB|BY|BE|BZ|BJ|BM|BT|BO|BQ|BA|BW|BV|BR|IO|" +
      "BN|BG|BF|BI|KH|CM|CA|CV|KY|CF|TD|CL|CN|CX|CC|CO|KM|CG|CD|CK|CR|CI|HR|CU|CW|CY|CZ|DK|DJ|DM|DO|EC|EG|SV|GQ|ER|EE|" +
      "ET|FK|FO|FJ|FI|FR|GF|PF|TF|GA|GM|GE|DE|GH|GI|GR|GL|GD|GP|GU|GT|GG|GN|GW|GY|HT|HM|VA|HN|HK|HU|IS|IN|ID|IR|IQ|IE|" +
      "IM|IL|IT|JM|JP|JE|JO|KZ|KE|KI|KP|KR|KW|KG|LA|LV|LB|LS|LR|LY|LI|LT|LU|MO|MK|MG|MW|MY|MV|ML|MT|MH|MQ|MR|MU|YT|MX|" +
      "FM|MD|MC|MN|ME|MS|MA|MZ|MM|NA|NR|NP|NL|NC|NZ|NI|NE|NG|NU|NF|MP|NO|OM|PK|PW|PS|PA|PG|PY|PE|PH|PN|PL|PT|PR|QA|RE|" +
      "RO|RU|RW|BL|SH|KN|LC|MF|PM|VC|WS|SM|ST|SA|SN|RS|SC|SL|SG|SX|SK|SI|SB|SO|ZA|GS|SS|ES|LK|SD|SR|SJ|SZ|SE|CH|SY|TW|" +
      "TJ|TZ|TH|TL|TG|TK|TO|TT|TN|TR|TM|TC|TV|UG|UA|AE|GB|US|UM|UY|UZ|VU|VE|VN|VG|VI|WF|EH|YE|ZM|ZW")]),
      email: new FormControl({ value: '', disabled: true}, [Validators.required, Validators.email,
      Validators.minLength(1), Validators.maxLength(50)]),
    });
   }

  ngOnInit(): void {
    this.setSubjectInfo();
    this.setDates();
    this.setExtensions();
    this.getCertificates();
  }

  setSubjectInfo() {
    const chosenCsr = this.csrService.chosenCsr.getValue();
    this.subjectInfoForm.get('commonName').setValue(chosenCsr.commonName);
    this.subjectInfoForm.get('givenName').setValue(chosenCsr.name);
    this.subjectInfoForm.get('surname').setValue(chosenCsr.surname);
    this.subjectInfoForm.get('organizationName').setValue(chosenCsr.organizationName);
    this.subjectInfoForm.get('organizationUnit').setValue(chosenCsr.organizationUnit);
    this.subjectInfoForm.get('country').setValue(chosenCsr.country);
    this.subjectInfoForm.get('email').setValue(chosenCsr.email);
  }

  setDates() {
    const currentYear = new Date().getFullYear();
    const now = new Date();
    let endDate = new Date();
    let minDate = new Date();
    let maxDate = new Date();
    this.generalInfoForm.get('startDate').setValue(new Date());
    if (this.generalInfoForm.get('certificateType').value === 'CA_CERT') {
      // moze ga potpisati samo root
      this.createCA = true;
      endDate.setFullYear(currentYear + 5);
      this.generalInfoForm.get('endDate').setValue(endDate);
      // minimalno trajanje
      minDate.setFullYear(currentYear + 5);
      this.minDate = minDate;
      // maksimalno trajanje
      maxDate.setFullYear(currentYear + 10);
      this.maxDate = maxDate;
    } else {
      // moze ga potpisati bilo koji ca
      this.createCA = false;
      endDate.setFullYear(currentYear + 1);
      this.generalInfoForm.get('endDate').setValue(endDate);
      // maksimalno trajanje
      maxDate.setFullYear(currentYear + 3);
      this.maxDate = maxDate;
      // minimalno trajanje
      minDate.setMonth(now.getMonth() + 6);
      this.minDate = minDate;
    }
  }

  setExtensions() {
    let certType = this.generalInfoForm.get('certificateType').value;
    this.uncheckAll();
    switch (certType) {
      case "CA_CERT":
        this.generalInfoForm.get('digitalSignature').setValue(true);
        this.generalInfoForm.get('keyCertSign').setValue(true);
        this.generalInfoForm.get('dataEncipherment').setValue(true);
        break;
      case "TLS_SERVER":
        this.generalInfoForm.get('digitalSignature').setValue(true);
        this.generalInfoForm.get('dataEncipherment').setValue(true);
        this.generalInfoForm.get('keyAgreement').setValue(true);
        this.generalInfoForm.get('keyEncipherment').setValue(true);
        // extended key usage
        this.generalInfoForm.get('serverAuth').setValue(true);
        break;
      case "TLS_CLIENT":
        this.generalInfoForm.get('digitalSignature').setValue(true);
        this.generalInfoForm.get('dataEncipherment').setValue(true);
        this.generalInfoForm.get('keyAgreement').setValue(true);
        // extended key usage
        this.generalInfoForm.get('clientAuth').setValue(true);
        break;
      case "END_USER":
        this.generalInfoForm.get('digitalSignature').setValue(true);
        this.generalInfoForm.get('dataEncipherment').setValue(true);
        break;
      default:
        break;
    }
  }

  getCertificates() {
    if (this.createCA) {
      this.certificateService.getRootCertificate()
        .subscribe(
          (response) => {
            this.signingCertificates = [response];
            this.setIssuerData(response);
          }
        )
    } else {
      this.certificateService.getAllCACertificates()
        .subscribe(
          (response) => {
            if (!Array.isArray(response)) {
              response = [response]
            }
            this.signingCertificates = response;
          }
        )
    }
  }

  setIssuerData(certificate) {
    this.issuerInfoForm.get('commonName').setValue(certificate.commonName);
    this.issuerInfoForm.get('givenName').setValue(certificate.name);
    this.issuerInfoForm.get('surname').setValue(certificate.surname);
    this.issuerInfoForm.get('organizationName').setValue(certificate.organizationName);
    this.issuerInfoForm.get('organizationUnit').setValue(certificate.organizationUnit);
    this.issuerInfoForm.get('country').setValue(certificate.country);
    this.issuerInfoForm.get('email').setValue(certificate.email);
  }

  certificateChange(event) {
    if (event) {
      this.chosenSigningCertificate.alias = event.value;
      this.chosenSigningCertificate.commonName = event.source.triggerValue;
    }
    this.certificateService.getCertificate(
      this.chosenSigningCertificate.alias)
        .subscribe(
          (response) => {
            this.setIssuerData(response);
          }
        )
  }

  onSubmit() {
    if (this.generalInfoForm.invalid || this.issuerInfoForm.invalid) {
      return;
    }

    const chosenCsr = this.csrService.chosenCsr.getValue();
    const newCert: CreateCertificate = 
      new CreateCertificate(
        chosenCsr.id,
        this.chosenSigningCertificate.alias,
        this.generalInfoForm.get('startDate').value,
        this.generalInfoForm.get('endDate').value,
        new CertificateAdditionalInfo()
      )

    if (this.generalInfoForm.get('certificateType').value === "CA_CERT") {
      newCert.additionalInfo.ca = true;
    } else {
      newCert.additionalInfo.ca = false;
    }
    newCert.additionalInfo.keyUsages = this.getKeyUsages();
    newCert.additionalInfo.extendedKeyUsages = this.getExtendedKeyUsages();

    this.certificateService.createCertificate(newCert)
      .subscribe(
        response => {
          this.toastr.success('Successfully created certificate!');
          this.router.navigate(['homepage/csr']);
        },
        error => {
          if (error.error.fieldErrors) {
            this.toastr.error(error.error.fieldErrors[0].defaultMessage)
          } else {
            this.toastr.error('Invalid date format.')
          }
        });
  }

  getKeyUsages(): number[] {
    let keyUsages : number[] = [];
    if (this.generalInfoForm.get('digitalSignature').value) {
      keyUsages.push(128);
    }
    if (this.generalInfoForm.get('nonRepudiation').value) {
      keyUsages.push(64);
    }
    if (this.generalInfoForm.get('keyEncipherment').value) {
      keyUsages.push(32);
    }
    if (this.generalInfoForm.get('dataEncipherment').value) {
      keyUsages.push(16);
    }
    if (this.generalInfoForm.get('keyAgreement').value) {
      keyUsages.push(8);
    }
    if (this.generalInfoForm.get('keyCertSign').value) {
      keyUsages.push(4);
    }
    if (this.generalInfoForm.get('cRLSign').value) {
      keyUsages.push(2);
    }
    if (this.generalInfoForm.get('encipherOnly').value) {
      keyUsages.push(1);
    }
    if (this.generalInfoForm.get('decipherOnly').value) {
      keyUsages.push(32768);
    }
    return keyUsages;
  }

  getExtendedKeyUsages(): string[] {
    let extendedKeyUsages : string[] = [];
    if (this.generalInfoForm.get('serverAuth').value) {
      extendedKeyUsages.push('id_kp_serverAuth');
    }
    if (this.generalInfoForm.get('clientAuth').value) {
      extendedKeyUsages.push('id_kp_clientAuth');
    }
    if (this.generalInfoForm.get('codeSigning').value) {
      extendedKeyUsages.push('id_kp_codeSigning');
    }
    if (this.generalInfoForm.get('emailProtection').value) {
      extendedKeyUsages.push('id_kp_emailProtection');
    }
    if (this.generalInfoForm.get('timeStamping').value) {
      extendedKeyUsages.push('id_kp_timeStamping');
    }
    if (this.generalInfoForm.get('ocspSigning').value) {
      extendedKeyUsages.push('id_kp_OCSPSigning');
    }
    return extendedKeyUsages;
  }

  uncheckAll() {
    this.generalInfoForm.get('digitalSignature').setValue(false);
    this.generalInfoForm.get('nonRepudiation').setValue(false);
    this.generalInfoForm.get('keyEncipherment').setValue(false);
    this.generalInfoForm.get('dataEncipherment').setValue(false);
    this.generalInfoForm.get('keyAgreement').setValue(false);
    this.generalInfoForm.get('keyCertSign').setValue(false);
    this.generalInfoForm.get('cRLSign').setValue(false);
    this.generalInfoForm.get('encipherOnly').setValue(false);
    this.generalInfoForm.get('decipherOnly').setValue(false);
    // extended
    this.generalInfoForm.get('serverAuth').setValue(false);
    this.generalInfoForm.get('clientAuth').setValue(false);
    this.generalInfoForm.get('codeSigning').setValue(false);
    this.generalInfoForm.get('emailProtection').setValue(false);
    this.generalInfoForm.get('timeStamping').setValue(false);
    this.generalInfoForm.get('ocspSigning').setValue(false);
  }

  getFieldErrorMessage(fieldName: string): string {
    if (this.subjectInfoForm.controls[fieldName].touched) {
      if (this.subjectInfoForm.controls[fieldName].hasError('required')) {
        return 'Required field'
      }
      else if (this.subjectInfoForm.controls[fieldName].hasError('maxLength')) {
        return 'Field must not be more than 50 characters long'
      }
      else if(fieldName === "commonName") {
        if (this.subjectInfoForm.controls[fieldName].hasError('pattern')) {
          return 'Common name can only contain small letters and -'
        } 
      }
      else if(fieldName === "name" || fieldName === "surname") {
        if (this.subjectInfoForm.controls[fieldName].hasError('pattern')) {
          return fieldName[0].toUpperCase() + fieldName.substr(1).toLowerCase() + ' must start with a capital letter and can only contain small letters'
        } 
      }
      else if(fieldName === "organizationName") {
        if (this.subjectInfoForm.controls[fieldName].hasError('pattern')) {
          return 'Organization name must start with a capital letter and can only contain small letters'
        } 
      }
      else if(fieldName === "organizationUnit") {
        if (this.subjectInfoForm.controls[fieldName].hasError('pattern')) {
          return 'Organization unit must start with a capital letter and can only contain small letters'
        } 
      }
      else if (this.subjectInfoForm.controls[fieldName].hasError('email')) {
        return 'Email format is not valid'
      }
    }
    return '';
  }
}
