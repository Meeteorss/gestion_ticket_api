package school.spring.school.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


import school.spring.school.models.Incident;
import school.spring.school.models.Ticket;
import school.spring.school.models.User;
import school.spring.school.models.Incident.Urgency;
import school.spring.school.models.Ticket.Status;
import school.spring.school.models.User.Role;
import school.spring.school.services.IncidentService;
import school.spring.school.services.TicketService;
import school.spring.school.services.UserService;

@RestController
@RequestMapping(value = "/client")
public class ClientController {
	@Autowired
	TicketService ts;
	@Autowired
	UserService us;
	@Autowired
	IncidentService is;

	@PostMapping("/tickets")
	public CreateTicketResponse create(@RequestBody CreateTicketInput body, HttpServletRequest request) {
		System.out.println("body: "+body);
		
		CreateTicketResponse response = new CreateTicketResponse();
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		try {
			
			
			String token = authorizationHeader.substring("Bearer ".length());
			Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
			JWTVerifier verifier = JWT.require(alg).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			String username = decodedJWT.getSubject();
			User user = us.findByUsername(username);
			Incident incident = is.create(new Incident(body.software,body.description,body.environment,body.urgency));
			//Incident incident = null;
			// User dev = us.findById(2);
			if (user == null) {
				response.error.field = "user";
				response.error.message = "user not found";
				return response;
			} else if (user.getRole() != Role.CLIENT) {
				response.error.field = "user";
				response.error.message = "user must be a client only";
				return response;
			} else if (incident == null) {
				response.error.field = "incident";
				response.error.message = "incident not found";
				return response;
			} else {
				Ticket ticket = new Ticket();
				ticket.setClient(user);
				// ticket.setDev(dev);
				ticket.setIncident(incident);
				ticket.setStatus(Status.PENDING);

				Ticket t = ts.create(ticket);
				if (t != null) {
					response.ticket = t;
				} else {
					response.error.field = "";
					response.error.message = "An error has occured";
				}
				return response;
			}
		} catch (Exception e) {
			System.out.println("Error creating ticket from controller: " + e.getMessage());
			return response;
		}

	}

	@GetMapping("/tickets")
	public List<Ticket> findTicketsByClient(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		try {
			String token = authorizationHeader.substring("Bearer ".length());
			Algorithm alg = Algorithm.HMAC256("jeoaizjjqqd".getBytes());
			JWTVerifier verifier = JWT.require(alg).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			String username = decodedJWT.getSubject();

			return ts.getTicketsByClient(username);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return new ArrayList<>();
		}

	}
}

class Error {

	public String field;
	public String message;
}

class CreateTicketInput {
	public String software;
	public String environment;
	public String description;
	public Urgency urgency;
	@Override
	public String toString() {
		return "CreateTicketInput [software=" + software + ", environment=" + environment + ", description="
				+ description + ", urgency=" + urgency + "]";
	}
	
}
class BodyInput {
	CreateTicketInput body;
}

class CreateTicketResponse {
	public CreateTicketResponse() {
		this.error = new Error();
	}

	public Ticket ticket;
	public Error error;
}