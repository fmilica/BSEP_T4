import { LogRuleConditionDto } from "./log-rule-condition-dto.dto";

export class LogRuleConditionListDto {

    constructor(
        public ruleConditions: LogRuleConditionDto[],
    ) {}

}