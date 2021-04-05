import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { CSR } from "../model/csr.model";

@Injectable({
    providedIn: 'root',
})
export class CsrService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    createCsr(csr: CSR) : Observable<CSR> {
        return this.http.post<CSR>(environment.apiEndpoint + 'csr/create', csr, {
            headers: this.headers,
        });
    }
}