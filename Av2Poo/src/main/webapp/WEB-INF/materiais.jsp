<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiais - Conecta Aula</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

    <%@ include file="partes/cabecalho.jsp" %>

    <main>
        <section class="disciplinas">
            <div class="container">

                <%-- PESQUISA: por nome e/ou por disciplina --%>
                <form class="filtros" action="/materiais" method="get">
                    <input type="search" name="busca" value="${fn:escapeXml(busca)}"
                           placeholder="Título ou assunto...">
                    <select name="disciplina">
                        <option value="">Todas as disciplinas</option>
                        <c:forEach items="${disciplinas}" var="d">
                            <option value="${d.id}" ${d.id == disciplinaSelecionada ? 'selected' : ''}>${fn:escapeXml(d.nome)}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="botao-compartilhar">Pesquisar</button>
                </form>

                <div class="titulo-secao">
                    <div>
                        <c:choose>
                            <c:when test="${not empty busca or not empty disciplinaSelecionada}">
                                <h2>Resultado da pesquisa</h2>
                            </c:when>
                            <c:otherwise>
                                <h2>Todos os materiais</h2>
                            </c:otherwise>
                        </c:choose>
                        <p>${fn:length(materiais)} ${fn:length(materiais) == 1 ? 'material encontrado' : 'materiais encontrados'}</p>
                    </div>
                    <c:if test="${not empty busca or not empty disciplinaSelecionada}">
                        <a href="/materiais">Limpar pesquisa</a>
                    </c:if>
                </div>

                <%-- Disciplinas cujo nome bate com a busca --%>
                <c:if test="${not empty disciplinasEncontradas}">
                    <div class="disciplinas-encontradas">
                        <span>Disciplinas:</span>
                        <c:forEach items="${disciplinasEncontradas}" var="d">
                            <a href="/disciplinas/${d.id}">${fn:escapeXml(d.nome)}</a>
                        </c:forEach>
                    </div>
                </c:if>

                <c:choose>
                    <c:when test="${empty materiais}">
                        <div class="vazio">
                            <p>Nenhum material encontrado.</p>
                            <a class="botao-compartilhar" href="#compartilhar" data-compartilhar>Compartilhar um material</a>
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
