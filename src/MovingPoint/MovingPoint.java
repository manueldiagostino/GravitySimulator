package MovingPoint;

import Vectors.Force2D;
import Vectors.Vector2D;
import Canvas.MyPanel;
import java.awt.*;
import java.awt.event.FocusListener;
import java.util.LinkedList;
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

    public LinkedList<Vector2D> lastPos;
    public double lastPosAveragex;
    public double lastPosAveragey;
    public LinkedList<Vector2D> lastVel;
    public double lastVelAveragex;
    public double lastVelAveragey;
    public int count;
    public int timeLimitToStopx;
    public int timeLimitToStopy;
    private static final int timeStop = 400;

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

        this.lastPos = new LinkedList<>();
        this.lastPosAveragex = 0.0;
        this.lastPosAveragey = 0.0;
        this.lastVel = new LinkedList<>();
        this.lastVelAveragex = 0.0;
        this.lastVelAveragey = 0.0;
        this.count = 0;
        this.timeLimitToStopx = 0;
        this.timeLimitToStopy = 0;
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

    public void resetTimeLimit() {
        timeLimitToStopx = 0;
        timeLimitToStopy = 0;
        count = 0;
        lastPos.clear();
        lastVel.clear();
        lastPosAveragex = lastPosAveragey = lastVelAveragex = lastVelAveragey = 0.0;
    }
    public void resetTimeLimitX() {
        timeLimitToStopx = 0;
        count = 0;
        lastPos.clear();
        lastVel.clear();
        lastPosAveragex = lastVelAveragex = 0.0;
    }
    public void resetTimeLimitY() {
        timeLimitToStopy = 0;
        count = 0;
        lastPos.clear();
        lastVel.clear();
        lastPosAveragey = lastVelAveragey = 0.0;
    }

    /*
    * Move the object with given acceleration of a deltaTime amount of time (in seconds).
    * It also checks
    * */
    public void move(Vector2D acceleration, double deltaTime) {
        if (timeLimitToStopx >= timeStop && timeLimitToStopy >= timeStop) {
            this.getVelocity().setMagnitudes(0.0,0.0);
            return;
        }

        this.acceleration = acceleration;

        double resx = 0.0;
        double resy = 0.0;
        if (count == 4) {

            for (int i = 0; i < 4; i++) {
                resx += lastPos.get(i).getXMag();
                resy += lastPos.get(i).getYMag();
            }

            lastPosAveragex = resx/4;
            lastPosAveragey = resy/4;

            resx = 0.0;
            resy = 0.0;
            for (int i = 0; i < 4; i++) {
                resx += lastVel.get(i).getXMag();
                resy += lastVel.get(i).getYMag();
            }
            lastVelAveragex = resx/4;
            lastVelAveragey = resy/4;

            count = 0;
        }

        if (lastPos.size() >= 5) {
            lastPos.removeLast();
            lastVel.removeLast();
        }

        // Position
        double x = position.getXMag() +
                velocity.getXMag() * deltaTime +
                (acceleration.getXMag() * deltaTime * deltaTime)/2;
        double y = position.getYMag() +
                velocity.getYMag() * deltaTime +
                (acceleration.getYMag() * deltaTime * deltaTime)/2;
        if (timeLimitToStopx < timeStop)
            position.setXMag(x);
        if (timeLimitToStopy < timeStop)
            position.setYMag(y);

        // Velocity
        x = velocity.getXMag() + acceleration.getXMag() * deltaTime;
        y = velocity.getYMag() + acceleration.getYMag() * deltaTime;
        if (timeLimitToStopx < timeStop)
            velocity.setXMag(x);
        else
            velocity.setXMag(0.0);

        if (timeLimitToStopy < timeStop)
            velocity.setYMag(y);
        else
            velocity.setYMag(0.0);

        if (lastPos.isEmpty() && lastVel.isEmpty()) {
            lastPos.add(this.position);
            lastVel.add(this.velocity);
        }
        else if (lastPos.size() < 4) {
            lastPos.addFirst(this.position);
            lastVel.addFirst(this.velocity);
        }

        count++;
        movingTime += deltaTime;

        double posx = this.position.getXMag();
        double posy = this.position.getYMag();
        double velx = this.velocity.getXMag();
        double vely = this.velocity.getYMag();

        if (Math.abs(posx-lastPosAveragex)<=0.2 && Math.abs(velx-lastVelAveragex)<=0.3)
            timeLimitToStopx += MyPanel.delta;
        if (Math.abs(posy-lastPosAveragey)<=0.2 && Math.abs(vely-lastVelAveragey)<=0.3)
            timeLimitToStopy += MyPanel.delta;

        if (this.focused) {
            System.out.println(lastPosAveragex);
            System.out.println(lastPosAveragey);
            System.out.println(lastVelAveragex);
            System.out.println(lastVelAveragey + "\n ");

            System.out.println(position);
            System.out.println(velocity);
            System.out.println(timeLimitToStopy+"\n");
        }
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


