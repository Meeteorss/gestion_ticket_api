package school.spring.school.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import school.spring.school.models.Ticket;
import school.spring.school.models.Ticket.Status;
import school.spring.school.models.User;
import school.spring.school.models.User.Role;
import school.spring.school.services.IncidentService;
import school.spring.school.services.TicketService;
import school.spring.school.services.UserService;

@RestController
@RequestMapping(value = "/dev")
public class DevController {
	@Autowired
	TicketService ts;
	@Autowired
	UserService us;
	@Autowired
	IncidentService is;

	@GetMapping("/tickets")
	public List<Ticket> findTicketsByDev(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		try {
			String token = authorizationHeader.substring("Bearer ".length());
			Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
			JWTVerifier verifier = JWT.require(alg).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			String username = decodedJWT.getSubject();
			return ts.getTicketsByDev(username);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return new ArrayList<>();
		}

	}

	@PostMapping("/tickets")
	public UpdateTicketResponse updateTicket(@RequestBody UpdateTicketInput input, HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		UpdateTicketResponse response = new UpdateTicketResponse();
		try {
			String token = authorizationHeader.substring("Bearer ".length());
			Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
			JWTVerifier verifier = JWT.require(alg).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			String username = decodedJWT.getSubject();
			User user = us.findByUsername(username);
			Ticket ticket = ts.findById(input.ticketId);
			if (ticket == null) {
				response.error.field = "ticket";
				response.error.message = "ticket not found";
				return response;
			} else if (user == null) {
				response.error.field = "user";
				response.error.message = "user not found";
				return response;
			} else if (user.getRole() == Role.CLIENT) {
				response.error.field = "user";
				response.error.message = "Not autorized";
				return response;
			} else if (user.getRole() == Role.DEV && ticket.getDev().getId() != user.getId()) {
				response.error.field = "user";
				response.error.message = "Not autorized";
				return response;
			} else {
				if (input.status != Status.PENDING && ticket.getStatus() != input.status) {
					response.ticket = ts.updateStatus(input.status, ticket.getId());
					return response;
				} else {
					response.error.field = "status";
					response.error.message = "Incorrect value";
					return response;
				}

			}
		}catch(Exception e) {
			System.out.println("Updating ticket errror: "+e.getMessage());
			response.error.field = "all";
			response.error.message = e.getMessage();
		}
		return response;
	}

}

class UpdateTicketResponse {
	UpdateTicketResponse() {
		this.error = new Error();
	}

	public Ticket ticket;
	public Error error;

}

class UpdateTicketInput {

	public int ticketId;
	public Status status;

	@Override
	public String toString() {
		return "UpdateTicketInput [ticketId=" + ticketId + ", status=" + status + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
