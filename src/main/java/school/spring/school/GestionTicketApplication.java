package school.spring.school;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import school.spring.school.models.User;
import school.spring.school.models.User.Role;
import school.spring.school.services.UserService;

@SpringBootApplication
public class GestionTicketApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GestionTicketApplication.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	CommandLineRunner run(UserService us) {
		return args -> {
			try {
				us.create(new User("admin1","admin1@test.com","123456",Role.ADMIN));
				us.create(new User("admin2","admin2@test.com","123456",Role.ADMIN));
				us.create(new User("client1","client1@test.com","123456",Role.CLIENT));
				us.create(new User("client2","client2@test.com","123456",Role.CLIENT));
				us.create(new User("dev1","dev1@test.com","123456",Role.DEV));
				us.create(new User("dev2","dev2@test.com","123456",Role.DEV));
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			
		};
	}
	

}
