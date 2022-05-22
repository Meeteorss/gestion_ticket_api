package school.spring.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import school.spring.school.models.Incident;
import school.spring.school.repositories.IncidentRepository;

@Service
public class IncidentService {
	@Autowired
	IncidentRepository ir;
	
	public Incident create(Incident i) {
		try {
			return ir.save(i);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	
	}
	
	public Incident findById(int id) {
		try {
			return ir.findById(id).get();
		}catch(Exception e) {
		
			return null;
		}
		
	}
}