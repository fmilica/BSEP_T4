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

public class IpAddressLogTest {

    private KieSession kieSession;
    private List<String> malicious;

    @Before
    public void setUp() {
        malicious = new ArrayList<String>();
        malicious.add("122.1.1.1");
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks
                .newKieContainer(ks.newReleaseId("bsep.tim4", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        kieSession = kContainer.newKieSession("cepSession");
        kieSession.getAgenda().getAgendaGroup("ip").setFocus();
        kieSession.setGlobal("maliciousIpAdresses", malicious);
    }

    @Test
    public void MaliciousLoginTest() {
        Log log = new Log("1L", new Date(), LogLevel.INFO, "Message for log.", "640d99cc-3299-4a3a-a170-dc3ebabf6775",
                "LOGIN", "122.1.1.1", "", "200", false);

        kieSession.insert(log);

        int firedRules = kieSession.fireAllRules();
        assertEquals(1, firedRules);
    }

    @Test
    public void MaliciousLogTest() {
        Log log = new Log("1L", new Date(), LogLevel.INFO, "Message for log.", "640d99cc-3299-4a3a-a170-dc3ebabf6775",
                "CSR", "122.1.1.1", "", "200", false);

        kieSession.insert(log);

        int firedRules = kieSession.fireAllRules();
        assertEquals(1, firedRules);
    }

    @Test
    public void MaliciousIpAddressTest() {
        //kieSession.getAgenda().getAgendaGroup("ip").setFocus();
        int firedRules = 0;
        Log log1 = new Log("1L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log1);
        kieSession.fireAllRules();
        Log log2 = new Log("2L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log2);
        kieSession.fireAllRules();
        Log log3 = new Log("3L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log3);
        kieSession.fireAllRules();
        Log log4 = new Log("4L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log4);
        kieSession.fireAllRules();
        Log log5 = new Log("5L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log5);
        kieSession.fireAllRules();
        Log log6 = new Log("6L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log6);
        kieSession.fireAllRules();
        Log log7 = new Log("7L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log7);
        kieSession.fireAllRules();
        Log log8 = new Log("8L", new Date(), LogLevel.INFO, "Message for log.", "user1",
                "LOGIN_ERROR", "127.0.0.1", "", "404", false);
        kieSession.insert(log8);
        firedRules = kieSession.fireAllRules();
        malicious = (List<String>) kieSession.getGlobal("maliciousIpAdresses");
        System.out.println(firedRules);
        assertEquals(2, malicious.size());
    }
}
