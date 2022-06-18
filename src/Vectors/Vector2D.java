package Vectors;

import java.util.Objects;

public class Vector2D {
    protected Vector x;
    protected Vector y;

    public Vector2D(Vector x, Vector y) {
        this.x = x;
        this.y = y;
    }

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

    public void setMagnitudes(Double x, Double y) {
        this.x.setMag(x);
        this.y.setMag(y);
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
}
