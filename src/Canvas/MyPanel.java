package Canvas;

import MovingPoint.*;
import Vectors.Vector2D;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
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

    // Time in milliseconds between two repaint()
    private static final int delta = 16;
    // How many pixels for a real meter
    private static final int pxPerMeter = 100;

    private static final Vector2D gravityAcc = new Vector2D(0, 9.81*pxPerMeter);
    private Vector2D totalAcceleration;


    private void initKeyBindings() {
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "pause");
        this.getActionMap().put("pause", new pauseAction());
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
        this.elements.add(b1);
        this.elements.add(b2);

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
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.requestFocus();

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
