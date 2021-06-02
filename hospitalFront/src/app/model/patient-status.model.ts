export class PatientStatus {

    constructor(
        public name: string,
        public heartRate: number,
        public bloodPressure: string,
        public bodyTemperature: number,
        public respiratoryRate: number,
        public timestamp: Date,
    ) {}

}
