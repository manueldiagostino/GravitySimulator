package Vectors;

import java.util.Objects;

public class Vector2D implements Cloneable {
    protected Vector x;
    protected Vector y;

    public Vector2D(double x, double y) {
        this.x = new Vector(x, 0);
        this.y = new Vector(y, 90);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x.magnitude+other.x.magnitude, this.y.magnitude+other.y.magnitude);
    }

    public double getXMag() {
        return x.magnitude;
    }

    public void setXMag(Double x) {
        this.x.setMag(x);
    }

    public double getYMag() {
        return y.magnitude;
    }

    public void setYMag(Double y) {
        this.y.setMag(y);
    }

    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x.magnitude, 2) + Math.pow(this.y.magnitude, 2));
    }

    public void setMagnitudes(Double x, Double y) {
        this.x.setMag(x);
        this.y.setMag(y);
    }

    public Vector2D oppose() {
        return new Vector2D(-this.x.magnitude, -this.y.magnitude);
    }
    public Vector2D plus(Vector2D other) {
        return new Vector2D(this.x.magnitude+other.x.magnitude, this.y.magnitude+other.y.magnitude);
    }

    public Vector2D minus(Vector2D other) {
        return this.plus(other.oppose());
    }

    public Vector2D dotProduct(double d) {
        return new Vector2D(this.x.magnitude*d, this.y.magnitude*d);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null) return false;
        else if (!(o instanceof Vector2D)) return false;
        Vector2D vector2D = (Vector2D) o;
        return Objects.equals(x, vector2D.x) && Objects.equals(y, vector2D.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + getXMag() + ", " + getYMag() + "]";
    }

    @Override
    public Object clone() {
        Vector2D res = new Vector2D(this.x.magnitude, this.y.magnitude);
        return res;
    }
}
