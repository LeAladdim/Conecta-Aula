package principal.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Disciplina do curso. Os materiais são organizados por disciplina.
 *
 * @Entity = esta classe vira uma tabela "disciplina" no banco.
 */
@Entity
public class Disciplina {

	@Id // chave primária
	@GeneratedValue(strategy = GenerationType.IDENTITY) // o banco gera o id sozinho (1, 2, 3...)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nome;

	// Sigla aparece no "ícone" do card (POO, ED, BD...)
	@Column(length = 5)
	private String sigla;

	@Column(length = 300)
	private String descricao;

	// Uma disciplina tem vários materiais (1 para N).
	// mappedBy = quem guarda a ligação no banco é o atributo "disciplina" da classe Material
	@OneToMany(mappedBy = "disciplina")
	private List<Material> materiais = new ArrayList<>();

	// O JPA exige um construtor vazio
	public Disciplina() {
	}

	public Disciplina(String nome, String sigla, String descricao) {
		this.nome = nome;
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public int getQuantidadeMateriais() {
		return materiais.size();
	}

	// ---------- getters e setters ----------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Material> getMateriais() {
		return materiais;
	}
}
