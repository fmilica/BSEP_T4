package bsep.tim4.hospitalApp.users.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name="users")
@SQLDelete(sql =
"UPDATE users " +
"SET is_active = false " +
"WHERE id = ?")
@Where(clause="is_active=true")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType= DiscriminatorType.STRING)
public class User implements UserDetails {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
	
	@Column(name="is_active")
	private Boolean active;
	
	@NonNull
	@Column(name = "name", nullable = false, unique = false)
	private String name;
	
	@NonNull
	@Column(name = "surname", nullable = false, unique = false)
	private String surname;
	
	@NonNull
	@Column(name = "email", nullable = false, unique = false)
	private String email;
	
	@NonNull
	@Column(name = "password", nullable = false, unique = false)
	private String password;

	@Column(name = "last_password_reset_date")
	private Timestamp lastPasswordResetDate;

	@Column(name = "account_non_locked")
	private boolean accountNonLocked;

	@Column(name = "failed_attempts")
	private int failedAttempts;

	@Column(name = "last_account_lock_date")
	private Timestamp lastAccountLockDate;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
	private Set<Authority> authorities;

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	public void setPassword(String password) {
		Timestamp now = new Timestamp(new Date().getTime());
		this.setLastPasswordResetDate(now);
		this.password = password;
	}

	public User() {
		this.active = true;
	}
	
	public User(Long id, @NonNull String name, @NonNull String surname, @NonNull String email, @NonNull String password) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.active = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return email.equals(user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}
}
