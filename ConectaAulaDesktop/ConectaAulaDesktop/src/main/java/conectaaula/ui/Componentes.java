package conectaaula.ui;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/** Componentes visuais reutilizados nas telas. */
final class Componentes {

    private Componentes() {
    }

    // ---------- textos ----------

    static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\n", "<br>");
    }

    static String plural(int n) {
        return n + (n == 1 ? " material" : " materiais");
    }

    static JLabel rotulo(String texto, int estilo, int tamanho, Color cor) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.fonte(estilo, tamanho));
        l.setForeground(cor);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    /** Rótulo que quebra o texto em linhas dentro da largura dada. */
    static JLabel rotuloHtml(String texto, int largura, int estilo, int tamanho, Color cor) {
        JLabel l = new JLabel("<html><div style='width:" + largura + "px'>" + esc(texto) + "</div></html>");
        l.setFont(Tema.fonte(estilo, tamanho));
        l.setForeground(cor);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    static JPanel pilha() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        return p;
    }

    static JButton botaoTexto(String texto, Color cor, int tamanho) {
        JButton b = new JButton(texto);
        b.setFont(Tema.fonte(java.awt.Font.PLAIN, tamanho));
        b.setForeground(cor);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setBorder(new EmptyBorder(4, 2, 4, 2));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    static Color escurecer(Color c, float fator) {
        return new Color(Math.round(c.getRed() * fator), Math.round(c.getGreen() * fator),
                Math.round(c.getBlue() * fator));
    }

    // ---------- painéis e botões ----------

    /** Painel com fundo branco (ou outra cor), cantos arredondados e borda opcional. */
    static class PainelArredondado extends JPanel {
        private final int arco;
        private final Color borda;

        PainelArredondado(int arco, Color fundo, Color borda) {
            this.arco = arco;
            this.borda = borda;
            setBackground(fundo);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Tema.suavizar(g2);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arco, arco);
            if (borda != null) {
                g2.setColor(borda);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arco, arco);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Fundo em degradê azul do banner. */
    static class PainelGradiente extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            if (w <= 0 || h <= 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(w, h * 0.6f),
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{new Color(0x09285f), new Color(0x075a99), new Color(0x078ac4)}));
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    static class BotaoArredondado extends JButton {
        private final Color fundo;
        private final Color fundoHover;
        private final int arco;

        BotaoArredondado(String texto, Color fundo, Color corTexto, int arco) {
            super(texto);
            this.fundo = fundo;
            this.fundoHover = escurecer(fundo, 0.92f);
            this.arco = arco;
            setForeground(corTexto);
            setFont(Tema.fonte(java.awt.Font.PLAIN, 14));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(11, 18, 11, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Tema.suavizar(g2);
            g2.setColor(getModel().isRollover() ? fundoHover : fundo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arco, arco);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Campo de busca branco e arredondado, com texto de dica. */
    static class CampoBusca extends JTextField {
        private final String dica;

        CampoBusca(String dica) {
            this.dica = dica;
            setOpaque(false);
            setBorder(new EmptyBorder(0, 18, 0, 18));
            setFont(Tema.fonte(java.awt.Font.PLAIN, 14));
            setForeground(Tema.TEXTO);
            setCaretColor(Tema.TEXTO);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Tema.suavizar(g2);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
            g2.dispose();
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g3 = (Graphics2D) g.create();
                Tema.suavizar(g3);
                g3.setColor(Tema.SUAVE);
                g3.setFont(getFont());
                FontMetrics fm = g3.getFontMetrics();
                g3.drawString(dica, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g3.dispose();
            }
        }
    }

    /** Painel dentro de JScrollPane que acompanha a largura da janela. */
    static class PainelRolavel extends JPanel implements Scrollable {
        PainelRolavel(LayoutManager layout) {
            super(layout);
            setBackground(Tema.FUNDO);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visivel, int orientacao, int direcao) {
            return 24;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visivel, int orientacao, int direcao) {
            return orientacao == SwingConstants.VERTICAL ? Math.max(visivel.height - 40, 40) : visivel.width;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    /**
     * Centraliza o único filho com largura máxima (equivale ao ".container" do CSS).
     * A altura do filho é sempre a preferida.
     */
    static class CentralizadoLayout implements LayoutManager {
        private final int larguraMax;
        private final int margem;
        private final int alturaMin;
        private final boolean centroVertical;

        CentralizadoLayout(int larguraMax, int margem, int alturaMin, boolean centroVertical) {
            this.larguraMax = larguraMax;
            this.margem = margem;
            this.alturaMin = alturaMin;
            this.centroVertical = centroVertical;
        }

        private static Component filho(Container p) {
            return p.getComponentCount() > 0 ? p.getComponent(0) : null;
        }

        @Override
        public void addLayoutComponent(String nome, Component c) {
        }

        @Override
        public void removeLayoutComponent(Component c) {
        }

        @Override
        public Dimension preferredLayoutSize(Container p) {
            Insets in = p.getInsets();
            Component c = filho(p);
            Dimension d = c == null ? new Dimension() : c.getPreferredSize();
            return new Dimension(Math.min(d.width, larguraMax) + 2 * margem + in.left + in.right,
                    Math.max(alturaMin, d.height + in.top + in.bottom));
        }

        @Override
        public Dimension minimumLayoutSize(Container p) {
            return new Dimension(0, preferredLayoutSize(p).height);
        }

        @Override
        public void layoutContainer(Container p) {
            Component c = filho(p);
            if (c == null) {
                return;
            }
            Insets in = p.getInsets();
            int disponivel = p.getWidth() - in.left - in.right;
            int w = Math.max(0, Math.min(disponivel - 2 * margem, larguraMax));
            int h = c.getPreferredSize().height;
            int x = in.left + (disponivel - w) / 2;
            int y = in.top;
            if (centroVertical) {
                y += Math.max(0, (p.getHeight() - in.top - in.bottom - h) / 2);
            }
            c.setBounds(x, y, w, h);
        }
    }

    /** Logo desenhado em código: círculo escuro com um chapéu de formando (capelo) branco. */
    static class LogoIcon implements Icon {
        private final int tam;

        LogoIcon(int tam) {
            this.tam = tam;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            Tema.suavizar(g2);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(Tema.BOTAO_ESCURO);
            g2.fillOval(x, y, tam, tam);

            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(Math.max(1.4f, tam / 20f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            float t = tam;
            float cx = x + t / 2f;
            float cy = y + t / 2f;

            // tampo do capelo (losango)
            Path2D topo = new Path2D.Float();
            topo.moveTo(cx, cy - 0.22f * t);
            topo.lineTo(cx + 0.32f * t, cy - 0.06f * t);
            topo.lineTo(cx, cy + 0.10f * t);
            topo.lineTo(cx - 0.32f * t, cy - 0.06f * t);
            topo.closePath();
            g2.draw(topo);

            // parte que veste a cabeça
            Path2D base = new Path2D.Float();
            base.moveTo(cx - 0.18f * t, cy + 0.02f * t);
            base.lineTo(cx - 0.18f * t, cy + 0.15f * t);
            base.quadTo(cx, cy + 0.27f * t, cx + 0.18f * t, cy + 0.15f * t);
            base.lineTo(cx + 0.18f * t, cy + 0.02f * t);
            g2.draw(base);

            // pingente
            g2.draw(new Line2D.Float(cx + 0.32f * t, cy - 0.06f * t, cx + 0.32f * t, cy + 0.14f * t));
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tam;
        }

        @Override
        public int getIconHeight() {
            return tam;
        }
    }

    /** Ícone de envio (seta para cima sobre uma bandeja), usado nos botões "Compartilhar material". */
    static class UploadIcon implements Icon {
        private final int tam;
        private final Color cor;

        UploadIcon(int tam, Color cor) {
            this.tam = tam;
            this.cor = cor;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            Tema.suavizar(g2);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(cor);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            float s = tam;

            Path2D bandeja = new Path2D.Float();
            bandeja.moveTo(x + 0.12f * s, y + 0.62f * s);
            bandeja.lineTo(x + 0.12f * s, y + 0.88f * s);
            bandeja.lineTo(x + 0.88f * s, y + 0.88f * s);
            bandeja.lineTo(x + 0.88f * s, y + 0.62f * s);
            g2.draw(bandeja);

            g2.draw(new Line2D.Float(x + 0.5f * s, y + 0.72f * s, x + 0.5f * s, y + 0.12f * s));
            Path2D ponta = new Path2D.Float();
            ponta.moveTo(x + 0.26f * s, y + 0.36f * s);
            ponta.lineTo(x + 0.5f * s, y + 0.12f * s);
            ponta.lineTo(x + 0.74f * s, y + 0.36f * s);
            g2.draw(ponta);
            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tam;
        }

        @Override
        public int getIconHeight() {
            return tam;
        }
    }

    /** Botão "Compartilhar material" com o ícone de envio. */
    static BotaoArredondado botaoCompartilhar(Color fundo, Color corTexto) {
        BotaoArredondado b = new BotaoArredondado("Compartilhar material", fundo, corTexto, 24);
        b.setIcon(new UploadIcon(16, corTexto));
        b.setIconTextGap(8);
        return b;
    }

    static JComponent caixaFixa(JComponent c, int largura, int altura) {
        Dimension d = new Dimension(largura, altura);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
        c.setMaximumSize(d);
        return c;
    }
}
