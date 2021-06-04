import { Component, OnInit, ViewChild } from '@angular/core';
import { PatientService } from 'src/app/services/patient.service';
import { map } from 'rxjs/operators';
import { PatientPage } from 'src/app/model/patient-page.model';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.sass']
})
export class PatientComponent implements OnInit {

  displayedColumns: string[] = ['name', 'dateOfBirth', 'placeOfBirth', 'illnesses'];
  dataSource: PatientPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private patientService: PatientService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
  }

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    this.patientService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((patientPage: PatientPage) => (this.dataSource = patientPage)))
      .subscribe();
  }

  onPaginateChange(event: PageEvent): void {
    this.pageEvent = event;
    this.getNewPage(this.pageEvent.pageIndex, this.pageEvent.pageSize);
  }

  getNewPage(index: number, size: number): void {
    this.patientService
      .findAllByPage(index, size)
      .pipe(map((patientPage: PatientPage) => (this.dataSource = patientPage)))
      .subscribe();
  }
}
