import { HttpHeaders, HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Client } from "@stomp/stompjs";
import { ToastrService } from "ngx-toastr";
import { Observable, throwError } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { LogAlarmPage } from "../model/log-alarm-page.model";

@Injectable({
    providedIn: 'root',
})
export class LogAlarmService {
    private headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    constructor(
        private http: HttpClient,
        private toastr: ToastrService
    ) {
        this.initializeWebSocketConnection()
    }

    public logAlarmStompClient;

    initializeWebSocketConnection() {
        this.logAlarmStompClient = new Client({
            brokerURL: 'wss://localhost:8081/connect',
            debug: function (str) {
                console.log(str);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
        });

        let that = this;
        this.logAlarmStompClient.onConnect = function (frame) {
            console.log("Patient alarm STOMP client connected: " + frame);
            // subskripcije
            this.subscribe("/topic/logs", function (message) {
                const patientAlarm = JSON.parse(message.body);
                that.toastr.warning(patientAlarm.message);
            });
        }

        this.logAlarmStompClient.onStompError = function (frame) {
            console.log("Patient alarm STOMP client error: " + frame.headers["message"]);
            console.log("Patient alarm STOMP client error: " + frame.body);
        };

        this.logAlarmStompClient.activate();
    }

    findAllByPage(page: number, size: number): Observable<LogAlarmPage> {
        let params = new HttpParams();

        params = params.append('page', String(page));
        params = params.append('size', String(size));

        return this.http
            .get<LogAlarmPage>(environment.apiEndpoint + 'log-alarm/by-page', { params })
            .pipe(
                map((logAlarmPage: LogAlarmPage) => logAlarmPage),
                catchError((err) => throwError(err))
            );
    }

    findAllLogAlarmTypes(): Observable<String[]> {
        return this.http.get<String[]>(environment.apiEndpoint + 'log-alarm/types');
    }
}