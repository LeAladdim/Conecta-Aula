# Conecta-Aula
Projeto de aplicação Web de um sistema para reunir conteúdos diversos de matérias iguais, porém com professores, metodologias e materiais distintos

Link do Protótipo para Avaliação Preliminar:
https://pixel-perfect-snapshot-1127.lovable.app/

Turma: ADS-MAT-PIT-4S-T1

Integrantes: Antônio Pedro Cerqueira da Cruz, Danilo Santos Almeida, João Leonardo Telles Souza, Micaela da Silva de Castro, Rodrigo Costa Brasil e Yago de Almeida Araújo.

Fluxo Principal

Acesso e Disciplinas: Ao abrir a página inicial (http://localhost:8080), o sistema carrega automaticamente 6 disciplinas e 12 materiais de demonstração.

Consulta e Pesquisa: O estudante seleciona uma disciplina para listar seus conteúdos ou utiliza a busca textual (insensível a acentos e maiúsculas) na página de materiais (/materiais).

Compartilhamento: Pelo botão Compartilhar material, o usuário informa um título e a disciplina correspondente; o back-end (Spring Boot + JPA) valida e salva o item no banco H2, exibindo-o imediatamente no acervo.

Como Executar

Pré-requisitos: JDK 17+ e Apache Maven.

cd Av2Poo
mvn spring-boot:run


Aplicação: http://localhost:8080

Console H2 (apoio): http://localhost:8080/h2-console (JDBC URL: jdbc:h2:file:./dados/conecta-aula | User: sa | Senha: vazia).


(Você também pode iniciar a aplicação completa executando a classe principal .MainAplication (em Av2Poo/src/main/java/principal/MainAplication.java) pela sua IDE e acessando http://localhost:8080/ no navegador)   
