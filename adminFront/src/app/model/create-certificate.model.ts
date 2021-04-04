export class CreateCertificate {

    constructor(
        public csrId: number,
        public caAlias: string,
        // additional information
    ) {}

}
