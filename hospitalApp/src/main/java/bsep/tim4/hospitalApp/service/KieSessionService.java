package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.model.MaliciousIp;
import bsep.tim4.hospitalApp.repository.MaliciousIpRepository;
import org.apache.maven.shared.invoker.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class KieSessionService {
    private static Logger log = LoggerFactory.getLogger(KieSessionService.class);

    private final KieContainer kieContainer;
    private KieSession rulesSession;
    private KieSession cepSession;

    @Autowired
    private MaliciousIpRepository maliciousIpRepository;

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

    public KieSession getCepSession() {
        if (this.cepSession == null) {
            List<String> malicious = new ArrayList<>();
            List<MaliciousIp> maliciousIps = maliciousIpRepository.findAll();
            for (MaliciousIp ip : maliciousIps) {
                malicious.add(ip.getAddress());
            }
            log.info("Initialized session.");
            cepSession = kieContainer.newKieSession("cepSession");
            cepSession.setGlobal("maliciousIpAdresses", malicious);
        }
        return cepSession;
    }

    public void setCepSession(KieSession kieSession) {
        this.cepSession = kieSession;
    }

    public void updateRulesJar() throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File("../drools-spring-kjar/pom.xml"));
        request.setGoals(Arrays.asList("clean", "install"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("M2_HOME")));
        invoker.execute(request);
    }
}
