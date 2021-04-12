export class CertificateAdditionalInfo {

    constructor(
        public ca?: boolean,
        public keyUsages?: number[],
        public extendedKeyUsages?: string[]
    ) {}

}