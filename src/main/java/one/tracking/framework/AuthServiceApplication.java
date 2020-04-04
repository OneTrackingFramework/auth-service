package one.tracking.framework;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class AuthServiceApplication extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
    return application.sources(AuthServiceApplication.class);
  }

  public static void main(final String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
    return args -> {

      System.out.println("Let's inspect the beans provided by Spring Boot:");

      final String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (final String beanName : beanNames) {
        System.out.println(beanName);
      }
    };
  }
}
