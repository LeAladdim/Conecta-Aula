<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%--
    Card de um material. Usar dentro de um <c:forEach ... var="m">.
    fn:escapeXml protege a página caso alguém digite HTML no título/descrição.
--%>
<article class="card-material">
    <div class="topo-card-material">
        <span class="etiqueta">${m.tipo.descricao}</span>
        <span>${m.dataFormatada}</span>
    </div>

    <h3>${fn:escapeXml(m.titulo)}</h3>
    <p class="origem">
        <a href="/disciplinas/${m.disciplina.id}">${fn:escapeXml(m.disciplina.nome)}</a>
    </p>
    <p>${fn:escapeXml(m.descricao)}</p>

    <%-- INTEGRAÇÃO: quando existir a página de detalhes do material (parte "Compartilhar material"),
         dá para colocar aqui um botão:  <a href="/materiais/${m.id}">Ver material →</a> --%>
</article>
