package app.bluefig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestBluefigApplication {

	public static void main(String[] args) {
		SpringApplication.from(BluefigApplication::main).with(TestBluefigApplication.class).run(args);
	}

}
