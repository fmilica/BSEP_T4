export class CSR {

    constructor(
        public commonName: string,
        public givenName: string,
        public surname: string,
        public organizationName: string,
        public organizationUnit: string,
        public country: string,
        public email: string,
        public status: string,
        public id?: number
    ) {}

}
