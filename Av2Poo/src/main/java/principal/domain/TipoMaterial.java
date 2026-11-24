package principal.domain;

/**
 * Tipos de material que podem ser enviados.
 */
public enum TipoMaterial {

	SLIDES("Slides"),
	APOSTILA("Apostila"),
	LISTA_EXERCICIOS("Lista de exercícios"),
	RESUMO("Resumo"),
	OUTRO("Outro");

	private final String descricao;

	TipoMaterial(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	/**
	 * Converte o texto do formulário (ex: "SLIDES") para o enum.
	 * Retorna null se não existir, em vez de lançar exceção.
	 */
	public static TipoMaterial converter(String texto) {
		for (TipoMaterial tipo : values()) {
			if (tipo.name().equals(texto)) {
				return tipo;
			}
		}
		return null;
	}
}
