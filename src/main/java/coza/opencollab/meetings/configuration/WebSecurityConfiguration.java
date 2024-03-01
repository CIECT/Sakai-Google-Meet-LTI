package coza.opencollab.meetings.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import uk.ac.ox.ctl.lti13.Lti13Configurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        Lti13Configurer lti13Configurer = new Lti13Configurer();
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/resources/**", "/favicon.ico", "/config.json", "/.well-known/jwks.json", "/js/**")
                                .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Client(withDefaults())
                .apply(lti13Configurer);
        ;

        return http.build();
    }
}
