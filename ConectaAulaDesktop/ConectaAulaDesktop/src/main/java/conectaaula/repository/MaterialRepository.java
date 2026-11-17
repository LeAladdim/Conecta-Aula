package conectaaula.repository;

import conectaaula.model.Disciplina;
import conectaaula.model.Material;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Guarda as disciplinas e os materiais compartilhados.
 * Os materiais ficam em memória e são gravados em ~/.conecta-aula/materiais.tsv,
 * então continuam existindo quando o programa é fechado e aberto de novo.
 */
public class MaterialRepository {

    private static final List<Disciplina> DISCIPLINAS = List.of(
            new Disciplina("Programação Orientada a Objetos", "POO",
                    "Classes, herança, polimorfismo, encapsulamento e padrões de projeto."),
            new Disciplina("Estrutura de Dados", "ED",
                    "Listas, pilhas, filas, árvores, grafos e análise de complexidade."),
            new Disciplina("Banco de Dados", "BD",
                    "Modelagem relacional, SQL, normalização e transações."),
            new Disciplina("Engenharia de Software", "ES",
                    "Requisitos, documentação, arquitetura e qualidade de software."),
            new Disciplina("Desenvolvimento Web", "WEB",
                    "HTML, CSS, JavaScript e fundamentos de aplicações web."),
            new Disciplina("Matemática Discreta", "MD",
                    "Lógica, conjuntos, relações, funções e fundamentos matemáticos."));

    private final List<Material> materiais = new ArrayList<>();
    private final Path arquivo;

    public MaterialRepository() {
        this(Path.of(System.getProperty("user.home"), ".conecta-aula", "materiais.tsv"));
    }

    public MaterialRepository(Path arquivo) {
        this.arquivo = arquivo.toAbsolutePath();
        if (Files.exists(this.arquivo)) {
            try {
                carregar();
                return;
            } catch (IOException e) {
                System.err.println("Não foi possível ler " + this.arquivo + ": " + e.getMessage());
                materiais.clear();
                guardarCopiaDoArquivoComProblema();
            }
        }
        materiais.addAll(exemplos());
        salvar();
    }

    public List<Disciplina> disciplinas() {
        return DISCIPLINAS;
    }

    public synchronized List<Material> todos() {
        return List.copyOf(materiais);
    }

    public synchronized List<Material> porDisciplina(String nomeDisciplina) {
        return materiais.stream().filter(m -> m.disciplina().equals(nomeDisciplina)).toList();
    }

    public synchronized int contar(String nomeDisciplina) {
        return porDisciplina(nomeDisciplina).size();
    }

