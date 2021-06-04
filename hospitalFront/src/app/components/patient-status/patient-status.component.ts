import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
import { PatientName } from 'src/app/model/patient-name.model';
import { PatientStatusPage } from 'src/app/model/patient-status-page.model';
import { PatientStatusService } from 'src/app/services/patient-status.service';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-patient-status',
  templateUrl: './patient-status.component.html',
  styleUrls: ['./patient-status.component.sass']
})
export class PatientStatusComponent implements OnInit {

  displayedColumns: string[] = ['name', 'heartRate', 'bloodPressure', 'bodyTemperature', 'respiratoryRate', 'timestamp'];
  dataSource: PatientStatusPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  patients: PatientName[] = []

  filterForm: FormGroup;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private patientStatusService: PatientStatusService, private patientService: PatientService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
    this.filterForm = new FormGroup({
      patientFilter: new FormControl(''),
    })
  }

  ngOnInit(): void {
    this.initDataSource();
    this.patientService
      .findAll()
      .pipe(map((patients: PatientName[]) => (this.patients = patients)))
      .subscribe();
  }

  initDataSource(): void {
    this.patientStatusService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientStatusPage: PatientStatusPage) => (this.dataSource = patientStatusPage)))
      .subscribe();
  }

  onPaginateChange(event: PageEvent): void {
    this.pageEvent = event;
    this.getNewPage(this.pageEvent.pageIndex, this.pageEvent.pageSize);
  }

  getNewPage(index: number, size: number): void {
    let patientId = this.filterForm.get('patientFilter').value
    if (patientId != '') {
      this.patientStatusService
      .findAllByPageByPatient(this.pageEvent.pageIndex, this.pageEvent.pageSize, patientId)
      .pipe(map((patientStatusPage: PatientStatusPage) => (this.dataSource = patientStatusPage)))
      .subscribe();
    } else {
      this.patientStatusService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientStatusPage: PatientStatusPage) => (this.dataSource = patientStatusPage)))
      .subscribe();
    }
  }

  onFilter(filterPatientStatuses: FormGroupDirective): void {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
    let patientId = this.filterForm.get('patientFilter').value
    if (patientId != '') {
      this.patientStatusService
      .findAllByPageByPatient(this.pageEvent.pageIndex, this.pageEvent.pageSize, patientId)
      .pipe(map((patientStatusPage: PatientStatusPage) => (this.dataSource = patientStatusPage)))
      .subscribe();
    } else {
      this.patientStatusService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientStatusPage: PatientStatusPage) => (this.dataSource = patientStatusPage)))
      .subscribe();
    }
  }
}
