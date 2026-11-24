<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%--
    Página de erro. É usada de dois jeitos:
    1) pelos nossos controllers, passando "mensagem";
    2) pelo próprio Spring quando dá 404/500 (ele manda "status" e "error").
--%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ops! - Conecta Aula</title>
    <link rel="stylesheet" href="/style.css">
</head>
<body>

    <%@ include file="partes/cabecalho.jsp" %>

    <main>
        <section class="disciplinas">
            <div class="container">
                <div class="vazio">
                    <c:choose>
                        <c:when test="${not empty mensagem}">
                            <h2>${fn:escapeXml(mensagem)}</h2>
                        </c:when>
                        <c:when test="${status == 404}">
                            <h2>Página não encontrada (404)</h2>
                        </c:when>
                        <c:otherwise>
                            <h2>Ops! Algo deu errado.</h2>
                            <c:if test="${not empty status}"><p>Erro ${status}</p></c:if>
                        </c:otherwise>
                    </c:choose>
                    <a class="botao-compartilhar" href="/">Voltar para o início</a>
                </div>
            </div>
        </section>
    </main>

</body>
</html>
