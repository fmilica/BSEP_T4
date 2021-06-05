import { HttpHeaders, HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { LogsFilterDto } from "../dto/logs-filter-dto.dto";
import { LogPage } from "../model/log-page.model";
import { Report } from "../model/report.model";

@Injectable({
    providedIn: 'root',
})
export class ReportService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) { }

    createReport(report: Report): Observable<Report> {
        return this.http.post<Report>(environment.apiEndpoint + 'report', report, {
            headers: this.headers,
        });
    }
}