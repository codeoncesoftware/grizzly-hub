package fr.codeonce.grizzlyhub.swagger.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerListDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerToShow;

public interface ISwaggerService {
	public Swagger saveSwagger(SwaggerDto swaggerDto) throws JSONException, IOException, Exception;

	public  Page<Swagger> getAllSwaggers(int page,int pageSize);

	public List<Swagger> getSwaggersFromMicroservice(String microserviceId);

	public Swagger getSwaggerById(String id);

	void delete(String id);

	public List<Swagger> getSwaggersWithLastVersionFromMicroservice(String microserviceId);

	public Swagger getLastSwaggerByEnvironmentAndMicroservice(String environment, String microserviceId);

	public Swagger updateSwagger(SwaggerDto swaggerDto, String id) throws JSONException, IOException, Exception;

	public List<Swagger> getSwaggerByLastModifiedDate();

	List<Swagger> getSwaggersWithLastEnvironmentFromMicroservice(String microserviceId);

	List<Swagger> getSwaggersFromEnvironmentAndMicroservice(String environment, String microserviceId);

	SwaggerListDto updateSwaggerList(SwaggerListDto swaggerListDto) throws JSONException, IOException, Exception;
	
	public SwaggerListDto addMicroServiceAndSwaggers(SwaggerListDto swaggerListDto) throws JSONException, IOException, Exception;

	public List<Swagger> getSwaggersVersionsFromEnvironmentAndMicroservice(String environment, String microserviceId);
	
	public List<Swagger> addSwaggerFiles(MultipartFile[] files, String[] environments, Microservice microservice) throws Exception;

	public Swagger saveSwaggerForNotification(SwaggerDto swaggerDto) throws Exception;

	SwaggerToShow getSwaggerFromMicroservice(String microserviceId);

	List<Swagger> getSwaggersToShow(String microserviceId);

}
