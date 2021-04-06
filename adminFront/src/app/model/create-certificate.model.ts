export class CreateCertificate {

    constructor(
        public csrId: number,
        public caAlias: string,
        public beginDate: Date,
        public endDate: Date,
        public template: string
        // additional information
    ) {}

}
