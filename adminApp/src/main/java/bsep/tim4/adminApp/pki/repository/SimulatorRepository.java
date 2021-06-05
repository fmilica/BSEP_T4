package bsep.tim4.adminApp.pki.repository;

import bsep.tim4.adminApp.pki.model.Simulator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulatorRepository extends JpaRepository<Simulator, Long> {

    List<Simulator> findByIdNotIn(List<Long> ids);
}
