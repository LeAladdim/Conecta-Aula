/*
 * Modal/painel "Compartilhar material".
 *
 * Abre quando o usuário clica em qualquer botão com o atributo data-compartilhar
 * (cabeçalho, banner da página inicial, banner da disciplina).
 * Envia o material para a API (MaterialController -> POST /api/materiais),
 * que salva no banco. Depois recarrega a página para o material aparecer nos cards.
 *
 * Os outros botões (busca, "Ver todos", "Ver materiais") NÃO passam por aqui:
 * eles vão direto para as páginas do back-end.
 */
(() => {
  const botoesCompartilhar = document.querySelectorAll("[data-compartilhar]");
  if (botoesCompartilhar.length === 0) return;

  // Cria o painel embaixo da grade (ou no fim do conteúdo, nas páginas sem grade)
  const painel = document.createElement("div");
  painel.id = "painel-integracao";
  painel.className = "painel-compartilhar"; // o visual fica no style.css
  painel.style.display = "none";

  const grade = document.querySelector(".lista-disciplinas");
  const conteudo = document.querySelector("main .container") || document.querySelector("main");
  if (grade && grade.parentElement) {
    grade.parentElement.appendChild(painel);
  } else if (conteudo) {
    conteudo.appendChild(painel);
  }

  // Busca a lista de disciplinas no back-end (DisciplinaController -> GET /api/disciplinas)
  async function carregarDisciplinas(select, disciplinaInicial) {
    try {
      const resp = await fetch("/api/disciplinas");
      if (!resp.ok) throw new Error();
      const nomes = await resp.json();
      nomes.forEach((nome) => {
        const opcao = document.createElement("option");
        opcao.value = nome;
        opcao.textContent = nome; // textContent: não interpreta HTML (mais seguro)
        if (nome === disciplinaInicial) opcao.selected = true;
        select.appendChild(opcao);
      });
    } catch (_) {
      select.innerHTML = "<option value=''>Erro ao carregar as disciplinas</option>";
    }
  }

  function abrirPainel(disciplinaInicial) {
    painel.style.display = "block";
    // HTML fixo (sem nenhum dado digitado pelo usuário dentro do innerHTML)
    painel.innerHTML = `
      <strong>Compartilhar novo material</strong>
      <form id="form-novo-material">
        <input type="text" name="titulo" placeholder="Título do material" required minlength="3" maxlength="100">
        <select name="disciplina" required>
          <option value="">Escolha a disciplina</option>
        </select>
        <button type="submit" class="botao-compartilhar">Salvar</button>
      </form>
      <div id="msg-novo-material" class="msg-painel"></div>
    `;
    painel.scrollIntoView({ behavior: "smooth", block: "center" });

    const form = document.getElementById("form-novo-material");
    const msg = document.getElementById("msg-novo-material");
    carregarDisciplinas(form.querySelector("select"), disciplinaInicial);

    form.addEventListener("submit", async (ev) => {
      ev.preventDefault();
      const dados = Object.fromEntries(new FormData(form));
      const botao = form.querySelector("button");
      botao.disabled = true;

      try {
        const resp = await fetch("/api/materiais", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(dados)
        });

        if (resp.ok) {
          const salvo = await resp.json();
          msg.className = "msg-painel msg-ok";
          msg.textContent = `Material "${salvo.titulo}" registrado em ${salvo.disciplina}! Atualizando a página...`;
          setTimeout(() => location.reload(), 1500); // recarrega para os cards mostrarem o novo material
        } else {
          // Mostra a mensagem de erro que veio do back-end (ex: "O título precisa ter...")
          msg.className = "msg-painel msg-erro";
          msg.textContent = await resp.text();
          botao.disabled = false;
        }
      } catch (_) {
        msg.className = "msg-painel msg-erro";
        msg.textContent = "Não foi possível falar com o servidor. Tente de novo.";
        botao.disabled = false;
      }
    });
  }

  // data-disciplina (opcional) deixa a disciplina já selecionada. Ex: no banner da página da disciplina
  botoesCompartilhar.forEach((btn) => {
    btn.addEventListener("click", (e) => {
      e.preventDefault();
      abrirPainel(btn.dataset.disciplina || "");
    });
  });
})();
