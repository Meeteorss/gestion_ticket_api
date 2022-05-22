package school.spring.school.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Incident {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@Column(nullable = false)
	private String software;
	@Column(nullable = false)
	private String environment;
	@Column(nullable = false)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Urgency urgency;
	
	
	
	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getSoftware() {
		return software;
	}



	public void setSoftware(String software) {
		this.software = software;
	}



	public String getEnvironment() {
		return environment;
	}



	public void setEnvironment(String environment) {
		this.environment = environment;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Urgency getUrgency() {
		return urgency;
	}



	public void setUrgency(Urgency urgency) {
		this.urgency = urgency;
	}
	



	



	public Incident(String software, String environment, String description, Urgency urgency) {
		super();
		this.software = software;
		this.environment = environment;
		this.description = description;
		this.urgency = urgency;
	}
	








	public Incident() {
	
	}









	public enum Urgency{
		CRITICAL,URGENT,NORMAL,NOT_URGENT
	}
}

