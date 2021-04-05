import { NestedTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { map } from 'rxjs/operators';
import { CertificateView } from 'src/app/model/certificate-view.model';
import { CertificateService } from 'src/app/services/certificate.service';
import { CertificateDetailsComponent } from '../certificate-details/certificate-details.component';
import { RevocationDialogComponent } from '../revocation-dialog/revocation-dialog.component';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.sass']
})
export class CertificatesComponent implements OnInit{
  treeControl = new NestedTreeControl<CertificateView>(node => node.children);
  dataSource = new MatTreeNestedDataSource<CertificateView>();

  constructor(
    private certificateService : CertificateService,
    private revokeDialog: MatDialog,
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
    this.certificateService.getDetails(alias)
    .subscribe(
      response => {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.data = response;
      const dialogRef = this.revokeDialog.open(CertificateDetailsComponent, dialogConfig);
    })
  }

  openRevokeDialog(alias) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {certificateAlias: alias}
    const dialogRef = this.revokeDialog.open(RevocationDialogComponent, dialogConfig);
  }
}
