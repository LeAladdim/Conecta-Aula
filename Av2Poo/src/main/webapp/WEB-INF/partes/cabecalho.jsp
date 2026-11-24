<%@ page pageEncoding="UTF-8"%>
<%-- CABEÇALHO: incluído em todas as páginas. Igual ao front, só com os links apontando para o back. --%>
<header class="cabecalho">
    <div class="container cabecalho-conteudo">
        <a class="marca" href="/">
            <%-- onerror: se o logo ainda não existir em webapp/imagens/, a imagem some em vez de aparecer quebrada --%>
            <img src="/imagens/logo.png" alt="Logo Conecta Aula" onerror="this.style.display='none'">
            <strong>Conecta Aula</strong>
        </a>

        <nav class="menu">
            <a href="/#disciplinas">Disciplinas</a>          <%-- HomeController --%>
            <a href="/materiais">Materiais</a>               <%-- PesquisaController --%>
            <%-- data-compartilhar: abre o painel do js/compartilhar.js (MaterialController) --%>
            <a class="botao-compartilhar" href="#compartilhar" data-compartilhar>Compartilhar material</a>
        </nav>
    </div>
</header>
<%-- defer: o script só roda depois que a página inteira foi carregada --%>
<script src="/js/compartilhar.js" defer></script>
