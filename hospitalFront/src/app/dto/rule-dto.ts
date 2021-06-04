import { RuleConditionListDto } from "./rule-condition-list-dto.dto";

export class RuleDto {

    constructor(
        public patientId: string,
        public ruleConditions: RuleConditionListDto,
    ) {}

}
