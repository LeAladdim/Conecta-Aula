package principal.util;

import java.text.Normalizer;

/**
 * Funções auxiliares para trabalhar com texto.
 */
public class Texto {

	private Texto() {
		// classe utilitária, não precisa ser instanciada
	}

	/**
	 * Deixa o texto minúsculo e sem acentos, para a busca encontrar
	 * "matematica" mesmo quando o nome é "Matemática".
	 */
	public static String normalizar(String texto) {
		if (texto == null) {
			return "";
		}
		String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		return semAcento.toLowerCase().trim();
	}

	public static boolean vazio(String texto) {
		return texto == null || texto.isBlank();
	}

	/**
	 * Tira espaços do começo/fim. Se ficar vazio, devolve null.
	 */
	public static String limpar(String texto) {
		return vazio(texto) ? null : texto.trim();
	}
}
