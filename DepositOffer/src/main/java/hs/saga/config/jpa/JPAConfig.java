package hs.saga.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = "com.study.jpa")
public class JPAConfig {

    @Bean
    public AuditorAware<String> auditorAware() {

//        return new AuditorAware<>() {
//            @Override
//            public Optional<Object> getCurrentAuditor() {
//                return Optional.empty();
//            }
//        };
        // TODO : Security
        return ()->Optional.of("User ID");
    }
}
