import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Certificate } from "../model/certificate.model";

@Injectable({
    providedIn: 'root',
})
export class CertificateService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    getAllCertificates(): Observable<Certificate[]> {
        return this.http.get<Certificate[]>(environment.apiEndpoint + 'certificate');
    }

    getAllCACertificates(): Observable<Certificate[]> {
        return this.http.get<Certificate[]>(environment.apiEndpoint + 'certificate/ca');
    }

    getRootCertificate(): Observable<Certificate> {
        return this.http.get<Certificate>(environment.apiEndpoint + 'certificate/root');
    }

    getCertificate(id: number, alias: string): Observable<Certificate> {
        return this.http.get<Certificate>(environment.apiEndpoint + 'certificate/' + alias)
    }
}