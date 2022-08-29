package Vectors;

import java.util.Objects;

public class Vector implements Cloneable {
    protected double magnitude;
    protected double direction;

    public Vector(double magnitude, double direction) {
        this.magnitude = magnitude;
        this.direction = direction;
    }

    public double getMag() {
        return magnitude;
    }

    public void setMag(double magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null) return false;
        else if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return this.magnitude == vector.magnitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(magnitude);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Vector(this.magnitude, this.direction);
    }
}
