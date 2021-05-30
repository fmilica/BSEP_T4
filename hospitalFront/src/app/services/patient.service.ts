import { Injectable } from "@angular/core";
import { Client } from "@stomp/stompjs";

@Injectable({
    providedIn: 'root',
})
export class PatientService {

    constructor() {
        this.initializeWebSocketConnection();
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

}