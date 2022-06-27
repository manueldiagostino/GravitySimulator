package Canvas;

import MovingPoint.*;
import Vectors.Vector2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import Shapes.Arrow;

public class MyPanel extends JPanel implements ActionListener {
    private final int width;
    private final int height;
    private BufferedImage background;
    private final Timer timer;
    private final HashSet<MovingPoint> elements;
    private final MyMouseHandler mh;
    private final ControlsPanel cp;

    // Time in milliseconds between two repaint()
    private static final int delta = 16;
    // How many pixels for a real meter
    public static final int pxPerMeter = 100;

    private static final Vector2D gravityAcc = new Vector2D(0, 9.81*pxPerMeter);

    public void setTotalAcceleration(Vector2D totalAcceleration) {
        this.totalAcceleration = totalAcceleration;
    }

    private Vector2D totalAcceleration;


    private void initKeyBindings() {
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pause");
        this.getActionMap().put("pause", new pauseAction());

        AbstractAction update = cp.getUpdateAction();
        this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "update");
        this.getActionMap().put("update", update);
    }

    public MyPanel(int width, int height) {
        super(true);

        this.width = width;
        this.height = height;
        this.setBackground(Color.BLACK);
        this.setBackgroundImage(new File("/home/manuel/IdeaProjects/GravitySimulator/images/wall_1200x780.jpg"));
        this.setPreferredSize(new Dimension(width,height));

        this.timer = new Timer(delta, this);
        this.elements = new HashSet<>();
        this.mh = new MyMouseHandler(this);

        this.totalAcceleration = gravityAcc;
        Ball b1 = new Ball(this, new Vector2D(200,height-20), new Vector2D(0,0),5);
        Ball b2 = new Ball(this, new Vector2D(400,height-20), new Vector2D(0,0),5);
        b2.setName("Daniel");
        this.elements.add(b1);
        this.elements.add(b2);

        // Adding cursors panel
        int rows = 4;
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10,10,0,0);
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        this.cp = new ControlsPanel(this, rows);
        this.cp.getInfo()[0][1].setText(""+totalAcceleration.getXMag()/pxPerMeter);
        this.cp.getInfo()[0][2].setText(""+totalAcceleration.getYMag()/pxPerMeter);
        this.add(cp, c);

        this.initKeyBindings();
    }

    public void setBackgroundImage(File file) {
        try {
            background = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error in reading background image");
            background = null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        cp.repaint();
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        if (background != null) {
            g2d.drawImage(background, 0, 0, null);
        }

        for (MovingPoint mp : elements) {
            mp.draw(g2d);

            if (mp.isTrajectory) {
                mp.drawVelTrajectory(g2d, Color.red);
                mp.drawTrajectory(g2d);
            }

            if (mp.isFocused())
                mp.drawName(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (MovingPoint p : elements) {
            p.move(totalAcceleration, (double)delta/1000);
            p.edges(0, width, height, -10000);
        }
        repaint();
    }

    public void pause() {
        if (timer.isRunning())
            timer.stop();
        else
            timer.start();
    }

    public class pauseAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            pause();
        }
    }

    public HashSet<MovingPoint> getElements() {
        return elements;
    }

    public Vector2D getTotalAcceleration() {
        return totalAcceleration;
    }
}
