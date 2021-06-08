import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { HospitalService } from 'src/app/services/hospital.service';

@Component({
  selector: 'app-hospital-config',
  templateUrl: './hospital-config.component.html',
  styleUrls: ['./hospital-config.component.sass']
})
export class HospitalConfigComponent implements OnInit {

  displayedColumns: string[] = ['hospitalName', 'hospitalSimulators']
  dataSource: [] = []
  simulators: [] = []
  removeSimulators: [] = []

  addSimulatorForm: FormGroup;
  removeSimulatorForm: FormGroup;

  constructor(
    private hospitalService: HospitalService,
    private toastr: ToastrService
  ) {
    this.addSimulatorForm = new FormGroup({
      hospital: new FormControl(''),
      simulator: new FormControl(''),
      readInterval: new FormControl(0),
      filter: new FormControl('')
    });
    this.removeSimulatorForm = new FormGroup({
      hospital: new FormControl(''),
      simulator: new FormControl('')
    });
  }

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    // inicijalizacija hospital-simulator tabele
    this.hospitalService.getAllHospitals().pipe(
      map((hospitalData: []) => {
        this.dataSource = hospitalData;
      })
    ).subscribe();
  }

  onHospitalSelected() {
    let hospitalId = this.addSimulatorForm.get('hospital').value;
    this.hospitalService.getAllNotHospitalSimulators(hospitalId).pipe(
      map((simulators: []) => {
        this.simulators = simulators;
      })
    ).subscribe();
  }

  onRemoveHospitalSelected() {
    let hospitalId = this.removeSimulatorForm.get('hospital').value;
    this.hospitalService.getAllHospitalSimulators(hospitalId).pipe(
      map((simulators: []) => {
        this.removeSimulators = simulators;
      })
    ).subscribe();
  }

  onAddSubmit() {
    let hospitalId = this.addSimulatorForm.get('hospital').value;
    let simulatorId = this.addSimulatorForm.get('simulator').value;
    let readInterval = this.addSimulatorForm.get('readInterval').value;
    let filter = this.addSimulatorForm.get('filter').value;
    let simulator = [{ "simulatorId": simulatorId, "readInterval": readInterval, "filter": filter }]
    this.hospitalService.addSimulator(hospitalId, simulator)
      .subscribe(
        response => {
          this.toastr.success('Successfully configured hospital!');
          this.initDataSource();
        },
        error => {
          this.toastr.error(error.error.fieldErrors[0].defaultMessage);
        }
      );
  }

  onRemoveSubmit() {
    let hospitalId = this.removeSimulatorForm.get('hospital').value;
    let simulatorId = this.removeSimulatorForm.get('simulator').value;
    let simulator = [{ "simulatorId": simulatorId }]
    this.hospitalService.removeSimulator(hospitalId, simulator)
      .subscribe(
        response => {
          this.toastr.success('Successfully configured hospital!');
          this.initDataSource();
        },
        error => {
          this.toastr.error(error.error.fieldErrors[0].defaultMessage);
        }
      );
  }

}
