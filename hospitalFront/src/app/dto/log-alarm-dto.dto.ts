export class LogAlarmDto {

    constructor(
        public type: string,
        public message: string,
        public timestamp: Date
    ) {}

}