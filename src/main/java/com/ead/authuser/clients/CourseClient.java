package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    private final RestTemplate restTemplate;

    private final UtilsService utilsService;

    @Autowired
    public CourseClient(RestTemplate restTemplate, UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

    @Value("${ead.api.url.course}")
    String REQUEST_URL_COURSE;

    //@Retry(name = "retryInstance", fallbackMethod = "retryfallback")
    /*Circuitbreaker envia de primeira a msg pro usuário dizendo que a api solicitada não está funcionando, ao invés de
    fincar enviando requisições. Nesse meu caso defini no yml que dps de 2 chamadas ele não tenta mais enviar a requisição
    para o course, dps de duas chamadas ele já envia o erro diretamente.*/
    /* CircuitBreaker com rotas alternativas -> @CircuitBreaker(name = "circuitbreakerInstance",
    fallbackMethod = "circuitbreakerfallback"), dessa forma nunca envia o erro, o cliente pode achar que não existe esse
    user.*/
    @CircuitBreaker(name = "circuitbreakerInstance")
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {
        List<CourseDto> searchResult = null;
        String url = REQUEST_URL_COURSE + this.utilsService.createUrl(userId, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};
        // fazer a solicitação para o CourseMicroservice, a outra api. para obter a listagem de um curso para um determinado usuário.
        ResponseEntity<ResponsePageDto<CourseDto>> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        searchResult = result.getBody().getContent();
        log.debug(" Response Number of Elements: {} ", searchResult.size());
        log.info( "Ending request /courses userId {} ", userId);
        return new PageImpl<>(searchResult);
    }

    // Faz parte do CircuitBreaker com rotas alternativas
    public Page<CourseDto> circuitbreakerfallback(UUID userId, Pageable pageable, Throwable t) {
        log.error("Inside circuit breaker fallback , cause - {}" + t.toString());
        List<CourseDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

    public Page<CourseDto> retryfallback(UUID userId, Pageable pageable, Throwable t) {
        log.error("Inside retry retryfallback , cause - {}" + t.toString());
        List<CourseDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

}
