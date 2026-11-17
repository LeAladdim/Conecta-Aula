package conectaaula.model;

/** Material de estudo compartilhado por um aluno em uma disciplina. */
public record Material(String titulo, String autor, String tipo, String link, String descricao, String disciplina) {
}
