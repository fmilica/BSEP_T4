import { NestedTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { map } from 'rxjs/operators';
import { CertificateView } from 'src/app/model/certificate-view.model';
import { CertificateService } from 'src/app/services/certificate.service';



/*const TREE_DATA: CertificateView[] = [
  {
    alias: 'root',
    status: 'VALID',
    children: [
      {
        alias: 'nas-prvi',
        status: 'REVOKED'
      },
      {
        alias: 'nas-drugi',
        status: 'VALID'
      },
      {
        alias: 'nas-CA',
        status: 'VALID',
        children: [
          {
            alias: 'ca-junior',
            status: 'VALID'
          }
        ]
      },
      {
        alias: 'nas-treci',
        status: 'VALID'
      },
    ]
  }
];*/

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.sass']
})
export class CertificatesComponent implements OnInit{
  treeControl = new NestedTreeControl<CertificateView>(node => node.children);
  dataSource = new MatTreeNestedDataSource<CertificateView>();

  constructor(
    private certificateService : CertificateService
  ) { }

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    // inicijalizacija certificate tree
    this.certificateService.getAllCertificates().pipe(
      map((certificateView: CertificateView[]) =>
      this.dataSource.data = certificateView
      )
    ).subscribe();
  }

  hasChild = (_: number, node: CertificateView) => !!node.children && node.children.length > 0;

  details(alias) {
    console.log(alias)
  }
}
