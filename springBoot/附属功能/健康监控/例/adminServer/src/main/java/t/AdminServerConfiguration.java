package t;




import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author hcg
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableAdminServer
@Import({})
public class AdminServerConfiguration extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(AdminServerConfiguration.class, args);
    }

}
