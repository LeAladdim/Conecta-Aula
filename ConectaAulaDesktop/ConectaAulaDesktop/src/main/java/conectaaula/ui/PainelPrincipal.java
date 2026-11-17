package conectaaula.ui;

import conectaaula.model.Disciplina;
import conectaaula.model.Material;
import conectaaula.repository.MaterialRepository;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

/** Conteúdo da janela do Conecta Aula: cabeçalho, tela inicial (banner + disciplinas) e lista de materiais. */
class PainelPrincipal extends JPanel {

    private static final int LARGURA_MAX = 1110;
    private static final int MARGEM = 40;

    private final MaterialRepository repositorio;
    private final CardLayout cartas = new CardLayout();
    private final JPanel palco = new JPanel(cartas);
    private final JScrollPane rolagemHome = new JScrollPane();
    private final TelaMateriais telaMateriais;
    private JComponent secaoDisciplinas;
    private String disciplinaAtual;

    PainelPrincipal(MaterialRepository repositorio) {
        super(new BorderLayout());
        this.repositorio = repositorio;


        telaMateriais = new TelaMateriais(this::voltarParaInicio, this::compartilhar, this::abrirLink);

        rolagemHome.setBorder(null);
        rolagemHome.getVerticalScrollBar().setUnitIncrement(24);
        rolagemHome.getViewport().setBackground(Tema.FUNDO);

        palco.add(rolagemHome, "home");
        palco.add(telaMateriais, "materiais");

        add(criarCabecalho(), BorderLayout.NORTH);
        add(palco, BorderLayout.CENTER);

        reconstruirInicio();
        cartas.show(palco, "home");
    }

    // ---------- navegação ----------

    private void voltarParaInicio() {
        disciplinaAtual = null;
        cartas.show(palco, "home");
    }

    private void irParaDisciplinas() {
        voltarParaInicio();
        SwingUtilities.invokeLater(() -> {
            JComponent vista = (JComponent) rolagemHome.getViewport().getView();
            int maximo = Math.max(0, vista.getHeight() - rolagemHome.getViewport().getHeight());
            rolagemHome.getViewport().setViewPosition(new Point(0, Math.min(secaoDisciplinas.getY(), maximo)));
        });
    }

    private void mostrarMateriais(Disciplina disciplina) {
        disciplinaAtual = disciplina == null ? null : disciplina.nome();
        List<Material> materiais = disciplina == null
                ? repositorio.todos() : repositorio.porDisciplina(disciplina.nome());
        telaMateriais.exibir(disciplina == null ? "Todos os materiais" : disciplina.nome(),
                Componentes.plural(materiais.size()), materiais);
        cartas.show(palco, "materiais");
        palco.revalidate();
        palco.repaint();
    }

    private void buscar(String termo) {
        String texto = termo.trim();
        if (texto.isEmpty()) {
            mostrarMateriais(null);
            return;
        }
        List<Material> encontrados = repositorio.buscar(texto);
        disciplinaAtual = null;
        telaMateriais.exibir("Resultados para “" + texto + "”",
                encontrados.size() + (encontrados.size() == 1 ? " material encontrado" : " materiais encontrados"),
                encontrados);
        cartas.show(palco, "materiais");
        palco.revalidate();
        palco.repaint();
    }

