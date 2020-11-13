package com.devteamvietnam.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

import com.devteamvietnam.framework.security.filter.JwtAuthenticationTokenFilter;
import com.devteamvietnam.framework.security.handle.AuthenticationEntryPointImpl;
import com.devteamvietnam.framework.security.handle.LogoutSuccessHandlerImpl;

/**
 * spring security configuration
 *
 * @author ivan
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    /**
     * Custom user authentication logic
     */
    @Autowired
    private UserDetailsService userDetailsService;
    
    /**
     * Authentication failure handling class
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * Exit processing class
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token authentication filter
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * Cross-domain filter
     */
    @Autowired
    private CorsFilter corsFilter;
    
    /**
     * Solve Cannot directly inject AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    /**
     * anyRequest | match all request paths
     * access | Can be accessed when the result of SpringEl expression is true
     * anonymous | anonymously accessible
     * denyAll | User cannot access
     * fullyAuthenticated | The user is fully authenticated and can access (automatically log in under non-remember-me)
     * hasAnyAuthority | If there are parameters, the parameters indicate permissions, and any one of them can access
     * hasAnyRole | If there is a parameter, the parameter indicates a role, any one of them can access
     * hasAuthority | If there is a parameter, the parameter indicates the authority, then its authority can be accessed
     * hasIpAddress | If there is a parameter, the parameter represents the IP address, if the user IP matches the parameter, you can access
     * hasRole | If there is a parameter, the parameter indicates a role, then its role can be accessed
     * permitAll | Users can access at will
     * rememberMe | Allow users who log in through remember-me to access
     * authenticated | user can access after login
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                // CSRF is disabled because session is not used
                .csrf().disable()
                // Authentication failure handling class
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // Based on token, so no session is needed
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // filter request
                .authorizeRequests()
                // For login verification code captchaImage allows anonymous access
                .antMatchers("/login", "/captchaImage").anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/profile/**").anonymous()
                .antMatchers("/common/download**").anonymous()
                .antMatchers("/common/download/resource**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                // All requests except the above require authentication
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // Add JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // Add CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    }

    
    /**
     * Implementation of strong hash hash encryption
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    /**
     * Identity authentication interface
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
