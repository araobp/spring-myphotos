package araobp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Value("${araobp.myphotos.username.default}")
	private String usernameDefault;

	@Value("${araobp.myphotos.password.default}")
	private String passwordDefault;
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(usernameDefault)
                .password("{noop}" + passwordDefault)  // TODO: avoid using {noop}
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