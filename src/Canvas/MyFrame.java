package Canvas;

import javax.swing.*;

public class MyFrame extends JFrame {
    private final MyPanel panel;

    public MyFrame(String title, int width, int height) {
        super(title);

        // Setting Box-Layout for the ContentPane
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        panel = new MyPanel(width, height);
        add(panel);
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

}
