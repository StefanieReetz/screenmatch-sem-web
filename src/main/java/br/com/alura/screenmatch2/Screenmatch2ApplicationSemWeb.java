package br.com.alura.screenmatch2;

import br.com.alura.screenmatch2.principal.Principal;
import br.com.alura.screenmatch2.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class Screenmatch2ApplicationSemWeb implements CommandLineRunner {
//
//	@Autowired
//	private SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(Screenmatch2ApplicationSemWeb.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		Principal principal = new Principal(repository);
//		principal.exibeMenu();
//	}
//}