package br.com.estudos.screenmatchv2.principal;

import br.com.estudos.screenmatchv2.model.DadosEpisodio;
import br.com.estudos.screenmatchv2.model.DadosSerie;
import br.com.estudos.screenmatchv2.model.DadosTemporada;
import br.com.estudos.screenmatchv2.model.Episodio;
import br.com.estudos.screenmatchv2.service.ConsumoAPI;
import br.com.estudos.screenmatchv2.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

//		System.out.println(temporadaList);
//		temporadaList.forEach(System.out::println);
//
//        temporadaList.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 Episódios:");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equals("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

//        System.out.println("\nTop 5 Episódios por temporada:");
        List<Episodio> episodios = temporadaList.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d))
                ).collect(Collectors.toList());

        episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

//        System.out.println("Informe o nome do episódio a ser buscado: ");
//        var titulo = scanner.nextLine();
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(titulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()) {
//            System.out.println(episodioBuscado);
//        }

//        System.out.println("A partir de que ano deseja assistir os episodios? ");
//        var ano = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " - Episódio " + " (" + e.getNumero() + ") - " + e.getTitulo()  +
//                                " - Data de lançamento : " + e.getDataLancamento().format(formatter)
//                ));

        Map<Integer, Double> avaliacoesTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println("\nMédia das temporadas:");
        for (Map.Entry<Integer, Double> entry : avaliacoesTemporada.entrySet()) {
            System.out.println(entry.getKey() + "ª temporada: " + entry.getValue());
        }

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("\nMédia de avaliações: " + est.getAverage() +
                "\nMelhor avaliação: " + est.getMax() +
                "\nMenor avaliação: " + est.getMin());

    }
}
