export class LogRuleConditionDto {

    constructor(
        public conditionType: string,
        public conditionOperator: string,
        public value: string,
    ) {}

}