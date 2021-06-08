import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Client } from "@stomp/stompjs";
import { environment } from "src/environments/environment";
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { PatientPage } from "../model/patient-page.model";
import { RuleDto } from "../dto/rule-dto";
import { PatientName } from "../model/patient-name.model";
import { ToastrService } from "ngx-toastr";

@Injectable({
    providedIn: 'root',
})
export class PatientService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(
        private http: HttpClient,
        private toastr: ToastrService
    ) {
        this.initializeWebSocketConnection()
    }

    public patientStompClient;

    initializeWebSocketConnection() {
        this.patientStompClient = new Client({
            brokerURL: 'wss://localhost:8081/connect',
            debug: function (str) {
                console.log(str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        let that = this;
        this.patientStompClient.onConnect = function (frame) {
            console.log("Patient alarm STOMP client connected: " + frame);
            // subskripcije
            this.subscribe("/topic/patients", function (message) {
                const patientAlarm = JSON.parse(message.body);
                that.toastr.warning(patientAlarm.name + ":\n" + patientAlarm.message);
            });
        }

        this.patientStompClient.onStompError = function (frame) {
            console.log("Patient alarm STOMP client error: " + frame.headers["message"]);
            console.log("Patient alarm STOMP client error: " + frame.body);
        };

        this.patientStompClient.activate();
    }

    findAll() {
        return this.http
            .get<PatientName[]>(environment.apiEndpoint + 'patients')
            .pipe(
                map((patients: PatientName[]) => patients),
                catchError((err) => throwError(err))
            );
    }

    findAllByPage(page: number, size: number): Observable<PatientPage> {
        let params = new HttpParams();

        params = params.append('page', String(page));
        params = params.append('size', String(size));

        return this.http
            .get<PatientPage>(environment.apiEndpoint + 'patients/by-page', { params })
            .pipe(
                map((patientStatusPage: PatientPage) => patientStatusPage),
                catchError((err) => throwError(err))
            );
    }

    createRule(ruleDto: RuleDto): Observable<void> {
        return this.http.post<void>(environment.apiEndpoint + 'patients/create-rule', ruleDto)
    }
}