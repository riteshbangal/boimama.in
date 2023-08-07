package in.boimama.readstories.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    /* Let's use default one from application.yaml.
    // TODO: Implement JWT Authentication & Authorization
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.withUsername("rcb")
                .password(passwordEncoder.encode("rcb"))
                .roles("USER","HR")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/story/**").permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/admin/**").authenticated()
                .and()
                .authorizeHttpRequests()
                .and().httpBasic() // Works with --header 'Authorization: Basic YWRtaW46YWRtaW4='
                //.and().formLogin() // This redirects to spring-security login page
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET).permitAll()
                .and().build();
    }
}
