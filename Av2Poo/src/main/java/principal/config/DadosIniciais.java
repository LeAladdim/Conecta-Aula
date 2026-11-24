package principal.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import principal.domain.Disciplina;
import principal.domain.Material;
import principal.domain.TipoMaterial;
import principal.repository.DisciplinaRepository;
import principal.repository.MaterialRepository;

/**
 * Coloca dados no banco na PRIMEIRA vez que o sistema roda.
 * As disciplinas são as mesmas do front. Os materiais são só exemplos para
 * testar a pesquisa enquanto a parte de "Compartilhar material" não fica pronta.
 */
@Component
public class DadosIniciais implements CommandLineRunner {

	private final DisciplinaRepository disciplinaRepository;
	private final MaterialRepository materialRepository;

	public DadosIniciais(DisciplinaRepository disciplinaRepository, MaterialRepository materialRepository) {
		this.disciplinaRepository = disciplinaRepository;
		this.materialRepository = materialRepository;
	}

	@Override
	public void run(String... args) {
		// Se já tem disciplina no banco, os dados já foram criados antes
		if (disciplinaRepository.count() > 0) {
			return;
		}

		System.out.println(">>> Criando dados iniciais do Conecta Aula...");

		Disciplina poo = criarDisciplina("Programação Orientada a Objetos", "POO",
				"Classes, herança, polimorfismo, encapsulamento e padrões de projeto.");
		Disciplina ed = criarDisciplina("Estrutura de Dados", "ED",
				"Listas, pilhas, filas, árvores, grafos e análise de complexidade.");
		Disciplina bd = criarDisciplina("Banco de Dados", "BD",
				"Modelagem relacional, SQL, normalização e transações.");
		Disciplina es = criarDisciplina("Engenharia de Software", "ES",
				"Requisitos, documentação, arquitetura e qualidade de software.");
		Disciplina web = criarDisciplina("Desenvolvimento Web", "WEB",
				"HTML, CSS, JavaScript e fundamentos de aplicações web.");
		Disciplina md = criarDisciplina("Matemática Discreta", "MD",
				"Lógica, conjuntos, relações, funções e fundamentos matemáticos.");

		// Mesma quantidade do front: POO 3, ED 2, BD 2, ES 2, WEB 2, MD 1 = 12 materiais
		criarMaterial(poo, "Resumo dos 4 pilares da POO", "Abstração, encapsulamento, herança e polimorfismo com exemplos em Java.", TipoMaterial.RESUMO, 2);
		criarMaterial(poo, "Slides - Padrões de Projeto", "Strategy, Observer e Singleton explicados com diagramas.", TipoMaterial.SLIDES, 9);
		criarMaterial(poo, "Lista de exercícios - Herança", "15 exercícios de herança e classes abstratas.", TipoMaterial.LISTA_EXERCICIOS, 15);

		criarMaterial(ed, "Slides - Pilhas e Filas", "Slides do professor João com implementação em Java.", TipoMaterial.SLIDES, 1);
		criarMaterial(ed, "Resumo de árvores binárias", "Inserção, remoção e percursos (pré, em e pós-ordem).", TipoMaterial.RESUMO, 11);

		criarMaterial(bd, "Apostila de SQL", "SELECT, JOIN, GROUP BY e subconsultas com exemplos.", TipoMaterial.APOSTILA, 4);
		criarMaterial(bd, "Resumo de normalização", "1FN, 2FN e 3FN passo a passo.", TipoMaterial.RESUMO, 12);

		criarMaterial(es, "Modelo de documento de requisitos", "Template com requisitos funcionais e não funcionais.", TipoMaterial.APOSTILA, 6);
		criarMaterial(es, "Slides - Scrum e Kanban", "Cerimônias do Scrum e como montar um quadro Kanban.", TipoMaterial.SLIDES, 18);

		criarMaterial(web, "Guia de Flexbox e Grid", "Como alinhar elementos com display flex e grid.", TipoMaterial.RESUMO, 3);
		criarMaterial(web, "Lista - JavaScript básico", "Exercícios de variáveis, funções e eventos.", TipoMaterial.LISTA_EXERCICIOS, 7);

		criarMaterial(md, "Lista - Lógica proposicional", "Tabelas-verdade e equivalências lógicas.", TipoMaterial.LISTA_EXERCICIOS, 10);
	}

	private Disciplina criarDisciplina(String nome, String sigla, String descricao) {
		// save() faz o INSERT e devolve o objeto já com o id gerado pelo banco
		return disciplinaRepository.save(new Disciplina(nome, sigla, descricao));
	}

	private void criarMaterial(Disciplina disciplina, String titulo, String descricao, TipoMaterial tipo,
			int diasAtras) {
		Material m = new Material(titulo, descricao, tipo, disciplina);
		m.setDataPublicacao(LocalDateTime.now().minusDays(diasAtras));
		materialRepository.save(m);
	}
}
