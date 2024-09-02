package it.ale.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "account")
public class Account {

	@Id
	@UuidGenerator
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private String userName;
	private String password;
	private String role;
	private Instant createdDate;
}
