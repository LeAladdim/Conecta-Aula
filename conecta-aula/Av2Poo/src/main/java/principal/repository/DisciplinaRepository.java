package principal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import principal.domain.Disciplina;

/**
 * Acesso à tabela "disciplina".
 * Só de estender JpaRepository já ganhamos: findAll(), findById(), save(), delete(), count()...
 */
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

	// SELECT * FROM disciplina ORDER BY nome
	List<Disciplina> findAllByOrderByNomeAsc();
}
