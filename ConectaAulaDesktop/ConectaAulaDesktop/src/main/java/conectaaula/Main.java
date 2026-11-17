package conectaaula;

import conectaaula.repository.MaterialRepository;
import conectaaula.ui.JanelaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Ponto de entrada da aplicação desktop Conecta Aula. */
public class Main {

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFonts", "on");
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
                // usa o visual padrão do Swing
            }
            new JanelaPrincipal(new MaterialRepository()).setVisible(true);
        });
    }
}
