import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Client } from "@stomp/stompjs";
import { environment } from "src/environments/environment";
import { Patient } from "../model/patient.model";
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class PatientService {

    constructor(private http: HttpClient) {}

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

        this.patientStompClient.onConnect = function (frame) {
            console.log("Patient alarm STOMP client connected: " + frame);
            // subskripcije
            this.subscribe("/topic/patients", function (patientAlarm) {
                console.log(patientAlarm);
            });
        }

        this.patientStompClient.onStompError = function (frame) {
            console.log("Patient alarm STOMP client error: " + frame.headers["message"]);
            console.log("Patient alarm STOMP client error: " + frame.body);
            //.headers["message"]
            //console.log("Additional details: " + frame.body);
        };

        this.patientStompClient.activate();
    }

    findAllPatients() {
        return this.http
        .get<Patient[]>(environment.apiEndpoint + 'patients')
        .pipe(
          map((patients: Patient[]) => patients),
          catchError((err) => throwError(err))
      );
    }
}