package bsep.tim4.hospitalApp.dto;

public class PatientDTO {
    private String id;
    private String name;

    public PatientDTO() {}

    public PatientDTO(String id, String name) {
        this.id = id;
        this.name = name;
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
}
