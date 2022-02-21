package araobp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 		
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	
    	String username = System.getenv().get("ARAOBP_MYPHOTOS_USERNAME_DEFAULT");
    	String password = System.getenv().get("ARAOBP_MYPHOTOS_PASSWORD_DEFAULT");
    	if (username == null) username = "test";
    	if (password == null) password = "passw0rd";
    	
    	auth.inMemoryAuthentication()
                .withUser(username)
                .password("{noop}" + password)  // TODO: avoid using {noop}
                .roles("USER");
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors();
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }  
}