import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: 'root',
})
export class HospitalService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) { }

    getAllHospitals(): Observable<[]> {
        return this.http.get<[]>(environment.apiEndpoint + 'hospital');
    }

    getAllHospitalSimulators(hospitalId: number): Observable<[]> {
        return this.http.get<[]>(environment.apiEndpoint + 'hospital/' + hospitalId);
    }

    getAllNotHospitalSimulators(hospitalId: number): Observable<[]> {
        return this.http.get<[]>(environment.apiEndpoint + 'hospital/not-in/' + hospitalId);
    }

    addSimulator(hospitalId: number, simulator): Observable<[]> {
        return this.http.post<[]>(environment.apiEndpoint + 'hospital/add-simulator/' + hospitalId, simulator, {
            headers: this.headers,
        });
    }

    removeSimulator(hospitalId: number, simulator): Observable<[]> {
        return this.http.post<[]>(environment.apiEndpoint + 'hospital/remove-simulator/' + hospitalId, simulator, {
            headers: this.headers,
        });
    }
}