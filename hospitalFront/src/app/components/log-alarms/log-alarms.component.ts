import { Component, OnInit, ViewChild } from '@angular/core';
import { PageEvent, MatPaginator } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
import { LogAlarmPage } from 'src/app/model/log-alarm-page.model';
import { LogAlarmService } from 'src/app/services/log-alarm.service';

@Component({
  selector: 'app-log-alarms',
  templateUrl: './log-alarms.component.html',
  styleUrls: ['./log-alarms.component.sass']
})
export class LogAlarmsComponent implements OnInit {

  displayedColumns: string[] = ['type', 'message', 'timestamp'];
  dataSource: LogAlarmPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private logAlarmService: LogAlarmService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
  }

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    this.logAlarmService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((logAlarmPage: LogAlarmPage) => (this.dataSource = logAlarmPage)))
      .subscribe();
  }

  onPaginateChange(event: PageEvent): void {
    this.pageEvent = event;
    this.getNewPage(this.pageEvent.pageIndex, this.pageEvent.pageSize);
  }

  getNewPage(index: number, size: number): void {
    this.logAlarmService
    .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
    .pipe(map((logAlarmPage: LogAlarmPage) => (this.dataSource = logAlarmPage)))
    .subscribe();
  }

}
