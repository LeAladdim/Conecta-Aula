package principal.domain;

/**
 * Dados que chegam do modal "Compartilhar material" (JSON) e que a API devolve.
 *
 * Esta classe NÃO é salva no banco: ela só transporta os dados (um "DTO").
 * O MaterialService converte ela em um Material (que é a @Entity salva no H2),
 * assim o material novo aparece nos cards, na página da disciplina e na pesquisa.
 */
public class MaterialEstudo {
    private String titulo;
    private String disciplina;

    public MaterialEstudo() {}

    public MaterialEstudo(String titulo, String disciplina) {
        this.titulo = titulo;
        this.disciplina = disciplina;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }
}
