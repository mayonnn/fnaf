package animatronics;

public class Foxy {
    private int[][] AILevel = {{0, 0, 0, 1, 2, 2}, //night 1, 12AM/1AM/2AM/3AM/4AM/5AM
            {1, 1, 1, 2, 3, 3}, //night 2
            {2, 2, 2, 3, 4, 4}, //night 3
            {6, 6, 6, 7, 8, 8}, //night 4
            {5, 5, 5, 6, 7, 7}, //night 5
            {16, 16, 16, 17, 18, 18}}; //night 6

    private double movementTime = 5.01;
    private String location = "1C";

}
