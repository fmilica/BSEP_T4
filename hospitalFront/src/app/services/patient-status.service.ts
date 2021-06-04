import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PatientStatusPage } from "../model/patient-status-page.model";

@Injectable({
    providedIn: 'root',
})
export class PatientStatusService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    findAllByPageByPatient(page: number, size: number, patientId: string): Observable<PatientStatusPage> {
      let params = new HttpParams();
  
      params = params.append('page', String(page));
      params = params.append('size', String(size));
  
      return this.http
        .get<PatientStatusPage>(environment.apiEndpoint + 'patientStatuses/' + patientId, { params })
        .pipe(
          map((patientStatusPage: PatientStatusPage) => patientStatusPage),
          catchError((err) => throwError(err))
      );
  }

  findAllByPage(page: number, size: number): Observable<PatientStatusPage> {
      let params = new HttpParams();
  
      params = params.append('page', String(page));
      params = params.append('size', String(size));
  
      return this.http
        .get<PatientStatusPage>(environment.apiEndpoint + 'patientStatuses/by-page', { params })
        .pipe(
          map((patientStatusPage: PatientStatusPage) => patientStatusPage),
          catchError((err) => throwError(err))
      );
    }
}