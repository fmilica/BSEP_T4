package bsep.tim4.hospitalApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Patient")
public class PatientEncrypted {

    @Id
    private String id;

    private byte[] personalInfo;

    public PatientEncrypted() {}

    public PatientEncrypted(byte[] personalInfo) {
        this.personalInfo = personalInfo;
    }

    public PatientEncrypted(String id, byte[] personalInfo) {
        this.id = id;
        this.personalInfo = personalInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(byte[] personalInfo) {
        this.personalInfo = personalInfo;
    }
}