    /** Busca por título, autor, tipo, descrição, nome ou sigla da disciplina. Ignora acentos e maiúsculas. */
    public synchronized List<Material> buscar(String termo) {
        String[] palavras = normalizar(termo).split("\\s+");
        List<Material> resultado = new ArrayList<>();
        for (Material m : materiais) {
            String texto = normalizar(String.join(" ", m.titulo(), m.autor(), m.tipo(), m.descricao(),
                    m.disciplina(), siglaDe(m.disciplina())));
            boolean todasEncontradas = true;
            for (String palavra : palavras) {
                if (!palavra.isEmpty() && !texto.contains(palavra)) {
                    todasEncontradas = false;
                    break;
                }
            }
            if (todasEncontradas) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    /** Adiciona o material. Retorna false se não foi possível gravá-lo em disco. */
    public boolean adicionar(Material material) {
        synchronized (this) {
            materiais.add(material);
        }
        return salvar();
    }

    // ---------- persistência ----------

    private void carregar() throws IOException {
        for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
            if (linha.isBlank()) {
                continue;
            }
            String[] c = linha.split("\t", -1);
            if (c.length != 6) {
                continue; // linha inválida é ignorada
            }
            // ordem no arquivo: disciplina, título, autor, tipo, link, descrição
            materiais.add(new Material(desfazer(c[1]), desfazer(c[2]), desfazer(c[3]),
                    desfazer(c[4]), desfazer(c[5]), desfazer(c[0])));
        }
    }

    private synchronized boolean salvar() {
        try {
            Files.createDirectories(arquivo.getParent());
            List<String> linhas = new ArrayList<>();
            for (Material m : materiais) {
                linhas.add(String.join("\t", proteger(m.disciplina()), proteger(m.titulo()), proteger(m.autor()),
                        proteger(m.tipo()), proteger(m.link()), proteger(m.descricao())));
            }
            Path temporario = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");
            Files.write(temporario, linhas, StandardCharsets.UTF_8);
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Não foi possível gravar " + arquivo + ": " + e.getMessage());
            return false;
        }
    }

    private void guardarCopiaDoArquivoComProblema() {
        try {
            Files.move(arquivo, arquivo.resolveSibling(arquivo.getFileName() + ".bak"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignorada) {
            // se não der para copiar, o arquivo será recriado mesmo assim
        }
    }

    private static String proteger(String s) {
        StringBuilder sb = new StringBuilder();
        for (char ch : (s == null ? "" : s).toCharArray()) {
            switch (ch) {
                case '\\' -> sb.append("\\\\");
                case '\t' -> sb.append("\\t");
                case '\n' -> sb.append("\\n");
                case '\r' -> { }
                default -> sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static String desfazer(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\\' && i + 1 < s.length()) {
                char proximo = s.charAt(++i);
                switch (proximo) {
                    case 't' -> sb.append('\t');
                    case 'n' -> sb.append('\n');
                    default -> sb.append(proximo);
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    // ---------- utilitários ----------

    private static String normalizar(String s) {
        String semAcento = Normalizer.normalize(s == null ? "" : s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toLowerCase(Locale.ROOT).trim();
    }

    private static String siglaDe(String nomeDisciplina) {
        return DISCIPLINAS.stream().filter(d -> d.nome().equals(nomeDisciplina))
                .map(Disciplina::sigla).findFirst().orElse("");
    }

    /** Materiais de exemplo (12), na mesma distribuição da página original: 3, 2, 2, 2, 2 e 1. */
    private static List<Material> exemplos() {
        String autor = "Comunidade Conecta Aula";
        return List.of(
                new Material("Resumo: herança e polimorfismo em Java", autor, "Resumo", "",
                        "Conceitos de herança, sobrescrita e polimorfismo com exemplos em Java.",
                        "Programação Orientada a Objetos"),
                new Material("Lista de exercícios: classes e objetos", autor, "Lista de exercícios", "",
                        "Exercícios de encapsulamento, construtores e métodos.",
                        "Programação Orientada a Objetos"),
                new Material("Slides: padrões de projeto", autor, "Slides", "",
                        "Visão geral dos padrões Singleton, Factory, Observer e Strategy.",
                        "Programação Orientada a Objetos"),
                new Material("Guia visual de pilhas e filas", autor, "Resumo", "",
                        "Operações, usos comuns e implementação com vetores e listas encadeadas.",
                        "Estrutura de Dados"),
                new Material("Árvores binárias de busca", autor, "Slides", "",
                        "Inserção, busca, remoção e percursos, com análise de complexidade.",
                        "Estrutura de Dados"),
                new Material("Cheatsheet de SQL", autor, "Resumo", "",
                        "SELECT, JOIN, GROUP BY e subconsultas em uma página.",
                        "Banco de Dados"),
                new Material("Normalização: 1FN até 3FN", autor, "Resumo", "",
                        "Passo a passo de normalização com exemplos de tabelas.",
                        "Banco de Dados"),
                new Material("Modelo de documento de requisitos", autor, "PDF", "",
                        "Estrutura sugerida para requisitos funcionais e não funcionais.",
                        "Engenharia de Software"),
                new Material("Introdução à UML", autor, "Slides", "",
                        "Diagramas de casos de uso e de classes explicados com exemplos.",
                        "Engenharia de Software"),
                new Material("Fundamentos de HTML e CSS", autor, "Resumo", "",
                        "Estrutura de uma página, seletores, box model e flexbox.",
                        "Desenvolvimento Web"),
                new Material("JavaScript: DOM e eventos", autor, "Resumo", "",
                        "Como selecionar elementos, tratar eventos e alterar a página.",
                        "Desenvolvimento Web"),
                new Material("Lógica proposicional", autor, "Lista de exercícios", "",
                        "Tabelas-verdade, equivalências e exercícios resolvidos.",
                        "Matemática Discreta"));
    }
}
