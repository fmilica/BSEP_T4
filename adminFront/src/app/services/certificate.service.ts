import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Certificate } from "../model/certificate.model";
import { CreateCertificate } from "../model/create-certificate.model";

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
        return this.http.get<Certificate[]>(environment.apiEndpoint + 'certificate/ca-certificates');
    }

    getRootCertificate(): Observable<Certificate> {
        return this.http.get<Certificate>(environment.apiEndpoint + 'certificate/root-certificate');
    }

    getCertificate(alias: string): Observable<Certificate> {
        return this.http.get<Certificate>(environment.apiEndpoint + 'certificate/' + alias)
    }

    createCertificate(certDto: CreateCertificate): Observable<CreateCertificate> {
        return this.http.post<CreateCertificate>(environment.apiEndpoint + 'certificate', certDto, {
            headers: this.headers,
        });
    }

    revokeCertificate(alias: string): Observable<Certificate> {
        return this.http.get<Certificate>(environment.apiEndpoint + 'certificate/revoke' + alias)
    }
}