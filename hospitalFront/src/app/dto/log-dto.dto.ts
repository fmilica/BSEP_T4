export class LogDto {

    constructor(
        public level: string,
        public message: string,
        public timestamp: Date
    ) {}

}