package bsep.tim4.hospitalApp.service;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KieSessionService {
    private static Logger log = LoggerFactory.getLogger(KieSessionService.class);

    private final KieContainer kieContainer;
    private KieSession rulesSession;

    @Autowired
    public KieSessionService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public KieContainer getKieContainer() {
        return kieContainer;
    }

    public KieSession getRulesSession() {
        if (this.rulesSession == null) {
            log.info("Initialized session");
            rulesSession = kieContainer.newKieSession("rulesSession");
        }
        return rulesSession;
    }

    public void setRulesSession(KieSession kieSession) {
        this.rulesSession = kieSession;
    }
}
