package Canvas;

import Vectors.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ControlsPanel extends JPanel {
    public JTextField[][] info;
    private MyPanel panel;

    public JTextField[][] getInfo() {
        return info;
    }

    public ControlsPanel(MyPanel panel, int rows) {
        super(true);
        this.panel = panel;

        setOpaque(true);

        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        info = new JTextField[rows][3];
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.ipadx = 10;
        c.ipady = 10;
        c.weightx = 0.5;
        c.weighty = 0.5;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 3; j++) {
                c.gridx = j;
                c.gridy = i;

                if (j == 0) {
                    info[i][j] = new JTextField("TEXT");
                    info[i][j].setBackground(Color.gray);
                    info[i][j].setPreferredSize(new Dimension(100,20));
                    info[i][j].setEditable(false);
                }
                else {
                    info[i][j] = new JTextField("");
                    info[i][j].setBackground(Color.white);
                    info[i][j].setForeground(Color.black);
                    info[i][j].setPreferredSize(new Dimension(70,20));
                }

                info[i][j].setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
                this.add(info[i][j], c);
            }
        }

        info[0][0].setText("World Acceleration: ");
        info[1][0].setText("Position: ");
        info[2][0].setText("Velocity: ");
        info[3][0].setText("Acceleration: ");

    }

    public void updateInfo() {
        panel.requestFocus();

        String xAcc = info[0][1].getText();
        String yAcc = info[0][2].getText();

        if (!(xAcc.equals("") && yAcc.equals(""))) {
            System.out.println(11);
            panel.setTotalAcceleration(new Vector2D(
                    Double.parseDouble(xAcc)*MyPanel.pxPerMeter,
                    Double.parseDouble(yAcc)*MyPanel.pxPerMeter)
            );
        }
    }

    private class updateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            updateInfo();
        }
    }

    public AbstractAction getUpdateAction() {
        return new updateAction();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
