import { HttpHeaders, HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { LogAlarmPage } from "../model/log-alarm-page.model";

@Injectable({
    providedIn: 'root',
})
export class LogAlarmService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    findAllByPage(page: number, size: number): Observable<LogAlarmPage> {
        let params = new HttpParams();
    
        params = params.append('page', String(page));
        params = params.append('size', String(size));
    
        return this.http
          .get<LogAlarmPage>(environment.apiEndpoint + 'log-alarm/by-page', { params })
          .pipe(
            map((logAlarmPage: LogAlarmPage) => logAlarmPage),
            catchError((err) => throwError(err))
        );
    }
}