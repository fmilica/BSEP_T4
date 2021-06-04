import { PatientAlarm } from "./patient-alarm.model";

export class PatientAlarmPage {

    constructor(
        public content: PatientAlarm[],
        public totalPages: number,
        public totalElements: number,
        public size: number,
    ) {}

}
