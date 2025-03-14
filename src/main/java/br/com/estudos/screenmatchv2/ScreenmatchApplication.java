package br.com.estudos.screenmatchv2;

import br.com.estudos.screenmatchv2.model.DadosSerie;
import br.com.estudos.screenmatchv2.service.ConsumoAPI;
import br.com.estudos.screenmatchv2.service.ConverteDados;
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

		ConsumoAPI consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("http://www.omdbapi.com/?t=Game+of+Thrones&apikey=20089c9e");

		ConverteDados converteDados = new ConverteDados();
		DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);

		System.out.println(json);
		System.out.println(dadosSerie);
	}
}
