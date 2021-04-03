export class CSR {

    constructor(
        public commonName: string,
        public name: string,
        public surname: string,
        public organizationName: string,
        public organizationUnit: string,
        public country: string,
        public email: string,
        public status: number,
        public id?: number
    ) {}

}
