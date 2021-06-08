import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective } from '@angular/forms';
import { Report } from 'src/app/model/report.model';
import { LogAlarmService } from 'src/app/services/log-alarm.service';
import { ReportService } from 'src/app/services/report.service';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.sass']
})
export class ReportsComponent implements OnInit {

  reportForm: FormGroup;

  chartTypeDoughnut: string = 'doughnut';

  logsDataset: Array<any> = [
    { data: [], label: 'Logs Report' }
  ];

  logAlarmsDataset: Array<any> = [
    { data: [], label: 'Log Alarms Report' }
  ];

  logLabels: Array<any> = ['INFO', 'WARN', 'ERROR'];
  logAlarmsLabels: Array<any> = [];

  logColors: Array<any> = [
    {
      backgroundColor: [
        'rgba(54, 162, 235, 0.2)',
        'rgba(255, 206, 86, 0.2)',
        'rgba(255, 99, 132, 0.2)',
      ],
      borderColor: [
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(255,99,132,1)',
      ],
      borderWidth: 2,
    }
  ];
  logAlarmsColors: Array<any> = [
    {
      backgroundColor: [
        'rgba(255, 99, 132, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(255, 206, 86, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(153, 102, 255, 0.2)',
        'rgba(255, 159, 64, 0.2)',
        'rgba(143, 232, 95, 0.2)',
        'rgba(120, 95, 232, 0.2)',
        'rgba(232, 95, 218, 0.2)',
        'rgba(95, 223, 232, 0.2)',
        'rgba(95, 100, 232, 0.2)'
      ],
      borderColor: [
        'rgba(255,99,132,1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(75, 192, 192, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)',
        'rgba(143, 232, 95, 1)',
        'rgba(120, 95, 232, 1)',
        'rgba(232, 95, 218, 1)',
        'rgba(95, 223, 232, 1)',
        'rgba(95, 100, 232, 1)'
      ],
      borderWidth: 2,
    }
  ];

  chartOptions: any = {
    responsive: true
  };

  showCharts = false;
  frequentSources = []
  displayedColumns = ['id', 'total']
  totalLogNumber;
  totalLogAlarmNumber;

  constructor(
    private reportService: ReportService,
    private logAlarmService: LogAlarmService
  ) {
    this.reportForm = new FormGroup({
      fromDate: new FormControl(''),
      toDate: new FormControl(''),
      sourcesNumber: new FormControl(1)
    })
  }

  ngOnInit(): void {
    this.logAlarmService.findAllLogAlarmTypes()
      .subscribe(
        response => {
          this.logAlarmsLabels = response;
        },
        error => {
        });
  }

  createReport(reportForm: FormGroupDirective): void {
    let report: Report = new Report(this.reportForm.get('fromDate').value, this.reportForm.get('toDate').value,
      this.reportForm.get('sourcesNumber').value);
    this.reportService.createReport(report)
      .subscribe(
        response => {
          this.logsDataset = response.logsByLevel;
          this.logAlarmsDataset = response.logAlarmsByType;
          this.totalLogNumber = response.totalLogs;
          this.totalLogAlarmNumber = response.totalLogAlarms;
          this.frequentSources = response.frequentSource;
          this.showCharts = true;
          console.log(response);
        },
        error => {
          reportForm.resetForm();
          this.reportForm.reset();
        });
  }

}
