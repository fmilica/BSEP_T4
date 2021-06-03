export class RuleConditionDto {

    constructor(
        public conditionType: string,
        public conditionOperator: string,
        public value: number,
    ) {}

}
