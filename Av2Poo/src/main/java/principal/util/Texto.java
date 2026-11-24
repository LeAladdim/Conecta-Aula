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

	/**
	 * Diz se TODAS as palavras do termo aparecem no texto (em qualquer ordem),
	 * ignorando maiúsculas e acentos.
	 * Ex: termo "banco dados" acha "Banco de Dados"; termo "slides java" acha
	 * "Slides - Pilhas e Filas ... em Java". Termo vazio sempre retorna true.
	 */
	public static boolean contemPalavras(String texto, String termo) {
		String t = normalizar(texto);
		for (String palavra : normalizar(termo).split("\\s+")) {
			if (!palavra.isEmpty() && !t.contains(palavra)) {
				return false;
			}
		}
		return true;
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
