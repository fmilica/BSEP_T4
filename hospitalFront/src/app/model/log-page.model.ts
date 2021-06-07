import { LogDto } from "../dto/log-dto.dto";

export class LogPage {

    constructor(
        public content: LogDto[],
        public totalPages: number,
        public totalElements: number,
        public size: number,
    ) {}

}
