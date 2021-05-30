import { Component, OnInit } from '@angular/core';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-patient-status',
  templateUrl: './patient-status.component.html',
  styleUrls: ['./patient-status.component.sass']
})
export class PatientStatusComponent implements OnInit {

  constructor(
    private patientService: PatientService
  ) { }

  ngOnInit(): void {
  }

}
