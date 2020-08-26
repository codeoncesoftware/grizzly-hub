package fr.codeonce.grizzlyhub.auth.domain.user.token;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<VerificationToken, String> {

	Optional<VerificationToken> findByUserEmail(String userEmail);
	
	Optional<VerificationToken> findByToken(String token);
	
}
