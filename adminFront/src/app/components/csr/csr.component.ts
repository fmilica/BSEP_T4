import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CSR } from 'src/app/model/csr.model';
import { CsrService } from 'src/app/services/csr.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styleUrls: ['./csr.component.sass']
})
export class CsrComponent implements OnInit {

  displayedColumns: string[] = ['commonName', 'name', 'surname', 'organizationName', 
                                'organizationUnit', 'country', 'email', 'status', 'actions'];

  dataSource: CSR[] =  [{commonName: 'common', name: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 0},
                            {commonName: 'common', name: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 1},
                            {commonName: 'common', name: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 1},
                            {commonName: 'common', name: 'ksenija', surname: 'prcic',
                            organizationName: 'kseno kompo', organizationUnit: 'sefovsko',
                            country: 'RS', email: 'ksenija.prcic1998@gmail.com', status: 2}
                          ]

  constructor(
    private csrService: CsrService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    //this.initDataSource();
  }

  initDataSource(): void {
    // inicijalizacija csr tabele
    this.csrService.getAllCertificateSigningRequests().pipe(
      map((csrData: CSR[]) =>
        this.dataSource = csrData
      )
    ).subscribe();
  }

  // accept csr
  acceptCsr() {}

  // decline csr
  declineCsr() {}
}
