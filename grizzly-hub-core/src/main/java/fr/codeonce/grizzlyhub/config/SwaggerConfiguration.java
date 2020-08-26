package fr.codeonce.grizzlyhub.config;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Autowired
	private BuildProperties buildProperties;
	

	@Bean
	public Docket api() {
		
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build()
                .globalOperationParameters(parameters())
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		String buildTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK)
				.withZone(ZoneOffset.UTC).format(buildProperties.getTime());

		return new ApiInfoBuilder().title("Grizzly HUB Api")//
				.version(buildProperties.getVersion())//
				.description(String.format("Build Time: %s", buildTime))//
				.build();
	}
	

	
	List<Parameter> parameters() {
        ArrayList<Parameter> parameters = new ArrayList<>();
        Parameter parameter = new ParameterBuilder()
                .name("Authorization")
                .description("Bearer {genereated_token}")
                .parameterType("header")
                .modelRef(new ModelRef("string"))
                .required(false)
                .defaultValue("")
                .build();
        parameters.add(parameter);
        return parameters;
    }
	
}