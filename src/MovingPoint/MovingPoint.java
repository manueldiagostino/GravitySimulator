package MovingPoint;

import Vectors.Force2D;
import Vectors.Vector2D;
import Canvas.MyPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusListener;
import java.util.Objects;

// Abstract class that implements the concept of a Moving Point in the space
public abstract class MovingPoint implements FocusListener {
    protected static MyPanel panel;
    protected String name;
    protected boolean focused;

    protected Vector2D position;
    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected double movingTime;
    protected double mass;

    public boolean isTrajectory;
    public Vector2D velTrajectory;
    public MovingPoint(MyPanel panel, Vector2D position, Vector2D velocity,
                       double mass) {
        MovingPoint.panel = panel;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = new Vector2D(0,0);

        this.movingTime = 0;
        this.mass = mass;
        this.isTrajectory = false;
        this.name = "A";
        this.focused = false;
    }

    public MovingPoint(MyPanel panel, Vector2D position, double mass) {
        this(panel, position, new Vector2D(0,0), mass);
    }

    public void move(Force2D f, double deltaTime) {
        acceleration.setMagnitudes(f.getXMag()/mass, f.getYMag()/mass);

        // Position
        double x = position.getXMag() +
                velocity.getXMag() * deltaTime +
                (acceleration.getXMag() * deltaTime * deltaTime)/2;
        double y = position.getYMag() +
                velocity.getYMag() * deltaTime +
                (acceleration.getYMag() * deltaTime * deltaTime)/2;
        position.setMagnitudes(x,y);

        // Velocity
        x = velocity.getXMag() + acceleration.getXMag() * deltaTime;
        y = velocity.getYMag() + acceleration.getYMag() * deltaTime;
        velocity.setMagnitudes(x,y);

        movingTime += deltaTime;
    }

    public void move(Vector2D acceleration, double deltaTime) {
        this.acceleration = acceleration;

        // Position
        double x = position.getXMag() +
                velocity.getXMag() * deltaTime +
                (acceleration.getXMag() * deltaTime * deltaTime)/2;
        double y = position.getYMag() +
                velocity.getYMag() * deltaTime +
                (acceleration.getYMag() * deltaTime * deltaTime)/2;
        position.setMagnitudes(x,y);

        // Velocity
        x = velocity.getXMag() + acceleration.getXMag() * deltaTime;
        y = velocity.getYMag() + acceleration.getYMag() * deltaTime;
        velocity.setMagnitudes(x,y);

        movingTime += deltaTime;
    }

    public abstract void drawVelTrajectory(Graphics2D g, Color c);
    public abstract void drawTrajectory(Graphics2D g);
    public abstract boolean contains(int x, int y);
    public abstract boolean detectBump(MovingPoint other);

    public abstract void bump(MovingPoint other);
    public abstract void edges(double left, double right, double bottom, double top);
    public abstract void draw(Graphics2D g);
    public abstract void drawName(Graphics2D g);
    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean b) {
        this.focused = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovingPoint)) return false;
        if (!super.equals(o)) return false;
        MovingPoint that = (MovingPoint) o;
        return this.position.equals(that.position) &&
                this.velocity.equals(that.velocity) &&
                this.acceleration.equals(that.acceleration) &&
                this.mass == that.mass &&
                this.movingTime == that.movingTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), position, velocity, acceleration, movingTime, mass);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }

    public double getMovingTime() {
        return movingTime;
    }

    public void setMovingTime(double movingTime) {
        this.movingTime = movingTime;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}


