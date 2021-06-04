import { PatientStatus } from "./patient-status.model";

export class PatientStatusPage {

    constructor(
        public content: PatientStatus[],
        public totalPages: number,
        public totalElements: number,
        public size: number,
    ) {}

}
