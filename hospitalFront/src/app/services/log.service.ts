import { HttpHeaders, HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { LogsFilterDto } from "../dto/logs-filter-dto.dto";
import { LogPage } from "../model/log-page.model";

@Injectable({
    providedIn: 'root',
})
export class LogService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    findAllByPage(page: number, size: number): Observable<LogPage> {
        let params = new HttpParams();
    
        params = params.append('page', String(page));
        params = params.append('size', String(size));
    
        return this.http
          .get<LogPage>(environment.apiEndpoint + 'log/by-page', { params })
          .pipe(
            map((logPage: LogPage) => logPage),
            catchError((err) => throwError(err))
        );
    }

    findAllFilter(page: number, size: number, logFilterDto: LogsFilterDto): Observable<LogPage> {
        let params = new HttpParams();
    
        params = params.append('page', String(page));
        params = params.append('size', String(size));
    
        return this.http
          .post<LogPage>(environment.apiEndpoint + 'log', logFilterDto, { params })
          .pipe(
            map((logPage: LogPage) => logPage),
            catchError((err) => throwError(err))
        );
    }
}