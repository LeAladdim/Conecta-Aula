package conectaaula.ui;

import conectaaula.model.Material;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;
import java.util.function.Consumer;

/** Tela com a lista de materiais (de uma disciplina, de todas ou de uma busca). */
class TelaMateriais extends JPanel {

    private static final int LARGURA_TEXTO = 620;

    private final JLabel titulo = Componentes.rotulo("", Font.BOLD, 22, Tema.PRIMARIA);
    private final JLabel subtitulo = Componentes.rotulo("", Font.PLAIN, 15, Tema.SUAVE);
    private final JPanel lista = Componentes.pilha();
    private final JScrollPane rolagem;
    private final Runnable aoCompartilhar;
    private final Consumer<String> aoAbrirLink;

    TelaMateriais(Runnable aoVoltar, Runnable aoCompartilhar, Consumer<String> aoAbrirLink) {
        super(new BorderLayout());
        this.aoCompartilhar = aoCompartilhar;
        this.aoAbrirLink = aoAbrirLink;
        setBackground(Tema.FUNDO);

        JButton voltar = Componentes.botaoTexto("← Voltar", Tema.LINK, 14);
        voltar.addActionListener(e -> aoVoltar.run());
        JPanel linhaVoltar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        linhaVoltar.setOpaque(false);
        linhaVoltar.add(voltar);

        JPanel textos = Componentes.pilha();
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(6));
        textos.add(subtitulo);

        JPanel topo = new JPanel(new BorderLayout(0, 10));
        topo.setOpaque(false);
        topo.add(linhaVoltar, BorderLayout.NORTH);
        topo.add(textos, BorderLayout.CENTER);

        JPanel topoExterno = new JPanel(new Componentes.CentralizadoLayout(1110, 40, 0, false));
        topoExterno.setBackground(Tema.FUNDO);
        topoExterno.setBorder(new EmptyBorder(26, 0, 10, 0));
        topoExterno.add(topo);

        Componentes.PainelRolavel pagina =
                new Componentes.PainelRolavel(new Componentes.CentralizadoLayout(1110, 40, 0, false));
        pagina.setBorder(new EmptyBorder(8, 0, 40, 0));
        pagina.add(lista);

        rolagem = new JScrollPane(pagina);
        rolagem.setBorder(BorderFactory.createEmptyBorder());
        rolagem.getVerticalScrollBar().setUnitIncrement(24);
        rolagem.getViewport().setBackground(Tema.FUNDO);

        add(topoExterno, BorderLayout.NORTH);
        add(rolagem, BorderLayout.CENTER);
    }

    void exibir(String tituloTela, String textoSubtitulo, List<Material> materiais) {
        titulo.setText(tituloTela);
        subtitulo.setText(textoSubtitulo);
        lista.removeAll();
        if (materiais.isEmpty()) {
            lista.add(criarVazio());
        } else {
            for (Material m : materiais) {
                lista.add(criarLinha(m));
                lista.add(Box.createVerticalStrut(14));
            }
        }
        revalidate();
        repaint();
        SwingUtilities.invokeLater(() -> rolagem.getVerticalScrollBar().setValue(0));
    }

    private JComponent criarVazio() {
        Componentes.PainelArredondado p = new Componentes.PainelArredondado(36, Tema.BRANCO, Tema.BORDA);
        p.setLayout(new BorderLayout(0, 16));
        p.setBorder(new EmptyBorder(32, 28, 32, 28));
        JPanel conteudo = Componentes.pilha();
        conteudo.add(Componentes.rotulo("Nenhum material encontrado.", Font.BOLD, 16, Tema.TEXTO));
        conteudo.add(Box.createVerticalStrut(6));
        conteudo.add(Componentes.rotulo("Que tal ser a primeira pessoa a compartilhar algo por aqui?",
                Font.PLAIN, 14, Tema.SUAVE));
        conteudo.add(Box.createVerticalStrut(16));
        Componentes.BotaoArredondado botao =
                Componentes.botaoCompartilhar(Tema.BOTAO_ESCURO, Tema.BRANCO);
        botao.setAlignmentX(Component.LEFT_ALIGNMENT);
        botao.addActionListener(e -> aoCompartilhar.run());
        conteudo.add(botao);
        p.add(conteudo, BorderLayout.CENTER);
        return limitarAltura(p);
    }

    private JComponent criarLinha(Material m) {
        Componentes.PainelArredondado linha = new Componentes.PainelArredondado(36, Tema.BRANCO, Tema.BORDA);
        linha.setLayout(new BorderLayout(18, 0));
        linha.setBorder(new EmptyBorder(20, 22, 20, 22));

        Componentes.PainelArredondado icone = new Componentes.PainelArredondado(32, Tema.ICONE_FUNDO, null);
        icone.setLayout(new GridBagLayout());
        JLabel sigla = new JLabel(siglaDoTipo(m.tipo()));
        sigla.setFont(Tema.fonte(Font.BOLD, 12));
        sigla.setForeground(Tema.PRIMARIA);
        icone.add(sigla);
        Componentes.caixaFixa(icone, 44, 44);
        JPanel iconeTopo = new JPanel(new BorderLayout());
        iconeTopo.setOpaque(false);
        iconeTopo.add(icone, BorderLayout.NORTH);

        JPanel textos = Componentes.pilha();
        textos.add(Componentes.rotuloHtml(m.titulo(), LARGURA_TEXTO, Font.BOLD, 16, Tema.TEXTO));
        textos.add(Box.createVerticalStrut(4));
        textos.add(Componentes.rotuloHtml(m.disciplina() + "  ·  " + m.tipo() + "  ·  por " + m.autor(),
                LARGURA_TEXTO, Font.PLAIN, 13, Tema.LINK));
        if (!m.descricao().isBlank()) {
            textos.add(Box.createVerticalStrut(8));
            textos.add(Componentes.rotuloHtml(m.descricao(), LARGURA_TEXTO, Font.PLAIN, 14, Tema.SUAVE));
        }

        linha.add(iconeTopo, BorderLayout.WEST);
        linha.add(textos, BorderLayout.CENTER);

        if (!m.link().isBlank()) {
            Componentes.BotaoArredondado abrir =
                    new Componentes.BotaoArredondado("Abrir link", Tema.PRIMARIA, Tema.BRANCO, 24);
            abrir.addActionListener(e -> aoAbrirLink.accept(m.link()));
            JPanel lado = new JPanel(new GridBagLayout());
            lado.setOpaque(false);
            lado.add(abrir);
            linha.add(lado, BorderLayout.EAST);
        }
        return limitarAltura(linha);
    }

    private static JComponent limitarAltura(JComponent c) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return c;
    }

    private static String siglaDoTipo(String tipo) {
        return switch (tipo) {
            case "Resumo" -> "RES";
            case "Slides" -> "PPT";
            case "Lista de exercícios" -> "LST";
            case "PDF" -> "PDF";
            case "Vídeo" -> "VÍD";
            case "Link" -> "URL";
            default -> tipo.length() <= 3 ? tipo.toUpperCase() : tipo.substring(0, 3).toUpperCase();
        };
    }
}
