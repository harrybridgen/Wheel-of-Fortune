package WheelOfFortune.Game;

import java.util.Random;

 /**
 * Generates random negative quadratic y values.
 * @author Harry Bridgen
 * @version 1.0
 */
public class Quadratic {
    double x;
    double a;
    double b;
    double c = 0.1;
    double y;

    /**
     *Constructor generates random a and b variables for the quadratic.
     * The {@link Quadratic#c} variable must always be positive since {@link Quadratic#calculate()}  is called by {@link GUI#wheelSpinAnimationGUI() }
     * by checking if the {@link Quadratic#y} value is positive.
     * @see <a href="https://i.imgur.com/6DB7lkY.jpeg">Smallest and biggest quadratics with default values</a>
     * @see GUI#wheelSpinAnimationGUI()
     */
    public Quadratic() {
        Random random = new Random();
        double randA = random.nextInt(80 - 20) + 20;
        double randB = random.nextInt(30 - 20) + 20;

        a = (randA / 10000) - ((randA / 10000) * 2);
        b = randB/100;
    }

    /**Calculates the quadratic {@link Quadratic#y} value given the random variables from the constructor and the value of {@link Quadratic#x}.
     * Then increments {@link Quadratic#x} by 0.1. Only returns y values that are
     * positive or 0.
     * @return y value of the quadratic
     */
    public double calculate() {

        double xPower2 = x * x;

        y = (a * xPower2) + (b * x) + c;

        if (y >= 0) {
            x += 0.1;
            return y;
        }
        return 0;
    }
}
