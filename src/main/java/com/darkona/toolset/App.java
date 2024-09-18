package com.darkona.toolset;

import net.datafaker.Faker;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.darkona.toolset.logging.LogStrings.bannerize;
import static com.darkona.toolset.logging.LogStrings.red;

@SpringBootApplication(
		exclude = {
				DataSourceAutoConfiguration.class,
				DataSourceTransactionManagerAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class,
				MongoAutoConfiguration.class
		}
)
@EnableAspectJAutoProxy

public class App {

	public static void main(String[] args)
    throws IOException {
		SpringApplication.run(App.class, args);

		System.out.println("Do I have a console? = " + System.console());
		System.out.println("What is my encoding? = " + Charset.defaultCharset().displayName());
		System.out.append("Hello World!").write("\n".getBytes());
		System.out.println(bannerize(red("Hello World!"), 100));
	}


	@JsonIgnore
	public static final Faker faker = new Faker();
}
