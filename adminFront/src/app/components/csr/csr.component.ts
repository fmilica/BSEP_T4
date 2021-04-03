import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CSR } from 'src/app/model/csr.model';
import { CsrService } from 'src/app/services/csr.service';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styleUrls: ['./csr.component.sass']
})
export class CsrComponent implements OnInit {

  displayedColumns: string[] = ['commonName', 'name', 'surname', 'organizationName', 
                                'organizationUnit', 'country', 'email', 'actions'];

  //dataSource: CSR[] = []
  dataSource: CSR[] =  [{commonName: 'common', givenName: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 'PENDING'},
                            {commonName: 'common', givenName: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 'APPROVED'},
                            {commonName: 'common', givenName: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 'APPROVED'},
                            {commonName: 'common', givenName: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 'DECLINED'}
                          ]

  constructor(
    private csrService: CsrService,
    private router: Router,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    // inicijalizacija csr tabele
    this.csrService.getAllCertificateSigningRequests().pipe(
      map((csrData: CSR[]) =>
        this.dataSource = csrData
      )
    ).subscribe();
  }

  // decline csr
  declineCsr(csr: CSR) {
    this.csrService.declineCertificateSigningRequest(csr.id)
    .subscribe(
      response => {
        this.toastr.success('Successfully declined CSR!');
        // reload tabele
        this.initDataSource();
      },
      error => {
        if (error.error.message){
          this.toastr.error(error.error.message);
        } else {
          this.toastr.error('503 Server Unavailable');
        }
      });
  }

  // accept csr
  acceptCsr(csr: CSR) {
    this.csrService.chosenCsr.next(csr);
    this.router.navigate(['homepage/create-certificate'])
  }
}
