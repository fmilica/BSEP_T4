import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-certificate',
  templateUrl: './create-certificate.component.html',
  styleUrls: ['./create-certificate.component.sass']
})
export class CreateCertificateComponent implements OnInit {

  subjectInfoForm: FormGroup;
  generalInfoForm: FormGroup;
  issuerInfoFrom: FormGroup;

  constructor() {
    this.subjectInfoForm = new FormGroup({
      commonName: new FormControl({ value: '', diabled: true}, []),
      givenName: new FormControl({ value: '', diabled: true}, []),
      surname: new FormControl({ value: '', diabled: true}, []),
      organizationName: new FormControl({ value: '', diabled: true}, []),
      organizationUnit: new FormControl({ value: '', diabled: true}, []),
      country: new FormControl({ value: '', diabled: true}, []),
      email: new FormControl({ value: '', diabled: true}, []),
    });
    this.generalInfoForm = new FormGroup({
      certificateType: new FormControl('', [Validators.required]),
      startDate: new FormControl({ value: '', diabled: true}, []),
      endDate: new FormControl({ value: '', diabled: true}, []),
      // key usage
      digitalSignature: new FormControl(''),
      nonRepudiation: new FormControl(''),
      keyEncipherment: new FormControl(''),
    });
    this.issuerInfoFrom = new FormGroup({
      signingCertificate: new FormControl('', [Validators.required]),
      commonName: new FormControl({ value: '', diabled: true}, []),
      givenName: new FormControl({ value: '', diabled: true}, []),
      surname: new FormControl({ value: '', diabled: true}, []),
      organizationName: new FormControl({ value: '', diabled: true}, []),
      organizationUnit: new FormControl({ value: '', diabled: true}, []),
      country: new FormControl({ value: '', diabled: true}, []),
      email: new FormControl({ value: '', diabled: true}, []),
    });
   }

  ngOnInit(): void {
  }

}
