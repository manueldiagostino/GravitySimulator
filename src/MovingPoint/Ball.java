package MovingPoint;

import Shapes.Arrow;
import Vectors.Vector2D;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.geom.Ellipse2D;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import Canvas.MyPanel;

public class Ball extends MovingPoint {
    private final Color color;
    private BufferedImage icon;
    private int width;

    public Ball(MyPanel panel, Vector2D position, Vector2D velocity,
                double mass, int width, Color color) {
        super(panel, position, velocity, mass);
        this.width = width;
        this.color = color;
        this.icon = null;
    }

    public Ball(MyPanel panel, Vector2D position, Vector2D velocity, double mass) {
        super(panel, position, velocity, mass);
        this.color = Color.WHITE;

        try {
            this.icon = ImageIO.read(new File("/home/manuel/IdeaProjects/GravitySimulator/images/ball_40x40.png"));
        } catch (IOException e) {
            System.err.println("Error in loading ball's image");
            return;
        }

        this.width = 20;
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= position.getXMag() && x <= position.getXMag() + width &&
                y >= position.getYMag() && y <= position.getYMag() + width;
    }

    @Override
    public void edges(double left, double right, double bottom, double top) {
        double x = position.getXMag();
        double y = position.getYMag();

        if (x < left || x > right-width) {
            if (x <= left)
                position.setMagnitudes(left, y);
            else
                position.setMagnitudes(right - width, position.getYMag());

            velocity.setMagnitudes((double)-3/5*velocity.getXMag(), velocity.getYMag());
        }

        if (y<top || y+width > bottom) {
            if (y <= top)
                position.setMagnitudes(position.getXMag(), top);
            else
                position.setMagnitudes(position.getXMag(), bottom - width);

            velocity.setMagnitudes(velocity.getXMag(),(double)-3/5*velocity.getYMag());
        }
    }

    public void setName(String s) {
        this.name = s;
    }

    @Override
    public void drawName(Graphics2D g) {
        Font f = new Font("Comic Sans MS", Font.BOLD, 15);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D.Double background = new Rectangle2D.Double(position.getXMag()-fm.stringWidth(name)-5, position.getYMag()-fm.getHeight()-5,
                fm.stringWidth(name), fm.getHeight());
        g.setColor(Color.black);
        g.fill(background);

        g.setColor(Color.white);
        g.drawString(name, (int) (background.getX()), (int) (background.getY() + (background.getHeight()-fm.getHeight())/2 + fm.getAscent()));

    }

    @Override
    public void draw(Graphics2D g) {
        if (icon == null) {
            Ellipse2D.Double point = new Ellipse2D.Double(position.getXMag(), position.getYMag(), width, width);
            g.setColor(color);
            g.fill(point);
        }
        else {
            g.drawImage(icon, (int)position.getXMag(), (int)position.getYMag(), width, width, null);
        }

//        drawName(g);
    }

    @Override
    public void drawVelTrajectory(Graphics2D g, Color c) {
        Arrow.draw(g, new Point2D.Double(position.getXMag()+(double) width/2, position.getYMag()+(double) width/2),
                new Point2D.Double(position.getXMag()+(double) width/2+(velTrajectory.getXMag()/2),position.getYMag()+(double) width/2+(velTrajectory.getYMag()/2)),
                new BasicStroke(2f), new BasicStroke(10f), 20, Color.red);
    }

    @Override
    public void drawTrajectory(Graphics2D g) {
        g.setColor(Color.lightGray);

        Ball T = new Ball(panel, new Vector2D(position.getXMag()+(double)width/2, position.getYMag()+(double)width/2), velTrajectory, 1, 4, Color.white);
        Ellipse2D.Double point = new Ellipse2D.Double(T.position.getXMag(), T.position.getYMag(), 4, 4);
        Vector2D acc = panel.getTotalAcceleration();

        int count = 0;
        while (count<50) {
            T.move(acc, 0.025);
            T.edges(0, panel.getWidth(), panel.getHeight(), -10000);

            point.setFrame(T.position.getXMag(), T.position.getYMag(), T.getWidth(), T.getWidth());
            g.fill(point);
            count++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ball)) return false;
        if (!super.equals(o)) return false;
        Ball ball = (Ball) o;
        return super.equals(ball) &&
                this.width == ball.width &&
                this.color.equals(ball.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width, color);
    }

    public double getWidth() {
        return width;
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        focused = true;
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        focused = false;
    }
}
