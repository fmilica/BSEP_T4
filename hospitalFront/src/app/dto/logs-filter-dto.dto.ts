export class LogsFilterDto {

    constructor(
        public level: string,
        public fromDate: Date,
        public toDate: Date,
        public message: string,
        public source: string,
        public ipAddress: string,
    ) {}

}