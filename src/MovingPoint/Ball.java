package MovingPoint;

import Shapes.Arrow;
import Vectors.Vector2D;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.geom.Ellipse2D;
import javax.imageio.ImageIO;
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
    private int diameter;

    public Ball(MyPanel panel, Vector2D position, Vector2D velocity,
                double mass, int diameter, Color color) {
        super(panel, position, velocity, mass);
        this.diameter = diameter;
        this.color = color;
        this.icon = null;
    }

    public Ball(MyPanel panel, Vector2D position, Vector2D velocity, double mass) {
        super(panel, position, velocity, mass);
        this.color = Color.WHITE;

        try {
            this.icon = ImageIO.read(new File("/home/manuel/IdeaProjects/GravitySimulator/images/ball_40x40.png"));
        } catch (IOException e) {
            System.err.println("Error in loading ball'`s` image");
            return;
        }

        this.diameter = 20;
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= position.getXMag() && x <= position.getXMag() + diameter &&
                y >= position.getYMag() && y <= position.getYMag() + diameter;
    }

    @Override
    public void edges(double left, double right, double bottom, double top) {
        double x = position.getXMag();
        double y = position.getYMag();

        if (x < left || x > right-diameter) {
            if (x <= left)
                position.setMagnitudes(left, y);
            else
                position.setMagnitudes(right - diameter, position.getYMag());

            velocity.setMagnitudes((double)-3/5*velocity.getXMag(), velocity.getYMag());
        }

        if (y<top || y+diameter > bottom) {
            if (y <= top)
                position.setMagnitudes(position.getXMag(), top);
            else
                position.setMagnitudes(position.getXMag(), bottom - diameter);

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
                fm.stringWidth(this.name), fm.getHeight());
        g.setColor(Color.black);
        g.fill(background);

        g.setColor(Color.white);
        g.drawString(this.name, (int) (background.getX()), (int) (background.getY() + (background.getHeight()-fm.getHeight())/2 + fm.getAscent()));
    }

    @Override
    public void draw(Graphics2D g) {
        if (icon == null) {
            Ellipse2D.Double point = new Ellipse2D.Double(position.getXMag(), position.getYMag(), diameter, diameter);
            g.setColor(color);
            g.fill(point);
        }
        else {
            g.drawImage(icon, (int)position.getXMag(), (int)position.getYMag(), diameter, diameter, null);
        }

//        drawName(g);
    }

    @Override
    public void drawVelTrajectory(Graphics2D g, Color c) {
        Arrow.draw(g, new Point2D.Double(position.getXMag()+(double) diameter/2, position.getYMag()+(double) diameter/2),
                new Point2D.Double(position.getXMag()+(double) diameter/2+(velTrajectory.getXMag()/2),position.getYMag()+(double) diameter/2+(velTrajectory.getYMag()/2)),
                new BasicStroke(2f), new BasicStroke(10f), 20, Color.red);
    }

    @Override
    public void drawTrajectory(Graphics2D g) {
        g.setColor(Color.lightGray);

        Ball T = new Ball(panel, new Vector2D(position.getXMag()+(double)diameter/2, position.getYMag()+(double)diameter/2), velTrajectory, 1, 4, Color.white);
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
                this.diameter == ball.diameter &&
                this.color.equals(ball.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diameter, color);
    }

    public double getWidth() {
        return diameter;
    }

    @Override
    public void focusGained(FocusEvent focusEvent) {
        focused = true;
    }

    @Override
    public void focusLost(FocusEvent focusEvent) {
        focused = false;
    }
    @Override
    public boolean detectBump(MovingPoint o) {
        if (o==null || this.equals(o))
            return false;
        else if (!(o instanceof Ball))
            return false;

        Ball other = (Ball)o;
        Vector2D center1 = new Vector2D(this.getPosition().getXMag()+(double)this.diameter/2, this.getPosition().getYMag()+(double)this.diameter/2);
        Vector2D center2 = new Vector2D(other.getPosition().getXMag()+(double)other.diameter/2, other.getPosition().getYMag()+(double) other.diameter/2);

        Vector2D distance = center1.minus(center2);
        return distance.getMagnitude() <= (double)this.diameter/2 + (double)other.diameter/2;
    }
    @Override
    public void bump(MovingPoint o) {
        if (o==null || this.equals(o))
            return;
        else if (!(o instanceof Ball))
            return;

        Ball other = (Ball)o;

        double a,b;
        a = (this.mass - other.mass)/(this.mass + other.mass);
        b = (2*other.mass)/(this.mass + other.mass);

        Vector2D thisFinalVel = (Vector2D) this.velocity.clone();
        thisFinalVel = thisFinalVel.dotProduct(a);
        thisFinalVel = thisFinalVel.plus(other.velocity.dotProduct(b));

        System.out.println(this.velocity);
        System.out.println(thisFinalVel);

        a = (2*this.mass)/(this.mass + other.mass);
        b = (other.mass - this.mass)/(this.mass + other.mass);

        Vector2D otherFinalVel = (Vector2D) this.velocity.clone();
        otherFinalVel = otherFinalVel.dotProduct(a);
        otherFinalVel = otherFinalVel.plus(other.velocity.dotProduct(b));

        this.setVelocity(thisFinalVel);
        other.setVelocity(otherFinalVel);

        this.move(panel.getTotalAcceleration(), (double) MyPanel.delta /1000);
        other.move(panel.getTotalAcceleration(), (double) MyPanel.delta /1000);
    }

    public static void main(String[] args) {
        Ball b1 = new Ball(null, new Vector2D(40,40), new Vector2D(0,0),5);
        Ball b2 = new Ball(null, new Vector2D(60,20), new Vector2D(0,0),5);

        System.out.println(b1.detectBump(b2));
    }
}
