package school.spring.school.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import school.spring.school.models.User;
import school.spring.school.models.User.Role;
import school.spring.school.repositories.UserRepository;
@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository ur;
	
	private final PasswordEncoder passwordEncoder;
	
	
	
	public UserService(PasswordEncoder passwordEncoder) {
		super();
		this.passwordEncoder = passwordEncoder;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = ur.findByUsername(username);
		if(user == null) {
			System.out.println("No User with username "+username+" found");
			throw new UsernameNotFoundException("No User with username "+username+" found");
		}else {
			System.out.println("User with username "+username+" found");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
	}
	
	
	public User create(User u) {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		return ur.save(u);
	}
	
	public boolean delete(int id) {
		try {
			ur.delete(ur.findById(id).get());
			return true;
		}catch(Exception e) {
		
			return false;
		}
		
	}
	
	public User findById(int id) {
		try {
			
			return ur.findById(id).get();
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public User findByUsername(String username) {
		try {
			
			return ur.findByUsername(username);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	public List<User> findAllDevs(){
		return ur.findByRole(Role.DEV);
	}
	
	

	
	
}