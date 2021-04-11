import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { CertificateDetails } from "../model/certificate-details.model";
import { CertificateView } from "../model/certificate-view.model";
import { Certificate } from "../model/certificate.model";
import { CreateCertificate } from "../model/create-certificate.model";

@Injectable({
    providedIn: 'root',
})
export class CertificateService {

    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(private http: HttpClient) {}

    getDetails(alias: string): Observable<CertificateDetails> {
        return this.http.get<CertificateDetails>(environment.apiEndpoint + 'certificate/detailed/' + alias);
    }

    downloadCertificate(alias: string) {
        return this.http.get(environment.apiEndpoint + 'certificate/download-any?alias=' + alias, {
            responseType: 'arraybuffer'
        })
    }

    downloadPkcs12(alias: string) {
        return this.http.get(environment.apiEndpoint + 'certificate/download-pkcs12?alias=' + alias, {
            responseType: 'arraybuffer'
        })
    }

    getAllCertificates(): Observable<CertificateView[]> {
        return this.http.get<CertificateView[]>(environment.apiEndpoint + 'certificate');
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

    revokeCertificate(alias: string, chosenReason: string): Observable<void> {
        return this.http.post<void>(environment.apiEndpoint + 'certificate/revoke/' + alias, chosenReason, {
            headers: this.headers,
        })
    }
}