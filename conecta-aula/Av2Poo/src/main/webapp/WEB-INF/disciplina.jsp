<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${fn:escapeXml(disciplina.nome)} - Conecta Aula</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

    <%@ include file="partes/cabecalho.jsp" %>

    <main>
        <section class="banner banner-pequeno">
            <div class="container banner-conteudo">
                <p class="subtitulo">Disciplina · <span class="sigla">${fn:escapeXml(disciplina.sigla)}</span></p>
                <h1>${fn:escapeXml(disciplina.nome)}</h1>
                <p class="descricao-banner">${fn:escapeXml(disciplina.descricao)}</p>

                <div class="acoes-banner">
                    <%-- data-disciplina: o painel já abre com esta disciplina selecionada --%>
                    <a class="botao-banner" href="#compartilhar" data-compartilhar
                       data-disciplina="${fn:escapeXml(disciplina.nome)}">Compartilhar material nesta disciplina</a>
                </div>
            </div>
        </section>

        <section class="disciplinas">
            <div class="container">
                <div class="titulo-secao">
                    <div>
                        <h2>Materiais</h2>
                        <p>${fn:length(materiais)} ${fn:length(materiais) == 1 ? 'material' : 'materiais'} · do mais recente para o mais antigo</p>
                    </div>
                    <a href="/#disciplinas">Todas as disciplinas</a>
                </div>

                <c:choose>
                    <c:when test="${empty materiais}">
                        <div class="vazio">
                            <p>Ainda não tem nenhum material nesta disciplina.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="lista-materiais">
                            <c:forEach items="${materiais}" var="m">
                                <%@ include file="partes/card-material.jsp" %>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>

</body>
</html>
