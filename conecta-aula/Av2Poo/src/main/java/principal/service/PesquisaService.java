package principal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import principal.domain.Disciplina;
import principal.domain.Material;
import principal.repository.MaterialRepository;
import principal.util.Texto;

/**
 * Pesquisa e listagem de materiais (só leitura).
 *
 * O envio de materiais (salvar) fica no MaterialService, separado deste.
 */
@Service
public class PesquisaService {

	private final MaterialRepository materialRepository;

	// O Spring passa o repository aqui automaticamente (injeção de dependência)
	public PesquisaService(MaterialRepository materialRepository) {
		this.materialRepository = materialRepository;
	}

	public List<Material> listarTodos() {
		return materialRepository.findAllByOrderByDataPublicacaoDesc();
	}

	public List<Material> listarPorDisciplina(Disciplina disciplina) {
		return materialRepository.findByDisciplinaOrderByDataPublicacaoDesc(disciplina);
	}

	public long contarMateriais() {
		return materialRepository.count();
	}

	/**
	 * Pesquisa por nome e/ou por disciplina. Os dois filtros são opcionais:
	 * - termo: procura no título, na descrição e no nome da disciplina
	 * - disciplinaId: mostra só os materiais daquela disciplina
	 *
	 * A comparação ignora maiúsculas e acentos ("matematica" acha "Matemática").
	 */
	public List<Material> pesquisar(String termo, Long disciplinaId) {
		List<Material> resultado = new ArrayList<>();
		String t = Texto.normalizar(termo);

		for (Material m : listarTodos()) {
			boolean disciplinaOk = disciplinaId == null || m.getDisciplina().getId().equals(disciplinaId);

			boolean termoOk = t.isEmpty()
					|| Texto.normalizar(m.getTitulo()).contains(t)
					|| Texto.normalizar(m.getDescricao()).contains(t)
					|| Texto.normalizar(m.getDisciplina().getNome()).contains(t);

			if (disciplinaOk && termoOk) {
				resultado.add(m);
			}
		}
		return resultado;
	}
}
