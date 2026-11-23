package principal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import principal.domain.Disciplina;
import principal.service.DisciplinaService;
import principal.service.PesquisaService;

/**
 * Página de uma disciplina com os materiais dela.
 * Botão do front: "Ver materiais →" em cada card de disciplina.
 */
@Controller
public class DisciplinaController {

	private final DisciplinaService disciplinaService;
	private final PesquisaService pesquisaService;

	public DisciplinaController(DisciplinaService disciplinaService, PesquisaService pesquisaService) {
		this.disciplinaService = disciplinaService;
		this.pesquisaService = pesquisaService;
	}

	// Ex: http://localhost:8080/disciplinas/1   ({id} vem da URL)
	@GetMapping("/disciplinas/{id}")
	public String detalhes(@PathVariable Long id, Model model) {
		Disciplina disciplina = disciplinaService.buscarPorId(id);

		if (disciplina == null) {
			model.addAttribute("mensagem", "Disciplina não encontrada.");
			return "error";
		}

		model.addAttribute("disciplina", disciplina);
		model.addAttribute("materiais", pesquisaService.listarPorDisciplina(disciplina));
		return "disciplina";
	}

	/**
	 * Nomes das disciplinas em JSON, para o <select> do modal "Compartilhar material".
	 * Ex: GET http://localhost:8080/api/disciplinas  ->  ["Banco de Dados", "Desenvolvimento Web", ...]
	 *
	 * @ResponseBody: devolve os dados direto (JSON), em vez de abrir uma página JSP.
	 */
	@GetMapping("/api/disciplinas")
	@ResponseBody
	public List<String> listarNomes() {
		List<String> nomes = new ArrayList<>();
		for (Disciplina d : disciplinaService.listarTodas()) {
			nomes.add(d.getNome());
		}
		return nomes;
	}
}
