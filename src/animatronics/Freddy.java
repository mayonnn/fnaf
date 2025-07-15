package animatronics;

public class Freddy {
    private int night4 = (Math.random() >= 0.5) ? 1 : 2;
    private int[][] AILevel = {{0, 0, 0, 0, 0, 0}, //night 1, 12AM/1AM/2AM/3AM/4AM/5AM
            {0, 0, 0, 0, 0, 0}, //night 2
            {1, 1, 1, 1, 1, 1}, //night 3
            {night4, night4, night4, night4, night4, night4}, //night 4
            {3, 3, 3, 3, 3, 3}, //night 5
            {4, 4, 4, 4, 4, 4}}; //night 6

    private double movementTime = 3.02;
    private String location = "1A";

}
