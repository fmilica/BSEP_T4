package bsep.tim4.adminApp.pki.repository;

import bsep.tim4.adminApp.pki.model.Simulator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulatorRepository extends JpaRepository<Simulator, Long> {
}
