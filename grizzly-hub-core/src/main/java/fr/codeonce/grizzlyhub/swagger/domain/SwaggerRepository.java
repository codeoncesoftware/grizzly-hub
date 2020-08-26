package fr.codeonce.grizzlyhub.swagger.domain;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SwaggerRepository extends MongoRepository<Swagger, String> {

    @Query(value="{'microserviceId' : ?0}", fields="{ 'lastUpdateDate' : 1, 'environment' : 1, 'url_content' : 1, 'version':1}" ,sort ="{ environment : -1, lastUpdateDate : 1 }" )
    List<Swagger> findAllByMicroserviceIdOrderByEnvironmentDescLastUpdateDateDesc(String microserviceId);
    List<Swagger> findAllByMicroserviceIdOrderByLastUpdateDateDescEnvironmentDesc(String microserviceId);
    List<Swagger> findAllByMicroserviceIdOrderByVersionDescLastUpdateDateDesc(String microserviceId);
    List<Swagger> findAllByMicroserviceIdOrderByLastUpdateDate(String microserviceId);
//    List<Swagger> findAllByMicroserviceIdOrderByLastUpdateDate(String microserviceId);
    List<Swagger> findAllByEnvironmentAndMicroserviceIdOrderByLastUpdateDate(String environment, String microserviceId);
    Swagger findFirstByEnvironmentAndMicroserviceIdOrderByLastUpdateDateDesc(String environment, String microserviceId);
    List<Swagger> findAllByEnvironmentAndMicroserviceIdOrderByVersionDescLastUpdateDateDesc(String environment, String microserviceId);
    List<Swagger> findAllByMicroserviceId(String microserviceId);

}
