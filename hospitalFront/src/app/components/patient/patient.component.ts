import { Component, OnInit } from '@angular/core';
import { PatientService } from 'src/app/services/patient.service';
import { map } from 'rxjs/operators';
import { Patient } from 'src/app/model/patient.model';

@Component({
  selector: 'app-patient-status',
  templateUrl: './patient-status.component.html',
  styleUrls: ['./patient-status.component.sass']
})
export class PatientStatusComponent implements OnInit {

  displayedColumns: string[] = ['name', 'dateOfBirth', 'placeOfBirth', 'illnesses'];
  dataSource: Patient[] = [];

  ngOnInit(): void {
    this.initDataSource();
  }

  constructor(private patientService: PatientService) { }

  initDataSource(): void {
    this.patientService
      .findAllPatients()
      .pipe(map((patients: Patient[]) => (this.dataSource = patients)))
      .subscribe();
  }
}
