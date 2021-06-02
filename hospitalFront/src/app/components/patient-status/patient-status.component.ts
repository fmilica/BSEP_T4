import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
import { PatientStatusPage } from 'src/app/model/patient-status-page.model';
import { PatientStatusService } from 'src/app/services/patient-status.service';

@Component({
  selector: 'app-patient-status',
  templateUrl: './patient-status.component.html',
  styleUrls: ['./patient-status.component.sass']
})
export class PatientStatusComponent implements OnInit {

  displayedColumns: string[] = ['name', 'heartRate', 'bloodPressure', 'bodyTemperature', 'respiratoryRate', 'timestamp'];
  dataSource: PatientStatusPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private patientStatusService: PatientStatusService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
  }

  ngOnInit(): void {
    this.initDataSource();
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
    this.patientStatusService
      .findAllByPage(index, size)
      .pipe(map((patientStatusPage: PatientStatusPage) => (this.dataSource = patientStatusPage)))
      .subscribe();
  }
}
