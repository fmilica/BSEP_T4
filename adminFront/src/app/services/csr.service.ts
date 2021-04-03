import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { CSR } from "../model/csr.model";

@Injectable({
    providedIn: 'root',
})
export class CsrService {

    public chosenCsr: BehaviorSubject<CSR> = new BehaviorSubject<CSR>({} as CSR);

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    getAllCertificateSigningRequests(): Observable<CSR[]> {
        return this.http.get<CSR[]>(environment.apiEndpoint + 'csr');
    }

    acceptCertificateSigningRequest(csrId: number /* sertifikat */): Observable<CSR> {
        return this.http.put<CSR>(environment.apiEndpoint + 'csr/accept/' + csrId, {
            headers: this.headers
        });
    }

    declineCertificateSigningRequest(csrId: number): Observable<CSR> {
        return this.http.put<CSR>(environment.apiEndpoint + 'csr/decline/' + csrId, {
            headers: this.headers
        });
    }
}