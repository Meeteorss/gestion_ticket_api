package school.spring.school.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import school.spring.school.models.Ticket;
import school.spring.school.models.Ticket.Status;
import school.spring.school.models.User;
import school.spring.school.models.User.Role;
import school.spring.school.repositories.TicketRepository;
import school.spring.school.repositories.UserRepository;

@Service
public class TicketService {
	@Autowired
	TicketRepository tr;
	@Autowired
	UserRepository ur;
	
	public Ticket create(Ticket t) {
		return tr.save(t);
	}
	
	public Ticket updateStatus(Status s,int id) {
		try {
			Ticket ticket = tr.findById(id).get();
			if(ticket == null) {
				return null;
			}
			ticket.setStatus(s);
			return tr.save(ticket);
			
		}catch(Exception e) {
			return null;
		}
		
	}
	public Ticket assignDev(User dev,int id) {
		try {
			Ticket ticket = tr.findById(id).get();
			if(ticket == null) {
				return null;
			}
			ticket.setDev(dev);
			ticket.setStatus(Status.OPEN);
			return tr.save(ticket);
			
		}catch(Exception e) {
			return null;
		}
		
	}
	
	public boolean delete(int id) {
		try {
			tr.delete(tr.findById(id).get());
			return true;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
	}
	public Ticket findById(int id) {
		try {
			return tr.findById(id).get();
		}catch(Exception e) {
			
			return null;
		}
	}
	
	public List<Ticket> getTicketsByClient(int id){
		try {
			User user = ur.findById(id).get();
			if(user == null || user.getRole() != Role.CLIENT) {
				return  new ArrayList<>();
			}else {
				List<Ticket> tickets = tr.findByClient_id(id);
				return tickets;
			}
		}catch(Exception e) {
			return  new ArrayList<>();
		}
		
	}
	public List<Ticket> getTicketsByClient(String username){
		try {
			User user = ur.findByUsername(username);
			if(user == null || user.getRole() != Role.CLIENT) {
				return  new ArrayList<>();
			}else {
				List<Ticket> tickets = tr.findByClient_username(username);
				return tickets;
			}
		}catch(Exception e) {
			return  new ArrayList<>();
		}
		
	}
	public List<Ticket> getTicketsByDev(int id){
		try {
			User user = ur.findById(id).get();
			if(user == null || user.getRole() != Role.DEV) {
				return  new ArrayList<>();
			}else {
				List<Ticket> tickets = tr.findByDev_id(id);
				return tickets;
			}
		}catch(Exception e) {
			return  new ArrayList<>();
		}
		
	}
	public List<Ticket> getTicketsByDev(String username){
		try {
			User user = ur.findByUsername(username);
			if(user == null || user.getRole() != Role.DEV) {
				return  new ArrayList<>();
			}else {
				List<Ticket> tickets = tr.findByDev_username(username);
				return tickets;
			}
		}catch(Exception e) {
			return  new ArrayList<>();
		}
		
	}
	
	public List<Ticket> getTicketsByStatus(Status s){
		List<Ticket> tickets = tr.findByStatus(s);
		return tickets;
	}
	
	public List<Ticket> getAll(){
		return tr.findAll();
	}
	
	
	
}
