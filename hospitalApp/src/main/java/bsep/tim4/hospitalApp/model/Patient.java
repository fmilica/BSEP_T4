package bsep.tim4.hospitalApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "Patient")
public class Patient {

    @Id
    private String id;

    private String name;

    private LocalDate dateOfBirth;

    private String placeOfBirth;

    private String illnesses;

    public Patient() {}

    public Patient(String name, LocalDate dateOfBirth, String placeOfBirth, String illnesses) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
        this.illnesses = illnesses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getIllnesses() {
        return illnesses;
    }

    public void setIllnesses(String illnesses) {
        this.illnesses = illnesses;
    }

}
