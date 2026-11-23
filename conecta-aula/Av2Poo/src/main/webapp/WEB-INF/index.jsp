<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conecta Aula</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

    <%@ include file="partes/cabecalho.jsp" %>

    <!-- CONTEÚDO  -->
    <main id="inicio">
        <section class="banner">
            <div class="container banner-conteudo">
                <p class="subtitulo">Plataforma acadêmica colaborativa</p>
                <h1>Compartilhe conhecimento. <br><mark>Aprenda além da sala de aula.</mark></h1>

                <div class="acoes-banner">
                    <form class="pesquisa" action="/materiais" method="get">
                        <input type="search" name="busca" placeholder="Buscar disciplinas ou materiais...">
                    </form>

                    <a class="botao-banner" href="#compartilhar" data-compartilhar>Compartilhar material</a>
                </div>
            </div>
        </section>

        <!-- DISCIPLINAS (vêm do banco) -->
        <section class="disciplinas" id="disciplinas">
            <div class="container">
                <div class="titulo-secao">
                    <div>
                        <h2>Disciplinas disponíveis</h2>
                        <p>${totalMateriais} ${totalMateriais == 1 ? 'material compartilhado' : 'materiais compartilhados'} pela comunidade</p>
                    </div>
                    <a href="/materiais">Ver o acervo completo</a>
                </div>

                <div class="lista-disciplinas">
                    <c:forEach items="${disciplinas}" var="d">
                        <article class="card-disciplina">
                            <div class="icone-disciplina">${fn:escapeXml(d.sigla)}</div>
                            <h3>${fn:escapeXml(d.nome)}</h3>
                            <p>${fn:escapeXml(d.descricao)}</p>
                            <div class="rodape-card">
                                <span>${d.quantidadeMateriais} ${d.quantidadeMateriais == 1 ? 'material' : 'materiais'}</span>
                                <a href="/disciplinas/${d.id}">Ver materiais</a>
                            </div>
                        </article>
                    </c:forEach>
                </div>
            </div>
        </section>

    </main>

</body>
</html>
