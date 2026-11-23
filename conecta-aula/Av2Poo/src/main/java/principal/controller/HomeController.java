package principal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import principal.service.DisciplinaService;
import principal.service.PesquisaService;

/**
 * Página inicial.
 * Botão do front: "Disciplinas" no menu (vai para /#disciplinas)
 */
@Controller
public class HomeController {

	private final DisciplinaService disciplinaService;
	private final PesquisaService pesquisaService;

	public HomeController(DisciplinaService disciplinaService, PesquisaService pesquisaService) {
		this.disciplinaService = disciplinaService;
		this.pesquisaService = pesquisaService;
	}

	// http://localhost:8080/
	@GetMapping("/")
	public String inicio(Model model) {
		// Tudo que vai no "model" fica disponível na JSP: ${disciplinas}, ${totalMateriais}
		model.addAttribute("disciplinas", disciplinaService.listarTodas());
		model.addAttribute("totalMateriais", pesquisaService.contarMateriais());
		return "index"; // abre /WEB-INF/index.jsp
	}
}
