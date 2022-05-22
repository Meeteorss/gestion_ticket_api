package school.spring.school.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import school.spring.school.models.Ticket;
import school.spring.school.models.Ticket.Status;
import school.spring.school.models.User;
import school.spring.school.models.User.Role;
import school.spring.school.services.IncidentService;
import school.spring.school.services.TicketService;
import school.spring.school.services.UserService;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {
	@Autowired
	TicketService ts;
	@Autowired
	UserService us;
	@Autowired
	IncidentService is;

	@GetMapping("/tickets")
	public List<Ticket> findPendingTickets() {
		return ts.getTicketsByStatus(Status.PENDING);
	}
	@GetMapping("/devs")
	public List<User> findAllDevs() {
		return us.findAllDevs();
	}

	@PostMapping("/tickets")
	public AssignResponse assign(@RequestBody AssignInput input) {
		Ticket ticket = ts.findById(input.ticketId);
		User dev = us.findById(input.devId);
		AssignResponse response = new AssignResponse();
		if(ticket == null) {
			response.error.field = "ticket";
			response.error.message = "ticket not found";
		}else if(dev == null){
			response.error.field = "dev";
			response.error.message = "dev not found";
		}else if(ticket.getStatus() != Status.PENDING || ticket.getDev() != null) {
			response.error.field = "ticket";
			response.error.message = "ticket Already assigned";
		}else if(dev.getRole() != Role.DEV) {
			response.error.field = "dev";
			response.error.message = "not a dev";
		}else {
			response.ticket =ts.assignDev(dev, ticket.getId());
			
			
		}
		return response;
	}
}

class AssignInput {
	public int ticketId;
	public int devId;
}

class AssignResponse {
	AssignResponse() {
		this.error = new Error();
	}

	public Ticket ticket;
	public Error error;
}
