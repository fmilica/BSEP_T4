import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CertificateService } from 'src/app/services/certificate.service';

@Component({
  selector: 'app-revocation-dialog',
  templateUrl: './revocation-dialog.component.html',
  styleUrls: ['./revocation-dialog.component.sass']
})
export class RevocationDialogComponent implements OnInit {

  revocationForm: FormGroup

  //revocation reasons
  revocationReasons: string[] = ['KeyCompromise', 'CACompromise', 'AffiliationChanged', 'Superseded',
                                 'CessationOfOperation', 'CertificateHold', 'RemoveFromCRL', 'Unspecified' ];

  //chosen reason
  chosenReason: string = ''
  constructor(
    private certificateService: CertificateService,
    public dialogRef: MatDialogRef<RevocationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { certificateAlias: string}
  ) { 
    this.revocationForm = new FormGroup({
      predefinedReason: new FormControl('', [Validators.required])
    })
  }

  ngOnInit(): void {
  }

  close(): void {
    this.dialogRef.close();
  }

  revoke(): void {
    this.certificateService.revokeCertificate(this.data.certificateAlias, this.chosenReason)
      .subscribe( () => {
        this.close();
      }
      );
  }

}
