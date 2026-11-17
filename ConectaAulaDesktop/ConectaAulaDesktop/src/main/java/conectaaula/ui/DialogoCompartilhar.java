package conectaaula.ui;

import conectaaula.model.Disciplina;
import conectaaula.model.Material;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/** Diálogo modal que hospeda o formulário de compartilhamento. */
class DialogoCompartilhar extends JDialog {

    private transient Material resultado;

    DialogoCompartilhar(Window dono, List<Disciplina> disciplinas, String disciplinaInicial) {
        super(dono, "Compartilhar material", ModalityType.APPLICATION_MODAL);
        FormularioMaterial formulario = new FormularioMaterial(disciplinas, disciplinaInicial,
                material -> {
                    resultado = material;
                    dispose();
                }, this::dispose);
        setContentPane(formulario);
        getRootPane().setDefaultButton((javax.swing.JButton) formulario.botaoConfirmar());
        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                formulario.focarTitulo();
            }
        });
        pack();
        setResizable(false);
        setLocationRelativeTo(dono);
    }

    /** O material informado, ou null se o usuário cancelou. */
    Material getResultado() {
        return resultado;
    }
}
