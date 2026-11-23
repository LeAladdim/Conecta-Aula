package principal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.domain.Disciplina;
import principal.domain.Material;

/**
 * Acesso à tabela "material".
 * O Spring Data JPA cria o SQL sozinho a partir do NOME do método.
 * Ex: findByDisciplinaOrderByDataPublicacaoDesc
 *   = SELECT * FROM material WHERE disciplina_id = ? ORDER BY data_publicacao DESC
 */
public interface MaterialRepository extends JpaRepository<Material, Long> {

	List<Material> findAllByOrderByDataPublicacaoDesc();

	List<Material> findByDisciplinaOrderByDataPublicacaoDesc(Disciplina disciplina);
}
