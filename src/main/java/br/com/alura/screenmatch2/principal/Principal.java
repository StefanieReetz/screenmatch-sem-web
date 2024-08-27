package br.com.alura.screenmatch2.principal;

import br.com.alura.screenmatch2.model.DadosEpisodio;
import br.com.alura.screenmatch2.model.DadosSerie;
import br.com.alura.screenmatch2.model.DadosTemporada;
import br.com.alura.screenmatch2.service.ConsumoApi;
import br.com.alura.screenmatch2.service.ConverteDados;
import com.series.model.Episodios;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para a busca");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);


        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//            for (int i = 0; i < dados.totalTemporadas(); i++) {
//                List<DadosEpisodio> episodioTemporada = temporadas.get(i).episodios();
//                for (int j = 0; j< episodioTemporada.size(); j++){
//                    System.out.println(episodioTemporada.get(j).titulo());
//                }
//            }
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        System.out.println("\nTop 5 episódios ");

        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodios(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
        System.out.println("Apartir de que ano você deseja de ver os episódios");
        var ano = leitura.nextInt();
        leitura.nextLine();
        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataDeLancamento() != null && e.getDataDeLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada:  " + e.getTemporada() +
                                "Episodio: " + e.getTitulo() +
                                "Data de lancamento: "+ e.getDataDeLancamento().format(formatador)
                ));
    }
}
