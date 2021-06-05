import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective } from '@angular/forms';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.sass']
})
export class ReportsComponent implements OnInit {

  reportForm: FormGroup;

  constructor() {
    this.reportForm = new FormGroup({
      fromDate: new FormControl(''),
      toDate: new FormControl('')
    })
   }

  ngOnInit(): void {
  }

  createReport(reportForm: FormGroupDirective): void {
    console.log("Hello")
  }

}
