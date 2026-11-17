package conectaaula.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Cores e fontes, tiradas do style.css da página original. */
final class Tema {

    static final Color PRIMARIA = new Color(0x102f70);
    static final Color TEXTO = new Color(0x101c32);
    static final Color FUNDO = new Color(0xf5f8fc);
    static final Color SUAVE = new Color(0x64748b);
    static final Color MENU = new Color(0x2b2f36);
    static final Color PRETO = new Color(0x111111);
    static final Color BOTAO_ESCURO = new Color(0x1c1f26);
    static final Color LINK = new Color(0x078bd0);
    static final Color BORDA = new Color(0xd8e2ee);
    static final Color ICONE_FUNDO = new Color(0xe1f1ff);
    static final Color BOTAO_BANNER = new Color(0xe2f1ff);
    static final Color BRANCO = Color.WHITE;

    private Tema() {
    }

    static Font fonte(int estilo, int tamanho) {
        return new Font(Font.SANS_SERIF, estilo, tamanho);
    }

    static void suavizar(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
