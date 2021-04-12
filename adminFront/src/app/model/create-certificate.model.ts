import { CertificateAdditionalInfo } from 'src/app/model/certificate-additional-info.model';

export class CreateCertificate {

    constructor(
        public csrId: number,
        public caAlias: string,
        public beginDate: Date,
        public endDate: Date,
        //public template: string,
        // additional information
        public additionalInfo: CertificateAdditionalInfo
        /*public isCA?: boolean,
        public keyUsage?: number[],
        public extendedKeyUsage?: string[]*/
    ) {}

}
