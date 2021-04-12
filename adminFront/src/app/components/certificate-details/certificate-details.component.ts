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
    this.certificateService.downloadCertificate(this.data.certAlias).subscribe(
      response => {
        let type = "application/crt";
        let blob = new Blob([response], { type: type});
        let url = window.URL.createObjectURL(blob);
        var link = document.createElement('a');
        link.href = url;
        link.download = this.data.commonName + this.data.serialNumb + ".crt";
        link.click();
      }
    )
  }

  downloadPkcs12(): void {
    this.certificateService.downloadPkcs12(this.data.certAlias).subscribe(
      response => {
        let type = "application/crt";
        let blob = new Blob([response], { type: type});
        let url = window.URL.createObjectURL(blob);
        var link = document.createElement('a');
        link.href = url;
        link.download = this.data.commonName + "_" + this.data.serialNumb + ".p12";
        link.click();
      }
    )
  }

}
