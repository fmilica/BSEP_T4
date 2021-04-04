package bsep.tim4.hospitalApp.users.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="authority")
public class Authority implements GrantedAuthority {

	@Transient
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@NonNull
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	@NonNull
	@Column(name="name")
	private String name;

	@Override
	public String getAuthority() {
		return name;
	}

	/*
	 * @Getter private Set<User> users;
	 */
}
