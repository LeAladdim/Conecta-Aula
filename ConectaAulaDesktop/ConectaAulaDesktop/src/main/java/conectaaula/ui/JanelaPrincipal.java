package conectaaula.ui;

import conectaaula.repository.MaterialRepository;

import javax.swing.JFrame;
import java.awt.Dimension;

/** Janela da aplicação Conecta Aula. */
public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal(MaterialRepository repositorio) {
        super("Conecta Aula");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(PainelPrincipal.iconeDaJanela());
        setContentPane(new PainelPrincipal(repositorio));
        setMinimumSize(new Dimension(1000, 640));
        setSize(1180, 800);
        setLocationRelativeTo(null);
    }
}
