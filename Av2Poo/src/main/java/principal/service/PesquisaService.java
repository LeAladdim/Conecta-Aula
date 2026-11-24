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
	 * - termo: procura no título, na descrição, no nome e na sigla da disciplina
	 *   (ex: "POO" ou "bd" trazem todos os materiais daquela disciplina)
	 * - disciplinaId: mostra só os materiais daquela disciplina
	 *
	 * A comparação ignora maiúsculas e acentos ("matematica" acha "Matemática")
	 * e aceita várias palavras em qualquer ordem ("banco dados" acha "Banco de Dados").
	 */
	public List<Material> pesquisar(String termo, Long disciplinaId) {
		List<Material> resultado = new ArrayList<>();

		for (Material m : listarTodos()) {
			Disciplina d = m.getDisciplina();
			boolean disciplinaOk = disciplinaId == null || d.getId().equals(disciplinaId);

			// Junta tudo que pode ser pesquisado num texto só
			String onde = m.getTitulo() + " " + m.getDescricao() + " " + d.getNome() + " " + d.getSigla();
			boolean termoOk = Texto.contemPalavras(onde, termo);

			if (disciplinaOk && termoOk) {
				resultado.add(m);
			}
		}
		return resultado;
	}
}
