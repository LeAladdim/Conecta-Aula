package principal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import principal.domain.Disciplina;
import principal.domain.Material;
import principal.domain.MaterialEstudo;
import principal.domain.TipoMaterial;
import principal.repository.MaterialRepository;
import principal.util.Texto;

/**
 * Regras do "Compartilhar material".
 * Recebe um MaterialEstudo (o que veio do modal) e salva como Material no banco.
 */
@Service
public class MaterialService {

	private static final String DESCRICAO_PADRAO = "Material compartilhado pela comunidade.";

	private final MaterialRepository materialRepository;
	private final DisciplinaService disciplinaService;
	private final PesquisaService pesquisaService;

	public MaterialService(MaterialRepository materialRepository, DisciplinaService disciplinaService,
			PesquisaService pesquisaService) {
		this.materialRepository = materialRepository;
		this.disciplinaService = disciplinaService;
		this.pesquisaService = pesquisaService;
	}

	/**
	 * Confere os dados. Retorna a mensagem de erro, ou null se estiver tudo certo.
	 */
	public String validar(MaterialEstudo dados) {
		if (dados == null || Texto.vazio(dados.getTitulo()) || dados.getTitulo().trim().length() < 3) {
			return "O título precisa ter pelo menos 3 caracteres.";
		}
		if (dados.getTitulo().trim().length() > 100) {
			return "O título pode ter no máximo 100 caracteres.";
		}
		if (disciplinaService.buscarPorNomeOuSigla(dados.getDisciplina()) == null) {
			return "Disciplina não encontrada. Escolha uma das disciplinas da lista.";
		}
		return null;
	}

	/**
	 * Salva no banco. Chame validar() antes!
	 * Devolve os dados já com o nome oficial da disciplina (ex: "bd" vira "Banco de Dados").
	 */
	public MaterialEstudo salvar(MaterialEstudo dados) {
		Disciplina disciplina = disciplinaService.buscarPorNomeOuSigla(dados.getDisciplina());

		// O modal só manda título e disciplina; os outros campos recebem um valor padrão
		Material material = new Material(dados.getTitulo().trim(), DESCRICAO_PADRAO, TipoMaterial.OUTRO, disciplina);
		materialRepository.save(material);

		return converter(material);
	}

	/**
	 * Lista os materiais do banco no formato do MaterialEstudo.
	 * Se vier uma disciplina (nome ou sigla), mostra só os dela.
	 */
	public List<MaterialEstudo> listar(String disciplina) {
		List<Material> materiais;

		if (Texto.vazio(disciplina)) {
			materiais = pesquisaService.listarTodos();
		} else {
			Disciplina d = disciplinaService.buscarPorNomeOuSigla(disciplina);
			materiais = (d == null) ? new ArrayList<>() : pesquisaService.listarPorDisciplina(d);
		}

		List<MaterialEstudo> resultado = new ArrayList<>();
		for (Material m : materiais) {
			resultado.add(converter(m));
		}
		return resultado;
	}

	private MaterialEstudo converter(Material material) {
		return new MaterialEstudo(material.getTitulo(), material.getDisciplina().getNome());
	}
}
