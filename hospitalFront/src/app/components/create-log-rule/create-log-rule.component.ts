import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { LogRuleConditionDto } from 'src/app/dto/log-rule-condition-dto.dto';
import { LogRuleConditionListDto } from 'src/app/dto/log-rule-condition-list-dto.dto';
import { LogRuleDto } from 'src/app/dto/log-rule-dto.dto';
import { RuleDto } from 'src/app/dto/rule-dto';
import { LogService } from 'src/app/services/log.service';

@Component({
  selector: 'app-create-log-rule',
  templateUrl: './create-log-rule.component.html',
  styleUrls: ['./create-log-rule.component.sass']
})
export class CreateLogRuleComponent implements OnInit {

  logAlarmTypes = ['BRUTE_FORCE', 'DOS', 'ERROR_LOG', 'FAILED_LOGIN', 'BLACKLIST_IP', 'NEW_BLACKLIST_IP']

  opers: string = "==|!=";

  levels: string = "INFO|WARN|ERROR"

  customAlarmForm: FormGroup;

  constructor(
    private toastr: ToastrService,
    private logService: LogService
  ) {
    this.customAlarmForm = new FormGroup({
      logAlarmType: new FormControl('', [Validators.required]),
      logAlarmMessage: new FormControl('', [Validators.required]),
      level: new FormControl(''),
      operatorLevel: new FormControl('', Validators.pattern(this.opers)),
      valueLevel: new FormControl(''),
      message: new FormControl(''),
      operatorMessage: new FormControl('', Validators.pattern(this.opers)),
      valueMessage: new FormControl(''),
      source: new FormControl(''),
      operatorSource: new FormControl('', Validators.pattern(this.opers)),
      valueSource: new FormControl(''),
      type: new FormControl(''),
      operatorType: new FormControl('', Validators.pattern(this.opers)),
      valueType: new FormControl(''),
      ipAddress: new FormControl(''),
      operatorIpAddress: new FormControl('', Validators.pattern(this.opers)),
      valueIpAddress: new FormControl(''),
      error: new FormControl(''),
      operatorError: new FormControl('', Validators.pattern(this.opers)),
      valueError: new FormControl(''),
      statusCode: new FormControl(''),
      operatorStatusCode: new FormControl('', Validators.pattern(this.opers)),
      valueStatusCode: new FormControl(''),
    })
  }

  ngOnInit(): void {
  }

  onSubmit(createCustomAlarmDirective: FormGroupDirective) {
    if (this.customAlarmForm.invalid) {
      return;
    }

    if(!this.customAlarmForm.get('level').value && !this.customAlarmForm.get('message').value && !this.customAlarmForm.get('source').value && 
    !this.customAlarmForm.get('type').value && !this.customAlarmForm.get('ipAddress').value && !this.customAlarmForm.get('error').value
    && !this.customAlarmForm.get('statusCode').value) {
      this.toastr.error("Select at least one checkbox!");
      return;
    }

    let logRuleConditionDto: LogRuleConditionDto[] = [];

    if (!this.validateForm(logRuleConditionDto)) {
        return;
    }

    let logRuleConditionListDto = new LogRuleConditionListDto(logRuleConditionDto);

    let logAlarmType = this.customAlarmForm.get('logAlarmType').value
    let logAlarmMessage = this.customAlarmForm.get('logAlarmMessage').value

    let logRuleDTO = new LogRuleDto(logRuleConditionListDto, logAlarmType, logAlarmMessage);
   
    this.toastr.info("Alarm for log alarm type " + logAlarmType + " and message " + logAlarmMessage +" successfuly created!" );

    //console.log(logRuleDTO)
    this.logService.createRule(logRuleDTO).subscribe();
  }

  validateForm(logRuleConditionDto) {
    if(this.validateGroup('Level', 'level', 'operatorLevel', 'valueLevel', logRuleConditionDto) && 
      this.validateGroup('Message', 'message', 'operatorMessage', 'valueMessage', logRuleConditionDto) && 
      this.validateGroup('Source', 'source', 'operatorSource', 'valueSource', logRuleConditionDto) &&
      this.validateGroup('Type', 'type', 'operatorType', 'valueType', logRuleConditionDto) &&
      this.validateGroup('Ip address', 'ipAddress', 'operatorIpAddress', 'valueIpAddress', logRuleConditionDto) &&
      this.validateGroup('Error', 'error', 'operatorError', 'valueError', logRuleConditionDto) &&
      this.validateGroup('Status code', 'statusCode', 'operatorStatusCode', 'valueStatusCode', logRuleConditionDto) ) 
    {
      return true;
    }
    return false;
  }

  validateGroup(text, checked, operator, value, logRuleConditionDto) {
    if (this.customAlarmForm.get(checked).value) {
      let hrOper = this.customAlarmForm.get(operator).value;
      let hrValue = this.customAlarmForm.get(value).value;
      if (!hrOper || !hrValue) {
        this.toastr.error(text + " selected, please choose values!");
        return false;
      }
      let logRuleCondition = new LogRuleConditionDto(checked, hrOper, hrValue);
      logRuleConditionDto.push(logRuleCondition);
    }
    return true;
  }

}
