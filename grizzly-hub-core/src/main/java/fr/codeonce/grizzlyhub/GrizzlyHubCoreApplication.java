package fr.codeonce.grizzlyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import fr.codeonce.grizzlyhub.auth.domain.config.GrizzlyCoreProperties;

@EnableAsync
@EnableCaching(proxyTargetClass = true)
@EnableConfigurationProperties(value = {  MongoProperties.class, GrizzlyCoreProperties.class })
@SpringBootApplication
public class GrizzlyHubCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrizzlyHubCoreApplication.class, args);
	}

}
