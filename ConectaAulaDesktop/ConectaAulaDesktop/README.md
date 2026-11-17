# Conecta Aula (aplicação desktop em Java)

Versão desktop (Java Swing) da plataforma **Conecta Aula**, baseada na página `index.html` / `index.jsp`
do projeto original. Não usa bibliotecas externas: só o Java 17 ou superior.

## Como executar

- **Windows:** clique duas vezes em `executar.bat` (ou rode `java -jar ConectaAula.jar`).
- **Linux/macOS:** `./executar.sh` (ou `java -jar ConectaAula.jar`).

## O que a aplicação faz

- Tela inicial com banner, busca e os 6 cards de disciplinas (com a contagem de materiais de cada uma).
- **Busca** por disciplina, sigla, título, autor, tipo ou descrição (ignora acentos e maiúsculas). Pressione Enter no campo.
- **Ver materiais**: lista os materiais de uma disciplina; "Ver todos os materiais" lista tudo.
- **Compartilhar material**: formulário para cadastrar um novo material (disciplina, título, autor, tipo, link opcional e descrição).
- Os materiais ficam salvos em `~/.conecta-aula/materiais.tsv` e continuam disponíveis quando o programa é reaberto.
  Para voltar aos 12 materiais de exemplo, apague esse arquivo.

## Como gerar o JAR de novo

Com Maven: `mvn clean package` (gera `target/ConectaAula.jar`).
No Eclipse: *File > Import > Existing Maven Projects* e escolha esta pasta. Classe principal: `conectaaula.Main`.

## Estrutura

```
src/main/java/conectaaula
  Main.java                      ponto de entrada
  model/       Disciplina, Material
  repository/  MaterialRepository  (dados + gravação em arquivo + busca)
  ui/          JanelaPrincipal, PainelPrincipal (telas), TelaMateriais,
               FormularioMaterial, DialogoCompartilhar, Componentes, Tema
```
