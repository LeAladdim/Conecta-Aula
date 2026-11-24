package principal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import principal.service.DisciplinaService;
import principal.service.PesquisaService;
import principal.util.Texto;

/**
 * Lista de materiais e pesquisa.
 * Botões do front:
 *  - campo "Buscar disciplinas ou materiais..." do banner  -> /materiais?busca=...
 *  - "Materiais" no menu e "Ver todos os materiais →"     -> /materiais
 */
@Controller
public class PesquisaController {

	private final PesquisaService pesquisaService;
	private final DisciplinaService disciplinaService;

	public PesquisaController(PesquisaService pesquisaService, DisciplinaService disciplinaService) {
		this.pesquisaService = pesquisaService;
		this.disciplinaService = disciplinaService;
	}

	// Ex: http://localhost:8080/materiais?busca=pilha&disciplina=2
	// Os dois parâmetros são opcionais (required = false)
	@GetMapping("/materiais")
	public String pesquisar(@RequestParam(required = false) String busca,
			@RequestParam(required = false) Long disciplina, Model model) {

		model.addAttribute("materiais", pesquisaService.pesquisar(busca, disciplina));
		model.addAttribute("disciplinas", disciplinaService.listarTodas()); // para o <select> de filtro
		model.addAttribute("busca", Texto.limpar(busca));
		model.addAttribute("disciplinaSelecionada", disciplina);

		// Também mostra as disciplinas cujo nome bate com a busca
		if (disciplina == null) {
			model.addAttribute("disciplinasEncontradas", disciplinaService.pesquisar(busca));
		}
		return "materiais";
	}
}
