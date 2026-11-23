package principal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import principal.domain.MaterialEstudo;
import principal.service.MaterialService;

/**
 * API usada pelo modal "Compartilhar material" (js/compartilhar.js).
 *
 * Antes os materiais ficavam numa ArrayList aqui dentro e sumiam ao reiniciar.
 * Agora eles são salvos no banco (pelo MaterialService), então aparecem
 * nos cards, na página da disciplina e na pesquisa.
 */
@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    // GET /api/materiais                    -> todos
    // GET /api/materiais?disciplina=BD      -> só os de Banco de Dados (aceita nome ou sigla)
    @GetMapping
    public List<MaterialEstudo> listar(@RequestParam(required = false) String disciplina) {
        return materialService.listar(disciplina);
    }

    // POST /api/materiais   com o JSON  {"titulo": "...", "disciplina": "..."}
    // Responde 201 (criado) ou 400 (dados errados) com a mensagem do erro
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody MaterialEstudo material) {
        String erro = materialService.validar(material);
        if (erro != null) {
            return ResponseEntity.badRequest().body(erro);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.salvar(material));
    }
}
