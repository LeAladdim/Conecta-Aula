package principal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import principal.domain.Disciplina;
import principal.repository.DisciplinaRepository;
import principal.util.Texto;

@Service
public class DisciplinaService {

	private final DisciplinaRepository disciplinaRepository;

	public DisciplinaService(DisciplinaRepository disciplinaRepository) {
		this.disciplinaRepository = disciplinaRepository;
	}

	public List<Disciplina> listarTodas() {
		return disciplinaRepository.findAllByOrderByNomeAsc();
	}

	/**
	 * Busca pelo id. Retorna null se não existir.
	 */
	public Disciplina buscarPorId(Long id) {
		if (id == null) {
			return null;
		}
		return disciplinaRepository.findById(id).orElse(null);
	}

	/**
	 * Acha a disciplina pelo nome OU pela sigla, sem ligar para maiúsculas e acentos.
	 * Ex: "Banco de Dados", "banco de dados" e "BD" encontram a mesma disciplina.
	 * Usado pelo MaterialController (/api/materiais), que recebe a disciplina como texto.
	 * Retorna null se não achar.
	 */
	public Disciplina buscarPorNomeOuSigla(String texto) {
		if (Texto.vazio(texto)) {
			return null;
		}
		String t = Texto.normalizar(texto);
		for (Disciplina d : listarTodas()) {
			if (Texto.normalizar(d.getNome()).equals(t) || Texto.normalizar(d.getSigla()).equals(t)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Disciplinas cujo nome ou sigla batem com o termo da pesquisa
	 * (o campo do front diz "Buscar disciplinas ou materiais").
	 */
	public List<Disciplina> pesquisar(String termo) {
		List<Disciplina> encontradas = new ArrayList<>();
		if (Texto.vazio(termo)) {
			return encontradas;
		}
		String t = Texto.normalizar(termo);
		for (Disciplina d : listarTodas()) {
			if (Texto.normalizar(d.getNome()).contains(t) || Texto.normalizar(d.getSigla()).equals(t)) {
				encontradas.add(d);
			}
		}
		return encontradas;
	}
}
