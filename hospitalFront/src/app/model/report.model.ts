import { FrequentSource } from "./frequent-source.model";

export class Report {

    constructor(
        public startDate: Date,
        public endDate: Date,
        public totalLogs?: number,
        public totalLogAlarms?: number,
        public frequentSource?: FrequentSource[],
        public logsByLevel?: number[],
        public logAlarmsByType?: number[]
    ) { }

}