    private void compartilhar() {
        DialogoCompartilhar dialogo = new DialogoCompartilhar(SwingUtilities.getWindowAncestor(this), repositorio.disciplinas(), disciplinaAtual);
        dialogo.setVisible(true);
        Material novo = dialogo.getResultado();
        dialogo.dispose();
        if (novo == null) {
            return;
        }
        boolean salvo = repositorio.adicionar(novo);
        reconstruirInicio();
        repositorio.disciplinas().stream()
                .filter(d -> d.nome().equals(novo.disciplina()))
                .findFirst()
                .ifPresent(this::mostrarMateriais);
        if (!salvo) {
            JOptionPane.showMessageDialog(this,
                    "O material foi adicionado, mas não foi possível gravá-lo em disco.\n"
                            + "Ele ficará disponível só até você fechar o programa.",
                    "Conecta Aula", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirLink(String link) {
        try {
            URI uri = new URI(link);
            String esquema = uri.getScheme();
            if (esquema == null || !(esquema.equalsIgnoreCase("http") || esquema.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException("Link inválido");
            }
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                throw new UnsupportedOperationException("Navegador indisponível");
            }
            Desktop.getDesktop().browse(uri);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível abrir o link:\n" + link,
                    "Conecta Aula", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------- cabeçalho ----------

    private JComponent criarCabecalho() {
        JButton marca = new JButton("Conecta Aula", new Componentes.LogoIcon(36));
        marca.setFont(Tema.fonte(Font.BOLD, 17));
        marca.setForeground(Tema.PRETO);
        marca.setIconTextGap(10);
        marca.setContentAreaFilled(false);
        marca.setBorderPainted(false);
        marca.setFocusPainted(false);
        marca.setBorder(new EmptyBorder(0, 0, 0, 0));
        marca.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        marca.addActionListener(e -> voltarParaInicio());

        JButton disciplinas = Componentes.botaoTexto("Disciplinas", Tema.MENU, 14);
        disciplinas.addActionListener(e -> irParaDisciplinas());
        JButton materiais = Componentes.botaoTexto("Materiais", Tema.MENU, 14);
        materiais.addActionListener(e -> mostrarMateriais(null));
        Componentes.BotaoArredondado compartilhar =
                Componentes.botaoCompartilhar(Tema.BOTAO_ESCURO, Tema.BRANCO);
        compartilhar.addActionListener(e -> compartilhar());

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        menu.setOpaque(false);
        menu.add(disciplinas);
        menu.add(materiais);
        menu.add(compartilhar);

        JPanel interno = new JPanel(new BorderLayout());
        interno.setOpaque(false);
        interno.add(marca, BorderLayout.WEST);
        interno.add(menu, BorderLayout.EAST);

        JPanel externo = new JPanel(new Componentes.CentralizadoLayout(LARGURA_MAX, MARGEM, 67, true));
        externo.setBackground(Tema.BRANCO);
        externo.setBorder(new MatteBorder(0, 0, 1, 0, Tema.BORDA));
        externo.add(interno);
        return externo;
    }

    // ---------- tela inicial ----------

    private void reconstruirInicio() {
        JPanel pilha = new JPanel();
        pilha.setLayout(new BoxLayout(pilha, BoxLayout.Y_AXIS));
        pilha.setOpaque(false);

        JComponent banner = criarBanner();
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        secaoDisciplinas = criarSecaoDisciplinas();
        secaoDisciplinas.setAlignmentX(Component.LEFT_ALIGNMENT);
        pilha.add(banner);
        pilha.add(secaoDisciplinas);

        Componentes.PainelRolavel pagina = new Componentes.PainelRolavel(new BorderLayout());
        pagina.add(pilha, BorderLayout.NORTH);
        rolagemHome.setViewportView(pagina);
    }

    private JComponent criarBanner() {
        JPanel conteudo = Componentes.pilha();
        conteudo.add(Componentes.rotulo("PLATAFORMA ACADÊMICA COLABORATIVA", Font.PLAIN, 15, new Color(0xd1dced)));
        conteudo.add(Box.createVerticalStrut(17));
        conteudo.add(Componentes.rotulo("Compartilhe conhecimento.", Font.BOLD, 44, Color.WHITE));
        conteudo.add(Componentes.rotulo("Aprenda além da sala de aula.", Font.BOLD, 44, Color.WHITE));
        conteudo.add(Box.createVerticalStrut(32));

        Componentes.CampoBusca busca = new Componentes.CampoBusca("Buscar disciplinas ou materiais...");
        Componentes.caixaFixa(busca, 427, 48);
        busca.addActionListener(e -> buscar(busca.getText()));

        Componentes.BotaoArredondado compartilhar =
                Componentes.botaoCompartilhar(Tema.BOTAO_BANNER, Tema.TEXTO);
        compartilhar.setBorder(new EmptyBorder(0, 29, 0, 29));
        Componentes.caixaFixa(compartilhar, compartilhar.getPreferredSize().width, 48);
        compartilhar.addActionListener(e -> compartilhar());

        JPanel acoes = new JPanel();
        acoes.setLayout(new BoxLayout(acoes, BoxLayout.X_AXIS));
        acoes.setOpaque(false);
        acoes.setAlignmentX(Component.LEFT_ALIGNMENT);
        acoes.add(busca);
        acoes.add(Box.createHorizontalStrut(12));
        acoes.add(compartilhar);
        conteudo.add(acoes);

        Componentes.PainelGradiente banner = new Componentes.PainelGradiente();
        banner.setLayout(new Componentes.CentralizadoLayout(LARGURA_MAX, MARGEM, 392, true));
        banner.add(conteudo);
        return banner;
    }

    private JComponent criarSecaoDisciplinas() {
        int total = repositorio.todos().size();

        JPanel textos = Componentes.pilha();
        textos.add(Componentes.rotulo("Disciplinas disponíveis", Font.BOLD, 22, Tema.PRIMARIA));
        textos.add(Box.createVerticalStrut(7));
        textos.add(Componentes.rotulo(total + (total == 1 ? " material compartilhado" : " materiais compartilhados")
                + " pela comunidade", Font.PLAIN, 15, Tema.SUAVE));

        JButton verTodos = Componentes.botaoTexto("Ver todos os materiais →", Tema.LINK, 14);
        verTodos.addActionListener(e -> mostrarMateriais(null));
        JPanel ladoDireito = new JPanel(new BorderLayout());
        ladoDireito.setOpaque(false);
        ladoDireito.add(verTodos, BorderLayout.SOUTH);

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setOpaque(false);
        titulo.add(textos, BorderLayout.WEST);
        titulo.add(ladoDireito, BorderLayout.EAST);

        JPanel grade = new JPanel(new GridLayout(0, 3, 20, 20));
        grade.setOpaque(false);
        for (Disciplina d : repositorio.disciplinas()) {
            grade.add(criarCard(d));
        }

        JPanel interno = new JPanel(new BorderLayout(0, 33));
        interno.setOpaque(false);
        interno.add(titulo, BorderLayout.NORTH);
        interno.add(grade, BorderLayout.CENTER);

        JPanel externo = new JPanel(new Componentes.CentralizadoLayout(LARGURA_MAX, MARGEM, 0, false));
        externo.setOpaque(false);
        externo.setBorder(new EmptyBorder(50, 0, 70, 0));
        externo.add(interno);
        return externo;
    }

    private JComponent criarCard(Disciplina disciplina) {
        Componentes.PainelArredondado card = new Componentes.PainelArredondado(44, Tema.BRANCO, Tema.BORDA) {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(d.width, Math.max(d.height, 238));
            }
        };
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        Componentes.PainelArredondado icone = new Componentes.PainelArredondado(32, Tema.ICONE_FUNDO, null);
        icone.setLayout(new GridBagLayout());
        JLabel sigla = new JLabel(disciplina.sigla());
        sigla.setFont(Tema.fonte(Font.BOLD, 13));
        sigla.setForeground(Tema.PRIMARIA);
        icone.add(sigla);
        Componentes.caixaFixa(icone, 44, 44);
        icone.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel topo = Componentes.pilha();
        topo.add(icone);
        topo.add(Box.createVerticalStrut(19));
        topo.add(Componentes.rotuloHtml(disciplina.nome(), 236, Font.BOLD, 16, Tema.PRETO));
        topo.add(Box.createVerticalStrut(11));
        topo.add(Componentes.rotuloHtml(disciplina.descricao(), 236, Font.PLAIN, 14, Tema.SUAVE));

        JLabel contagem = new JLabel(Componentes.plural(repositorio.contar(disciplina.nome())));
        contagem.setFont(Tema.fonte(Font.PLAIN, 12));
        contagem.setForeground(Tema.SUAVE);

        Componentes.BotaoArredondado ver =
                new Componentes.BotaoArredondado("Ver materiais →", Tema.PRIMARIA, Tema.BRANCO, 24);
        ver.setFont(Tema.fonte(Font.PLAIN, 12));
        ver.setBorder(new EmptyBorder(10, 13, 10, 13));
        ver.addActionListener(e -> mostrarMateriais(disciplina));

        JPanel rodape = new JPanel(new BorderLayout(10, 0));
        rodape.setOpaque(false);
        rodape.setBorder(new EmptyBorder(18, 0, 0, 0));
        rodape.add(contagem, BorderLayout.WEST);
        rodape.add(ver, BorderLayout.EAST);

        card.add(topo, BorderLayout.NORTH);
        card.add(rodape, BorderLayout.SOUTH);
        return card;
    }

    static Image iconeDaJanela() {
        BufferedImage imagem = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagem.createGraphics();
        new Componentes.LogoIcon(64).paintIcon(null, g, 0, 0);
        g.dispose();
        return imagem;
    }
}
