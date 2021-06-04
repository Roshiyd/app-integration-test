package ai.ecma.appintegrationtest.config;

import ai.ecma.appintegrationtest.service.AuthService;
import ai.ecma.appintegrationtest.service.JwtAuthenticationEntryPoint;
import ai.ecma.appintegrationtest.service.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final
    AuthService authService;
    final
    PasswordEncoder passwordEncoder;
    final
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    final JwtFilter jwtFilter;

    public SecurityConfig(@Lazy AuthService authService, PasswordEncoder passwordEncoder, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtFilter jwtFilter) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**")
                .permitAll()
//                .regexMatchers("/course(/)?\\\\w+(/)?")
                .antMatchers(HttpMethod.GET, "/api/course/**","/api/module/**","/api/lesson/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
