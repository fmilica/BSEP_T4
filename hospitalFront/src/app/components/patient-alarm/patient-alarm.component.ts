import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
import { PatientAlarmPage } from 'src/app/model/patient-alarm-page.model';
import { PatientName } from 'src/app/model/patient-name.model';
import { PatientAlarmService } from 'src/app/services/patient-alarm.service';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-patient-alarm',
  templateUrl: './patient-alarm.component.html',
  styleUrls: ['./patient-alarm.component.sass']
})
export class PatientAlarmComponent implements OnInit {

  displayedColumns: string[] = ['name', 'message', 'timestamp'];
  dataSource: PatientAlarmPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  patients: PatientName[] = []

  filterForm: FormGroup;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private patientAlarmService: PatientAlarmService, private patientService: PatientService) {
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
    this.patientAlarmService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientAlarmPage: PatientAlarmPage) => (this.dataSource = patientAlarmPage)))
      .subscribe();
  }

  onPaginateChange(event: PageEvent): void {
    this.pageEvent = event;
    this.getNewPage(this.pageEvent.pageIndex, this.pageEvent.pageSize);
  }

  getNewPage(index: number, size: number): void {
    let patientId = this.filterForm.get('patientFilter').value
    if (patientId != '') {
      this.patientAlarmService
      .findAllByPageByPatient(this.pageEvent.pageIndex, this.pageEvent.pageSize, patientId)
      .pipe(map((patientAlarmPage: PatientAlarmPage) => (this.dataSource = patientAlarmPage)))
      .subscribe();
    } else {
      this.patientAlarmService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientAlarmPage: PatientAlarmPage) => (this.dataSource = patientAlarmPage)))
      .subscribe();
    }
  }

  onFilter(filterPatientStatuses: FormGroupDirective): void {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
    let patientId = this.filterForm.get('patientFilter').value
    if (patientId != '') {
      this.patientAlarmService
      .findAllByPageByPatient(this.pageEvent.pageIndex, this.pageEvent.pageSize, patientId)
      .pipe(map((patientAlarmPage: PatientAlarmPage) => (this.dataSource = patientAlarmPage)))
      .subscribe();
    } else {
      this.patientAlarmService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientAlarmPage: PatientAlarmPage) => (this.dataSource = patientAlarmPage)))
      .subscribe();
    }
  }

}
