import { Byte } from "@angular/compiler/src/util";

export class CertificateDetails {
    // certificate
    certAlias: string;
    serialNumb: number;
    version: number;
    validFrom: Date;
    validUntil: Date;
    signatureAlgorithm: string;
    publicKey: Byte[];
    commonName: string;
    // subject
    subjectData: string;
    // issuer
    issuerData: string;
}