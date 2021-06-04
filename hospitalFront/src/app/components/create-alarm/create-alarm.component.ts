import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { map } from 'rxjs/operators';
import { RuleConditionDto } from 'src/app/dto/rule-condition-dto.dto';
import { RuleConditionListDto } from 'src/app/dto/rule-condition-list-dto.dto';
import { RuleDto } from 'src/app/dto/rule-dto';
import { PatientName } from 'src/app/model/patient-name.model';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-create-alarm',
  templateUrl: './create-alarm.component.html',
  styleUrls: ['./create-alarm.component.sass']
})
export class CreateAlarmComponent implements OnInit {

  opers: string = "<=|<|==|>|>=";

  customAlarmForm: FormGroup;

  patients: PatientName[] = []

  constructor(
    private toastr: ToastrService,
    private patientService: PatientService
  ) {
    this.customAlarmForm = new FormGroup({
      patient: new FormControl('', [Validators.required]),
      heartRate: new FormControl(''),
      operatorHeartRate: new FormControl('', Validators.pattern(this.opers)),
      valueHeartRate: new FormControl(''),
      lowerBloodPressure: new FormControl(''),
      operatorLowerPressure: new FormControl(''),
      valueLowerPressure: new FormControl(''),
      upperBloodPressure: new FormControl(''),
      operatorUpperPressure: new FormControl(''),
      valueUpperPressure: new FormControl(''),
      bodyTemperature: new FormControl(''),
      operatorTemperature: new FormControl(''),
      valueTemperature: new FormControl(''),
      respiratoryRate: new FormControl(''),
      operatorRespiratoryRate: new FormControl(''),
      valueRespiratoryRate: new FormControl('')
    })
  }

  ngOnInit(): void {
    this.patientService
      .findAll()
      .pipe(map((patients: PatientName[]) => (this.patients = patients)))
      .subscribe();
  }

  onSubmit(createCustomAlarmDirective: FormGroupDirective) {
    if (this.customAlarmForm.invalid) {
      return;
    }

    if(!this.customAlarmForm.get('heartRate').value && !this.customAlarmForm.get('lowerBloodPressure').value && !this.customAlarmForm.get('upperBloodPressure').value && 
    !this.customAlarmForm.get('bodyTemperature').value && !this.customAlarmForm.get('respiratoryRate').value) {
      this.toastr.error("Select at least one checkbox!");
      return;
    }

    let ruleConditionDto: RuleConditionDto[] = [];

    if (!this.validateForm(ruleConditionDto)) {
        return;
    }

    let ruleConditionListDto = new RuleConditionListDto(ruleConditionDto);

    let ruleDTO = new RuleDto(this.customAlarmForm.get('patient').value.id, ruleConditionListDto);
   
    this.toastr.info("Alarm for patient " + this.customAlarmForm.get('patient').value.name + " successfuly created!" );

    this.patientService.createRule(ruleDTO).subscribe();
  }

  validateForm(ruleConditionDto) {
    if(this.validateGroup('Heart rate', 'heartRate', 'operatorHeartRate', 'valueHeartRate', ruleConditionDto) && 
      this.validateGroup('Lower pressure', 'lowerBloodPressure', 'operatorLowerPressure', 'valueLowerPressure', ruleConditionDto) && 
      this.validateGroup('Upper pressure', 'upperBloodPressure', 'operatorUpperPressure', 'valueUpperPressure', ruleConditionDto) &&
      this.validateGroup('Temperature', 'bodyTemperature', 'operatorTemperature', 'valueTemperature', ruleConditionDto) &&
      this.validateGroup('Respiratory rate', 'respiratoryRate', 'operatorRespiratoryRate', 'valueRespiratoryRate', ruleConditionDto) ) 
    {
      return true;
    }
    return false;
  }

  validateGroup(text, checked, operator, value, ruleConditionDto) {
    if (this.customAlarmForm.get(checked).value) {
      let hrOper = this.customAlarmForm.get(operator).value;
      let hrValue = this.customAlarmForm.get(value).value;
      if (!hrOper || !hrValue) {
        this.toastr.error(text + " selected, please choose values!");
        return false;
      }
      let ruleCondition = new RuleConditionDto(checked, hrOper, hrValue);
      ruleConditionDto.push(ruleCondition);
    }
    return true;
  }
}
