package bsep.tim4.hospitalApp.tests;

import bsep.tim4.hospitalApp.model.Log;
import bsep.tim4.hospitalApp.model.LogLevel;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class FailedLoginTest {

    private KieSession kieSession;

    @Before
    public void setUp() {
        List<String> malicious = new ArrayList<>();
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks
                .newKieContainer(ks.newReleaseId("bsep.tim4", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        kieSession = kContainer.newKieSession("cepSession");
        kieSession.setGlobal("maliciousIpAdresses", malicious);
        //kieSession.getAgenda().getAgendaGroup("failed-login").setFocus();
    }

    @Test
    public void failedLoginTestFail() throws ParseException {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        Log log1 = new Log("60ba215daa3a1548cec57863", Date.from(Instant.from(OffsetDateTime.parse("2021-06-04T12:01:00.459+00:00", timeFormatter))), LogLevel.WARN, "Failed login.", "user1", "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log1);
        int firedRules = kieSession.fireAllRules();
        Log log2 = new Log("2L", Date.from(Instant.from(OffsetDateTime.parse("2021-06-04T12:01:01.459+00:00", timeFormatter))), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log2);
        firedRules = kieSession.fireAllRules();
        Log log3 = new Log("3L", Date.from(Instant.from(OffsetDateTime.parse("2021-06-04T12:01:02.459+00:00", timeFormatter))), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log3);
        firedRules = kieSession.fireAllRules();
        Log log4 = new Log("4L", Date.from(Instant.from(OffsetDateTime.parse("2021-06-04T12:01:03.459+00:00", timeFormatter))), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log4);
        firedRules = kieSession.fireAllRules();
        Log log5 = new Log("5L", Date.from(Instant.from(OffsetDateTime.parse("2021-06-04T12:01:04.459+00:00", timeFormatter))), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log5);
        firedRules = kieSession.fireAllRules();
        Log log6 = new Log("6L", Date.from(Instant.from(OffsetDateTime.parse("2021-06-04T12:01:05.459+00:00", timeFormatter))), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log6);

        firedRules = kieSession.fireAllRules();

        assertEquals(1, firedRules);
    }

    @Test
    public void failedLoginTest() throws ParseException {
        Log log1 = new Log("60ba215daa3a1548cec57863", new Date(), LogLevel.WARN, "Failed login.", "user1", "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log1);
        int firedRules = kieSession.fireAllRules();
        Log log2 = new Log("2L", new Date(), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log2);
        firedRules = kieSession.fireAllRules();
        Log log3 = new Log("3L", new Date(), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log3);
        firedRules = kieSession.fireAllRules();
        Log log4 = new Log("4L", new Date(), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log4);
        firedRules = kieSession.fireAllRules();
        Log log5 = new Log("5L", new Date(), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log5);
        firedRules = kieSession.fireAllRules();
        Log log6 = new Log("6L", new Date(), LogLevel.WARN, "Failed login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log6);

        firedRules = kieSession.fireAllRules();

        assertEquals(1, firedRules);
        assertEquals(true, log1.isProcessed());
    }
}
