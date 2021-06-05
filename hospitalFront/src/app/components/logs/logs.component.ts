import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { map } from 'rxjs/operators';
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

  constructor(private logService: LogService) {
    this.pageEvent.pageIndex = 0;
    this.pageEvent.pageSize = 5;
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

}
