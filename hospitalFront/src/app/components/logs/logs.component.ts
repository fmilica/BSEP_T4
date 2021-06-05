import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
import { LogsFilterDto } from 'src/app/dto/logs-filter-dto.dto';
import { LogPage } from 'src/app/model/log-page.model';
import { LogService } from 'src/app/services/log.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.sass']
})
export class LogsComponent implements OnInit {

  displayedColumns: string[] = ['level', 'message', 'timestamp'];
  dataSource: LogPage = {content: [], totalElements: 0, totalPages: 0, size: 0};
  pageEvent: PageEvent = new PageEvent();

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  filterForm: FormGroup;

  levels: string[] = ['ERROR', 'WARN', 'INFO']

  constructor(private logService: LogService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
    this.filterForm = new FormGroup({
      level: new FormControl(''),
      fromDate: new FormControl(''),
      toDate: new FormControl(''),
      message: new FormControl(''),
      source: new FormControl(''),
      ip: new FormControl(''),
    })
  }

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    this.logService
      .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
      .pipe(map((logPage: LogPage) => (this.dataSource = logPage)))
      .subscribe();
  }

  onPaginateChange(event: PageEvent): void {
    this.pageEvent = event;
    this.getNewPage(this.pageEvent.pageIndex, this.pageEvent.pageSize);
  }

  getNewPage(index: number, size: number): void {
    this.logService
    .findAllByPage(this.pageEvent.pageIndex, this.pageEvent.pageSize)
    .pipe(map((logPage: LogPage) => (this.dataSource = logPage)))
    .subscribe();
  }

  onFilter(filterLogs: FormGroupDirective): void {
    let level = this.filterForm.get('level').value
    let from = this.filterForm.get('fromDate').value
    let to = this.filterForm.get('toDate').value
    let message = this.filterForm.get('message').value
    let source = this.filterForm.get('source').value
    let ip = this.filterForm.get('ip').value
    let logsFilter: LogsFilterDto = new LogsFilterDto(level, from, to, message, source, ip)

    console.log(logsFilter)

    this.logService
    .findAllFilter(this.pageEvent.pageIndex, this.pageEvent.pageSize, logsFilter)
    .pipe(map((logsPage: LogPage) => (this.dataSource = logsPage)))
    .subscribe();
  }

}
