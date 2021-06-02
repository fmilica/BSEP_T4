import { Patient } from "./patient.model";

export class PatientPage {

    constructor(
        public content: Patient[],
        public totalPages: number,
        public totalElements: number,
        public size: number,
    ) {}

}
