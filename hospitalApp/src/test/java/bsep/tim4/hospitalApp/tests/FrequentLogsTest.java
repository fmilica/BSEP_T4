package bsep.tim4.hospitalApp.tests;

import bsep.tim4.hospitalApp.model.Log;
import bsep.tim4.hospitalApp.model.LogLevel;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FrequentLogsTest {
    private KieSession kieSession;

    @Before
    public void setUp() {
        List<String> malicious = new ArrayList<>();
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks
                .newKieContainer(ks.newReleaseId("bsep.tim4", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        kieSession = kContainer.newKieSession("cepSession");
        kieSession.setGlobal("maliciousIpAdresses", malicious);
    }

    /*@Test
    public void frequentLogsTest(){
        Log log1 = new Log(1L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log1);
        int firedRules = kieSession.fireAllRules();
        Log log2 = new Log(2L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log2);
        firedRules = kieSession.fireAllRules();
        Log log3 = new Log(3L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log3);
        firedRules = kieSession.fireAllRules();
        Log log4 = new Log(4L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log4);
        firedRules = kieSession.fireAllRules();
        Log log5 = new Log(5L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log5);
        firedRules = kieSession.fireAllRules();
        Log log6 = new Log(6L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log6);
        Log log7 = new Log(7L, new Date(), LogLevel.INFO, "CSR sent.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log7);
        firedRules = kieSession.fireAllRules();
        Log log8 = new Log(8L, new Date(), LogLevel.INFO, "Failed login.", "user1",
                "CSR", "127.0.0.1", "", 404, false);
        kieSession.insert(log8);

        firedRules = kieSession.fireAllRules();

        assertEquals(1, firedRules);
        assertEquals(true, log1.isProcessed());
        //nece raditi jer sam ovde stavila samo 8 logova a u pravilu je 50
    }

    @Test
    public void frequentLoginLogsTest(){
        Log log1 = new Log(1L, new Date(), LogLevel.INFO, "Error during login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", 404, false);
        kieSession.insert(log1);
        int firedRules = kieSession.fireAllRules();
        Log log2 = new Log(2L, new Date(), LogLevel.INFO, "Successful login.", "user1",
                "LOGIN", "127.0.0.1", "", 200, false);
        kieSession.insert(log2);
        firedRules = kieSession.fireAllRules();
        Log log3 = new Log(3L, new Date(), LogLevel.INFO, "Successful login.", "user1",
                "LOGIN", "127.0.0.1", "", 200, false);
        kieSession.insert(log3);
        firedRules = kieSession.fireAllRules();
        Log log4 = new Log(4L, new Date(), LogLevel.INFO, "Error during login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", 404, false);
        kieSession.insert(log4);
        firedRules = kieSession.fireAllRules();
        Log log5 = new Log(5L, new Date(), LogLevel.INFO, "Successful login.", "user1",
                "LOGIN", "127.0.0.1", "", 200, false);
        kieSession.insert(log5);
        firedRules = kieSession.fireAllRules();
        Log log6 = new Log(6L, new Date(), LogLevel.INFO, "Error during login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", 404, false);
        kieSession.insert(log6);
        Log log7 = new Log(7L, new Date(), LogLevel.INFO, "Successful login.", "user1",
                "LOGIN", "127.0.0.1", "", 200, false);
        kieSession.insert(log7);
        firedRules = kieSession.fireAllRules();
        Log log8 = new Log(8L, new Date(), LogLevel.INFO, "Error during login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", 404, false);
        kieSession.insert(log8);
        firedRules = kieSession.fireAllRules();
        Log log9 = new Log(9L, new Date(), LogLevel.INFO, "Error during login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", 404, false);
        kieSession.insert(log9);
        firedRules = kieSession.fireAllRules();
        Log log10 = new Log(10L, new Date(), LogLevel.INFO, "Successful login.", "user1",
                "LOGIN", "127.0.0.1", "", 200, false);
        kieSession.insert(log10);
        firedRules = kieSession.fireAllRules();
        Log log11 = new Log(11L, new Date(), LogLevel.INFO, "Error during login.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", 404, false);
        kieSession.insert(log11);

        firedRules = kieSession.fireAllRules();

        assertEquals(1, firedRules);
        assertEquals(true, log1.isProcessed());
    }*/
}
