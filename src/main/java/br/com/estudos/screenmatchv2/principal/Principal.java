package br.com.estudos.screenmatchv2.principal;

import br.com.estudos.screenmatchv2.model.DadosSerie;
import br.com.estudos.screenmatchv2.model.DadosTemporada;
import br.com.estudos.screenmatchv2.service.ConsumoAPI;
import br.com.estudos.screenmatchv2.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private final String ENDERECO = "http://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=20089c9e";

    private Scanner scanner = new Scanner(System.in);

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConverteDados converteDados = new ConverteDados();

    public void exibirMenu() {

        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadaList = new ArrayList<>();

		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);

			temporadaList.add(dadosTemporada);
		}

		//System.out.println(temporadaList);
		temporadaList.forEach(System.out::println);

        temporadaList.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    }
}
