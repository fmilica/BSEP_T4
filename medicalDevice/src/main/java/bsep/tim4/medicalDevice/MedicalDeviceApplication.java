package bsep.tim4.medicalDevice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedicalDeviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalDeviceApplication.class, args);
	}

}
