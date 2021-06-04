import { HttpHeaders, HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { PatientAlarmPage } from "../model/patient-alarm-page.model";
import { PatientStatusPage } from "../model/patient-status-page.model";

@Injectable({
    providedIn: 'root',
})
export class PatientAlarmService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    findAllByPageByPatient(page: number, size: number, patientId: string): Observable<PatientAlarmPage> {
      let params = new HttpParams();
  
      params = params.append('page', String(page));
      params = params.append('size', String(size));
  
      return this.http
        .get<PatientAlarmPage>(environment.apiEndpoint + 'patient-alarms/' + patientId, { params })
        .pipe(
          map((patientAlarmPage: PatientAlarmPage) => patientAlarmPage),
          catchError((err) => throwError(err))
      );
  }

    findAllByPage(page: number, size: number): Observable<PatientAlarmPage> {
        let params = new HttpParams();
    
        params = params.append('page', String(page));
        params = params.append('size', String(size));
    
        return this.http
          .get<PatientAlarmPage>(environment.apiEndpoint + 'patient-alarms/by-page', { params })
          .pipe(
            map((patientAlarmPage: PatientAlarmPage) => patientAlarmPage),
            catchError((err) => throwError(err))
        );
    }
}