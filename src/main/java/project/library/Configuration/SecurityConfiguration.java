package project.library.Configuration;



import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import project.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/index").permitAll() // Allow access to index page without authentication
                .antMatchers("/users/show/{id}").authenticated()
                .antMatchers("/users/changePassword/{id}").authenticated()
                .antMatchers("/export/**").authenticated()
                .antMatchers("/books/add").hasAuthority("SuperAdmin")
                .antMatchers("/books/{id}/delete").hasAuthority("SuperAdmin")
                .antMatchers("/orders/viewAllOrders").hasAuthority("SuperAdmin")
                .antMatchers("/orders/extendOrder").hasAuthority("SuperAdmin")
                .antMatchers("/users/viewAllUsers").hasAuthority("SuperAdmin")
                .antMatchers("/users/deleteUser").hasAuthority("SuperAdmin")
                .antMatchers("/users/disable").hasAuthority("SuperAdmin")
                .antMatchers("/users/enable").hasAuthority("SuperAdmin")
                .antMatchers("/users/edit/**").hasAuthority("SuperAdmin")
                .antMatchers("/users/new").hasAuthority("SuperAdmin")
                .antMatchers("/books/**", "/orders/**" , "/users/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginSuccessHandler()) // Set the custom success handler
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessDenied") // Redirect to error page for access denied
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
                .csrf().disable(); // Disable CSRF for simplicity, you might want to enable it in a production environment
    }



    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> response.sendRedirect("/index");
    }
    }

