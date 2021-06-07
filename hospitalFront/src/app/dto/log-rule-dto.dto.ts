import { LogRuleConditionListDto } from "./log-rule-condition-list-dto.dto";

export class LogRuleDto {

    constructor(
        public ruleConditions: LogRuleConditionListDto,
        public logAlarmType: string,
        public logAlarmMessage: string,
    ) {}

}