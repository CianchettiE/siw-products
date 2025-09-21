package it.uniroma3.siw.authentication;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                // Chiunque (anche non loggato) può accedere a queste pagine
                .requestMatchers(HttpMethod.GET, "/", "/index", "/login", "/register", "/css/**", "/images/**", "favicon.ico").permitAll()
                .requestMatchers(HttpMethod.GET, "/products", "/products/**", "/formSearchProducts").permitAll()
                // Chiunque può inviare dati (POST) a queste pagine
                .requestMatchers(HttpMethod.POST, "/register", "/login", "/searchProducts").permitAll()
                // Solo gli admin possono accedere a /admin/**
                .requestMatchers("/admin/**").hasAuthority(ADMIN_ROLE)
                // Qualsiasi altra richiesta necessita di autenticazione
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    // Logica di reindirizzamento post-login
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ADMIN_ROLE))) {
                        response.sendRedirect("/admin/home");
                    } else {
                        response.sendRedirect("/products");
                    }
                })
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }
}

