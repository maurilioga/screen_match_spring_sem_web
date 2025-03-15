package br.com.estudos.screenmatchv2;

import br.com.estudos.screenmatchv2.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Projeto Spring sem Web");

		Principal principal = new Principal();
		principal.exibirMenu();
	}
}
