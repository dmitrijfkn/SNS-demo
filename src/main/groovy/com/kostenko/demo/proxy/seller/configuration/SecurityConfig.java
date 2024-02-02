package com.kostenko.demo.proxy.seller.configuration;


import com.kostenko.demo.proxy.seller.service.UserDetailsServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuration class for defining security-related settings.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * JWT Authentication filter for handling authentication using JWT tokens.
     */
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Custom security configuration for requests with personal user data that should only be visible to the user itself.
     */
    private final UserSecurity userSecurity;

    /**
     * Service for loading user details and managing user authentication.
     */
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructs a SecurityConfig with the specified dependencies.
     *
     * @param jwtAuthFilter      JWT Authentication filter for handling authentication using JWT tokens.
     * @param userSecurity       Custom security configuration for user-related access restrictions.
     * @param userDetailsService Service for loading user details and managing user authentication.
     */
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserSecurity userSecurity, UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userSecurity = userSecurity;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Defines the whitelist of URLs that do not require authentication.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**"
    };


    /**
     * Configures the security settings for the application.
     *
     * @param http The HttpSecurity instance to configure.
     * @return The SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers("/user/registration", "/user/login", "/user/refreshToken", "/user/logout").permitAll()
                                .requestMatchers("/post/create").permitAll()
                                .requestMatchers("/user/page/{userId}").permitAll()
                                .requestMatchers("/user/delete/{userId}").access(userSecurity)
                                .anyRequest().permitAll()
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    /**
     * Configures the password encoder for securing user passwords.
     *
     * @return The configured PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Configures the ModelMapper for object mapping.
     *
     * @return The configured ModelMapper.
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    /**
     * Configures the AuthenticationProvider for authenticating users.
     *
     * @return The configured AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    /**
     * Configures the AuthenticationManager for handling authentication.
     *
     * @param config The AuthenticationConfiguration.
     * @return The configured AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /**
     * Configures custom security settings for web requests.
     *
     * @return The configured WebSecurityCustomizer.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(AUTH_WHITELIST));
    }
}