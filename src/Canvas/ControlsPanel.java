package Canvas;

import MovingPoint.MovingPoint;
import Vectors.Vector2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import Main.Number;

public class ControlsPanel extends JPanel {
    public final JTextField[][] info;
    public final JTextField[] mass;
    private final MyPanel panel;

    public JTextField[][] getInfo() {
        return info;
    }

    public ControlsPanel(MyPanel panel, int rows) {
        super(true);
        this.panel = panel;

        // Update info labels
        this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "update");
        this.getActionMap().put("update", new updateAction());

        setOpaque(true);

        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        info = new MyJTextField[rows][3];
        mass = new MyJTextField[2];


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
                    info[i][j] = new MyJTextField(this, "TEXT");
                    info[i][j].setBackground(new Color(120,5,5));
                    info[i][j].setForeground(Color.white);
                    info[i][j].setPreferredSize(new Dimension(130,20));
                    info[i][j].setEditable(false);
                }
                else {
                    info[i][j] = new MyJTextField(this, "");
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

        c.gridx = 0;
        c.gridy = 4;
        mass[0] = new MyJTextField(this, "Mass: ");
        mass[0].setEditable(false);
        mass[0].setBackground(new Color(120,5,5));
        mass[0].setForeground(Color.white);
        mass[0].setPreferredSize(new Dimension(130,20));
        mass[0].setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        this.add(mass[0], c);


        c.gridx = 1;
        c.gridwidth = 2;
        mass[1] = new MyJTextField(this, "");
        mass[1].setBackground(Color.white);
        mass[1].setForeground(Color.black);
        mass[1].setPreferredSize(new Dimension(150,20));
        mass[1].setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        mass[1].setHorizontalAlignment(JTextField.CENTER);
        this.add(mass[1], c);
    }

    public void infoBlank() {
        info[1][1].setText("");
        info[1][2].setText("");
        info[2][1].setText("");
        info[2][2].setText("");
        info[3][1].setText("");
        info[3][2].setText("");
        mass[1].setText("");
    }

    public void initInfo(MovingPoint mp) {
        double x, y;

        info[0][1].setText(String.format("%.2f", panel.getTotalAcceleration().getXMag()/MyPanel.pxPerMeter));
        info[0][2].setText(String.format("%.2f", panel.getTotalAcceleration().getYMag()/MyPanel.pxPerMeter));

        x = mp.getPosition().getXMag()/MyPanel.pxPerMeter;
        y = mp.getPosition().getYMag()/MyPanel.pxPerMeter;
        info[1][1].setText(String.format("%.2f", x));
        info[1][2].setText(String.format("%.2f", y));

        x = mp.getVelocity().getXMag()/MyPanel.pxPerMeter;
        y = mp.getVelocity().getYMag()/MyPanel.pxPerMeter;
        info[2][1].setText(String.format("%.2f", x));
        info[2][2].setText(String.format("%.2f", y));

        x = mp.getAcceleration().getXMag()/MyPanel.pxPerMeter;
        y = mp.getAcceleration().getYMag()/MyPanel.pxPerMeter;
        info[3][1].setText(String.format("%.2f", x));
        info[3][2].setText(String.format("%.2f", y));

        mass[1].setText(String.format("%.2f", mp.getMass()));
    }

    public void updateInfo() {
        panel.requestFocus();

        String xNum = info[0][1].getText();
        String yNum = info[0][2].getText();

        if (xNum==null && yNum==null && xNum.equals("") && yNum.equals("")) {
            panel.setTotalAcceleration(new Vector2D(0,0));
            return;
        }

        double x,y;

        // Total acceleration
        try {
            x = Double.parseDouble(xNum)*MyPanel.pxPerMeter;
            y = Double.parseDouble(yNum)*MyPanel.pxPerMeter;
            panel.setTotalAcceleration(new Vector2D(x,y));
        } catch (NumberFormatException e) {
            panel.setTotalAcceleration(new Vector2D(0,0));
        }

        HashSet<MovingPoint> elements = panel.getElements();
        for (MovingPoint mp : elements) {
            if (mp.isFocused()) {

                // Position
                xNum = info[1][1].getText();
                yNum = info[1][2].getText();

                try {
                    x = Number.parseDouble(xNum)*MyPanel.pxPerMeter;
                    y = Number.parseDouble(yNum)*MyPanel.pxPerMeter;

                    System.out.println(x + ", " + y);
                    mp.getPosition().setMagnitudes(x,y);
                } catch (RuntimeException e) {
                    mp.getPosition().setMagnitudes(0.0,0.0);
                }

                // Velocity
                xNum = info[2][1].getText();
                yNum = info[2][2].getText();

                try {
                    x = Number.parseDouble(xNum)*MyPanel.pxPerMeter;
                    y = Number.parseDouble(yNum)*MyPanel.pxPerMeter;
                    mp.getVelocity().setMagnitudes(x,y);
                } catch (RuntimeException e) {
                    mp.getVelocity().setMagnitudes(0.0,0.0);
                }

                // Acceleration
                xNum = info[3][1].getText();
                yNum = info[3][2].getText();

                try {
                    x = Number.parseDouble(xNum)*MyPanel.pxPerMeter;
                    y = Number.parseDouble(yNum)*MyPanel.pxPerMeter;
                    mp.getAcceleration().setMagnitudes(x,y);
                } catch (RuntimeException e) {
                    mp.getAcceleration().setMagnitudes(0.0,0.0);
                }

                mass[1].setText(String.format("%.2f", mp.getMass()));
            }
        }
    }

    public class updateAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            panel.requestFocus();
            updateInfo();
            panel.repaint();
        }
    }

    public AbstractAction getUpdateAction() {
        return new updateAction();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(rh);
    }
}
