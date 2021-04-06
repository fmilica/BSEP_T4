import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
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

  constructor(
    private csrService : CsrService,
    private toastr: ToastrService,
    private router: Router
  ) { 
    this.newCSRForm = new FormGroup({
      commonName: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      surname: new FormControl('', [Validators.required]),
      organizationName: new FormControl('', [Validators.required]),
      organizationUnit: new FormControl('', [Validators.required]),
      country: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required]),
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
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
      this.newCSRForm.value.email,
      'key'
    );

    this.csrService.createCsr(csr)
    .subscribe(
      response => {
        this.toastr.success('Successfully created csr!');
        this.router.navigate(['homepage/csr']);
        this.newCSRForm.reset();
      },
      error => {
        this.toastr.success('Successfully created csr!');
        this.newCSRForm.reset();
      });
  }

  getRequiredFieldErrorMessage(fieldName: string): string {
    if (this.newCSRForm.controls[fieldName].touched) {
      return this.newCSRForm.controls[fieldName].hasError('required')
        ? 'Required field'
        : '';
    }
    return '';
  }

}
