public class PowerManager {
    private double powerLevel;
    private int powerUsage;
    private long lastPowerDrainTime;

    private static final int MAX_POWER = 100;

    public PowerManager() {
        this.powerLevel = MAX_POWER;
        this.powerUsage = 0;
        this.lastPowerDrainTime = System.currentTimeMillis();
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPowerDrainTime >= 6000 / (powerUsage + 1)) {
            powerLevel = Math.max(0, powerLevel - 1);
            lastPowerDrainTime = currentTime;
        }
    }

    public void reset() {
        powerLevel = MAX_POWER;
        lastPowerDrainTime = System.currentTimeMillis();
    }

    public double getPowerLevel() {
        return powerLevel;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(int powerUsage) {
        this.powerUsage = powerUsage;
    }

    public boolean isPowerDepleted() {
        return powerLevel <= 0;
    }
}