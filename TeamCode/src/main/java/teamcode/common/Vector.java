package teamcode.common;

public class Vector {

    public static final Vector ZERO = new Vector(0.0, 0.0);
    public static final Vector FORWARD = new Vector(0.0, 1.0);
    public static final Vector RIGHT = new Vector(1.0, 0.0);

    private final double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public double getDirectionRadians() {
        if (x == 0.0) {
            if (y > 0.0) {
                return Math.PI / 2;
            } else if (y < 0.0) {
                return Math.PI;
            }
            throw new IllegalStateException("zero vectors don't have a direction");
        }
        return Math.atan(y / x);
    }

    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }

    /**
     * Returns a new vector without modifying this one.
     */
    public Vector multiply(double d) {
        return new Vector(x * d, y * d);
    }

    public Vector normalized() {
        double magnitude = magnitude();
        return new Vector(x / magnitude, y / magnitude);
    }

    public double dotProduct(Vector other) {
        return this.x * other.x + this.y * other.y;
    }

    public double angleDegreesBetween(Vector other) {
        return Math.toDegrees((Math.acos(dotProduct(other) / (magnitude() * other.magnitude()))));
    }

    public boolean isZero() {
        return x == 0.0 && y == 0.0;
    }

}
