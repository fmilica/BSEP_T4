import { RuleConditionDto } from "./rule-condition-dto.dto";

export class RuleConditionListDto {

    constructor(
        public ruleConditions: RuleConditionDto[],
    ) {}

}
