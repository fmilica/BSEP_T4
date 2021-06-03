import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
import { PatientAlarmPage } from 'src/app/model/patient-alarm-page.model';
import { PatientAlarmService } from 'src/app/services/patient-alarm.service';

@Component({
  selector: 'app-patient-alarm',
  templateUrl: './patient-alarm.component.html',
  styleUrls: ['./patient-alarm.component.sass']
})
export class PatientAlarmComponent implements OnInit {

  displayedColumns: string[] = ['name', 'message', 'timestamp'];
  dataSource: PatientAlarmPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private patientAlarmService: PatientAlarmService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
  }

  ngOnInit(): void {
    this.initDataSource();
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
    this.patientAlarmService
      .findAllByPage(index, size)
      .pipe(map((patientAlarmPage: PatientAlarmPage) => (this.dataSource = patientAlarmPage)))
      .subscribe();
  }

}
