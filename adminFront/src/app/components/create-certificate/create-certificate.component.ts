import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Certificate } from 'src/app/model/certificate.model';
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
  issuerInfoFrom: FormGroup;

  // validTo
  minDate: Date;
  maxDate: Date;

  // signingCertificates
  createCA = false;
  signingCertificates: Certificate[] = 
  [
    {alias: 'root', serialNumber: 1, id: 1},
    {alias: 'ca-inter', serialNumber: 2, id: 2}
  ]
  chosenSigningCertificate = {id: -1, alias: ''}

  constructor(
    private csrService: CsrService,
    private certificateService: CertificateService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.subjectInfoForm = new FormGroup({
      commonName: new FormControl({ value: '', disabled: true}, []),
      givenName: new FormControl({ value: '', disabled: true}, []),
      surname: new FormControl({ value: '', disabled: true}, []),
      organizationName: new FormControl({ value: '', disabled: true}, []),
      organizationUnit: new FormControl({ value: '', disabled: true}, []),
      country: new FormControl({ value: '', disabled: true}, []),
      email: new FormControl({ value: '', disabled: true}, []),
    });
    this.generalInfoForm = new FormGroup({
      certificateType: new FormControl('', [Validators.required]),
      startDate: new FormControl({ value: new Date(), disabled: true}, []),
      endDate: new FormControl('', [Validators.required]),
      // key usage
      // digitalSignature: new FormControl(''),
      // nonRepudiation: new FormControl(''),
      // keyEncipherment: new FormControl(''),
    });
    this.issuerInfoFrom = new FormGroup({
      signingCertificate: new FormControl('', [Validators.required]),
      commonName: new FormControl({ value: '', disabled: true}, []),
      givenName: new FormControl({ value: '', disabled: true}, []),
      surname: new FormControl({ value: '', disabled: true}, []),
      organizationName: new FormControl({ value: '', disabled: true}, []),
      organizationUnit: new FormControl({ value: '', disabled: true}, []),
      country: new FormControl({ value: '', disabled: true}, []),
      email: new FormControl({ value: '', disabled: true}, []),
    });
   }

  ngOnInit(): void {
    this.setSubjectInfo();
    this.setDates();
    //this.getCertificates();
  }

  setSubjectInfo() {
    const chosenCsr = this.csrService.chosenCsr.getValue();
    this.subjectInfoForm.get('commonName').setValue(chosenCsr.commonName);
    this.subjectInfoForm.get('givenName').setValue(chosenCsr.givenName);
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
    if (this.generalInfoForm.get('certificateType').value === 'intermediate') {
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
            this.signingCertificates = response;
          }
        )
    }
  }

  setIssuerData(certificate) {
    this.subjectInfoForm.get('commonName').setValue(certificate.commonName);
    this.subjectInfoForm.get('givenName').setValue(certificate.givenName);
    this.subjectInfoForm.get('surname').setValue(certificate.surname);
    this.subjectInfoForm.get('organizationName').setValue(certificate.organizationName);
    this.subjectInfoForm.get('organizationUnit').setValue(certificate.organizationUnit);
    this.subjectInfoForm.get('country').setValue(certificate.country);
    this.subjectInfoForm.get('email').setValue(certificate.email);
  }

  certificateChange(event) {
    if (event) {
      this.chosenSigningCertificate.id = event.value;
      this.chosenSigningCertificate.alias = event.source.triggerValue;
    }
    this.certificateService.getCertificate(
      this.chosenSigningCertificate.id, this.chosenSigningCertificate.alias)
        .subscribe(
          (response) => {
            this.signingCertificates = [response];
            this.setIssuerData(response);
          }
        )
  }

  submit() {
    
  }
}
