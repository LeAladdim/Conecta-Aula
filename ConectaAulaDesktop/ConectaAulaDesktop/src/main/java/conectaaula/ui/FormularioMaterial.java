package conectaaula.ui;

import conectaaula.model.Disciplina;
import conectaaula.model.Material;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Consumer;

/** Formulário "Compartilhar material" (o conteúdo do diálogo). */
class FormularioMaterial extends JPanel {

    static final List<String> TIPOS = List.of("Resumo", "Slides", "Lista de exercícios", "PDF", "Vídeo", "Link");

    private final JComboBox<String> comboDisciplina;
    private final JComboBox<String> comboTipo = new JComboBox<>(TIPOS.toArray(new String[0]));
    private final JTextField campoTitulo = new JTextField(30);
    private final JTextField campoAutor = new JTextField(30);
    private final JTextField campoLink = new JTextField(30);
    private final JTextArea campoDescricao = new JTextArea(4, 30);
    private final Componentes.BotaoArredondado botaoConfirmar =
            new Componentes.BotaoArredondado("Compartilhar", Tema.PRIMARIA, Tema.BRANCO, 24);
    private final Consumer<Material> aoConfirmar;

    FormularioMaterial(List<Disciplina> disciplinas, String disciplinaInicial,
                       Consumer<Material> aoConfirmar, Runnable aoCancelar) {
        super(new BorderLayout(0, 14));
        this.aoConfirmar = aoConfirmar;
        setBackground(Tema.BRANCO);
        setBorder(new EmptyBorder(22, 26, 20, 26));

        comboDisciplina = new JComboBox<>(disciplinas.stream().map(Disciplina::nome).toArray(String[]::new));
        if (disciplinaInicial != null) {
            comboDisciplina.setSelectedItem(disciplinaInicial);
        }
        campoDescricao.setLineWrap(true);
        campoDescricao.setWrapStyleWord(true);

        JPanel cabecalho = Componentes.pilha();
        cabecalho.add(Componentes.rotulo("Compartilhar material", Font.BOLD, 20, Tema.PRIMARIA));
        cabecalho.add(Box.createVerticalStrut(6));
        cabecalho.add(Componentes.rotuloHtml(
                "Contribua com resumos, slides, listas de exercícios ou links úteis para os colegas.",
                440, Font.PLAIN, 13, Tema.SUAVE));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setOpaque(false);
        adicionarLinha(formulario, 0, "Disciplina", comboDisciplina);
        adicionarLinha(formulario, 1, "Título *", campoTitulo);
        adicionarLinha(formulario, 2, "Seu nome *", campoAutor);
        adicionarLinha(formulario, 3, "Tipo", comboTipo);
        adicionarLinha(formulario, 4, "Link (opcional)", campoLink);
        adicionarLinha(formulario, 5, "Descrição", new JScrollPane(campoDescricao));

        Componentes.BotaoArredondado cancelar =
                new Componentes.BotaoArredondado("Cancelar", Tema.ICONE_FUNDO, Tema.PRIMARIA, 24);
        cancelar.addActionListener(e -> aoCancelar.run());
        botaoConfirmar.addActionListener(e -> confirmar());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);
        rodape.add(cancelar);
        rodape.add(botaoConfirmar);

        add(cabecalho, BorderLayout.NORTH);
        add(formulario, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
    }

    JComponent botaoConfirmar() {
        return botaoConfirmar;
    }

    void focarTitulo() {
        campoTitulo.requestFocusInWindow();
    }

    private void confirmar() {
        String titulo = campoTitulo.getText().trim();
        String autor = campoAutor.getText().trim();
        String link = campoLink.getText().trim();
        String descricao = campoDescricao.getText().trim();

        if (titulo.isEmpty()) {
            avisar("Informe o título do material.", campoTitulo);
            return;
        }
        if (titulo.length() > 120) {
            avisar("O título pode ter no máximo 120 caracteres.", campoTitulo);
            return;
        }
        if (autor.isEmpty()) {
            avisar("Informe o seu nome.", campoAutor);
            return;
        }
        if (!link.isEmpty()) {
            if (!link.matches("(?i)^[a-z][a-z0-9+.-]*://.*")) {
                link = "https://" + link;
            }
            if (!linkValido(link)) {
                avisar("O link informado não parece válido. Use um endereço http:// ou https://.", campoLink);
                return;
            }
        }
        aoConfirmar.accept(new Material(titulo, autor, (String) comboTipo.getSelectedItem(), link, descricao,
                (String) comboDisciplina.getSelectedItem()));
    }

    private static boolean linkValido(String link) {
        try {
            URI uri = new URI(link);
            String esquema = uri.getScheme();
            return esquema != null && (esquema.equalsIgnoreCase("http") || esquema.equalsIgnoreCase("https"))
                    && uri.getHost() != null;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private void avisar(String mensagem, JComponent campo) {
        JOptionPane.showMessageDialog(this, mensagem, "Verifique os dados", JOptionPane.WARNING_MESSAGE);
        campo.requestFocusInWindow();
    }

    private static void adicionarLinha(JPanel painel, int linha, String texto, JComponent campo) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(Tema.fonte(Font.PLAIN, 13));
        rotulo.setForeground(Tema.TEXTO);

        GridBagConstraints r = new GridBagConstraints();
        r.gridx = 0;
        r.gridy = linha;
        r.anchor = GridBagConstraints.NORTHWEST;
        r.insets = new Insets(8, 0, 6, 14);
        painel.add(rotulo, r);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = linha;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 0, 4, 0);
        painel.add(campo, c);
    }
}
