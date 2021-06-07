import { LogAlarmDto } from "../dto/log-alarm-dto.dto";

export class LogAlarmPage {

    constructor(
        public content: LogAlarmDto[],
        public totalPages: number,
        public totalElements: number,
        public size: number,
    ) {}

}