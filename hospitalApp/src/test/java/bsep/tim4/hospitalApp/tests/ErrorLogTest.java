package bsep.tim4.hospitalApp.tests;

import static org.junit.Assert.assertEquals;

import bsep.tim4.hospitalApp.model.LogAlarm;
import bsep.tim4.hospitalApp.model.LogLevel;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import bsep.tim4.hospitalApp.model.Log;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorLogTest {

    private KieSession kieSession;

    @Before
    public void setUp() {
        List<String> malicious = new ArrayList<String>();
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks
                .newKieContainer(ks.newReleaseId("bsep.tim4", "drools-spring-kjar", "0.0.1-SNAPSHOT"));
        kieSession = kContainer.newKieSession("cepSession");
        kieSession.setGlobal("maliciousIpAdresses", malicious);
    }

    @Test
    public void ErrorLogsTest() {
        Log log = new Log("1", new Date(), LogLevel.ERROR, "Message for log.", "640d99cc-3299-4a3a-a170-dc3ebabf6775",
                "ERROR", "127.0.0.1", "", "404", false);

        kieSession.insert(log);

        int firedRules = kieSession.fireAllRules();

        QueryResults results = kieSession.getQueryResults("getAllLogAlarms");
        List<LogAlarm> alarms = new ArrayList<>();

        for (QueryResultsRow row : results) {
            LogAlarm alarm = (LogAlarm) row.get("$logAlarm");
            alarms.add(alarm);
        }

        assertEquals(1, firedRules);
    }
}
