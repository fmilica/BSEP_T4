import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styleUrls: ['./csr.component.sass']
})
export class CsrComponent implements OnInit {

  displayedColumns: string[] = ['name'];

  dataSource =  {content: [{name: 'ImeValjda'}]}

  constructor() { }

  ngOnInit(): void {
  }

}
