package br.com.rafael.catalogo.config;


import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.auth.In;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	private final ResponseMessage m200 = simpleMessage(200, "Chamada realizada com sucesso");
	private final ResponseMessage m201 = simpleMessage(201,"Recurso criado");
	private final ResponseMessage m204 = simpleMessage(204, "Atualização ok");
	private final ResponseMessage m401 = simpleMessage(401, "Autorização é requerida");
	private final ResponseMessage m403 = simpleMessage(403, "Não autorizado");
	private final ResponseMessage m404 = simpleMessage(404, "Objeto não encontrado");
	private final ResponseMessage m422 = simpleMessage(422,"Erro de validação");
	private final ResponseMessage m500 = simpleMessage(500, "Erro inesperado");
	
	
	
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.POST, Arrays.asList(m201,m403,m422,m500,m200))
				.globalResponseMessage(RequestMethod.GET, Arrays.asList(m403,m404,m500))
				.globalResponseMessage(RequestMethod.PUT, Arrays.asList(m403,m204,m422,m500))
				.globalResponseMessage(RequestMethod.DELETE, Arrays.asList(m403,m404,m200))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.securitySchemes(Arrays.asList(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
				.securityContexts(Arrays.asList(securityContext()))
				.apiInfo(apiInfo());
		
	}
	
	
	
	private ResponseMessage simpleMessage(int code, String msg) {
		return new ResponseMessageBuilder().code(code).message(msg).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Demo Reference with spring/spring cloud")
				.version("1.0").description("Rafael Marinheiro ") .build();
	}
	
	 private List<SecurityReference> defaultAuth() {
		    AuthorizationScope authorizationScope = new AuthorizationScope(
		        "global", "accessEverything");
		    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		    authorizationScopes[0] = authorizationScope;
		    return Arrays.asList(new SecurityReference("apiKey",
		        authorizationScopes));
		    }
	 
	 
	   private SecurityContext securityContext() {
	        return SecurityContext.builder().securityReferences(defaultAuth())
	            .forPaths(PathSelectors.ant("/pedido/**")).build();
	    }

}
