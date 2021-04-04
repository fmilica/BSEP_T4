import { NestedTreeControl } from '@angular/cdk/tree';
import { Component, OnInit } from '@angular/core';
import { MatTreeNestedDataSource } from '@angular/material/tree';

/**
 * Food data with nested structure.
 * Each node has a name and an optional list of children.
 */
 interface CertificateView {
  alias: string;
  status: string;
  children?: CertificateView[];
}

const TREE_DATA: CertificateView[] = [
  {
    alias: 'root',
    status: 'VALID',
    children: [
      {
        alias: 'nas-prvi',
        status: 'REVOKED'
      },
      {
        alias: 'nas-drugi',
        status: 'VALID'
      },
      {
        alias: 'nas-CA',
        status: 'VALID',
        children: [
          {
            alias: 'ca-junior',
            status: 'VALID'
          }
        ]
      },
      {
        alias: 'nas-treci',
        status: 'VALID'
      },
    ]
  }
];

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.sass']
})
export class CertificatesComponent {
  treeControl = new NestedTreeControl<CertificateView>(node => node.children);
  dataSource = new MatTreeNestedDataSource<CertificateView>();

  constructor() {
    this.dataSource.data = TREE_DATA;
  }

  hasChild = (_: number, node: CertificateView) => !!node.children && node.children.length > 0;

  details(alias) {
    console.log(alias)
  }
}
