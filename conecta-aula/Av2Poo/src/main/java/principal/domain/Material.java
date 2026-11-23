package principal.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Material salvo no banco (@Entity). É ele que aparece nos cards, na página da disciplina e na pesquisa.
 *
 * O modal "Compartilhar material" manda um MaterialEstudo (só título e disciplina);
 * o MaterialService converte em Material e salva aqui.
 *
 * Para guardar mais coisas no futuro (professor, semestre, turma, arquivo...),
 * acrescente os campos NESTA classe. Não mude o nome dos campos que já existem: as páginas usam eles.
 */
@Entity
public class Material {

	private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String titulo;

	@Column(nullable = false, length = 500)
	private String descricao;

	// Salva o nome do enum no banco ("SLIDES"), e não o número da posição
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoMaterial tipo;

	// Vários materiais pertencem a uma disciplina (N para 1).
	// No banco vira a coluna "disciplina_id" (chave estrangeira).
	@ManyToOne(optional = false)
	private Disciplina disciplina;

	@Column(nullable = false)
	private LocalDateTime dataPublicacao;

	public Material() {
	}

	public Material(String titulo, String descricao, TipoMaterial tipo, Disciplina disciplina) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.tipo = tipo;
		this.disciplina = disciplina;
		this.dataPublicacao = LocalDateTime.now();
	}

	// A JSP não formata LocalDateTime sozinha, então deixamos a data pronta aqui
	public String getDataFormatada() {
		return dataPublicacao == null ? "" : dataPublicacao.format(FORMATO_DATA);
	}

	// ---------- getters e setters ----------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TipoMaterial getTipo() {
		return tipo;
	}

	public void setTipo(TipoMaterial tipo) {
		this.tipo = tipo;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public LocalDateTime getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(LocalDateTime dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}
}
