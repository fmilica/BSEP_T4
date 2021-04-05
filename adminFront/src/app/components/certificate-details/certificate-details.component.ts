import { Byte } from '@angular/compiler/src/util';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CertificateService } from 'src/app/services/certificate.service';

@Component({
  selector: 'app-certificate-details',
  templateUrl: './certificate-details.component.html',
  styleUrls: ['./certificate-details.component.sass']
})
export class CertificateDetailsComponent implements OnInit {

  constructor(
    private certificateService: CertificateService,
    public dialogRef: MatDialogRef<CertificateDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: 
      { certAlias: string, serialNumb: string, version: number,
        validFrom: Date, validUntil: Date, signatureAlgorithm: string, publicKey: Byte[], 
        commonName: string, subjectData: string, issuerData: string }
  ) { }

  ngOnInit(): void {
  }

  close(): void {
    this.dialogRef.close();
  }

  download(): void {
    this.certificateService.downloadCertificate(this.data.certAlias).subscribe()
  }

}