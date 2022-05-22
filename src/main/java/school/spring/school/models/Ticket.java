package school.spring.school.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	

	@OneToOne
	@JoinColumn(name = "incident_id", referencedColumnName = "id",nullable = false)
	private Incident incident;
	
	@JsonIgnoreProperties({"password"})
	@JoinColumn(nullable = false,name = "client")
	@ManyToOne
	private User client;
	
	
	@JsonIgnoreProperties({"password"})
	@JoinColumn(name = "dev",nullable = true)
	@ManyToOne
	private User dev;
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public Status getStatus() {
		return status;
	}



	public void setStatus(Status status) {
		this.status = status;
	}



	public Incident getIncident() {
		return incident;
	}



	public void setIncident(Incident incident) {
		this.incident = incident;
	}



	public User getClient() {
		return client;
	}



	public void setClient(User client) {
		this.client = client;
	}



	public User getDev() {
		return dev;
	}



	public void setDev(User dev) {
		this.dev = dev;
	}



	@Override
	public String toString() {
		return "Ticket [id=" + id + ", status=" + status + ", incident=" + incident + ", client=" + client + ", dev="
				+ dev + "]";
	}



	public enum Status{
		PENDING,RESOLVED,OPEN,CANCELED
	}
	
	
	
	
	
	
}