export class CertificateView {
    alias: string;
    commonName: string;
    revoked: boolean;
    children?: CertificateView[];
  }