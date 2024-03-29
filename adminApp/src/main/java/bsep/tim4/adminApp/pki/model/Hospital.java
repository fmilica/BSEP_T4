package bsep.tim4.adminApp.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hospital {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "med_dev_num")
    private long medicalDeviceNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "simulator_hospital",
            joinColumns = @JoinColumn(name = "hospital_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "simulator_id", referencedColumnName = "id"))
    private Set<Simulator> simulators;

    public Hospital(String email) {
        this.email = email;
        this.medicalDeviceNumber = 0;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", medicalDeviceNumber=" + medicalDeviceNumber +
                ", simulators=" + simulators +
                '}';
    }
}