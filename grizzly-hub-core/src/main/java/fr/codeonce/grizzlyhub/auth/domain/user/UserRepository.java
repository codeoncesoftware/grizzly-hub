package fr.codeonce.grizzlyhub.auth.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.codeonce.grizzlyhub.team.domain.Team;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);
	public List<User> findByTeam(Team team);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByEmailAndPassword(String email, String password);
}