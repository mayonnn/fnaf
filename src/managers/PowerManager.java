package managers;

import java.awt.*;

public class PowerManager {
    private static final int BASE_DRAIN_INTERVAL_MS = 6000;

    private double powerLevel;
    private int powerUsage;
    private long lastPowerDrainTime;

    private static final int MAX_POWER = 100;

    public PowerManager() {
        this.powerLevel = MAX_POWER;
        this.powerUsage = 0;
        this.lastPowerDrainTime = System.currentTimeMillis();
    }

    public void drawPowerStats(Graphics g, int x, int y, Font font) {
        drawPowerLevel(g, x, y, font);
        drawUsageBar(g, x, y, font);
    }

    private void drawPowerLevel(Graphics g, int x, int y, Font font) {
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString("Power left: " + (int) powerLevel + "%", x, y);
    }

    private void drawUsageBar(Graphics g, int x, int y, Font font) {
        g.drawString("Usage:", x, y + font.getSize() + 5);
        FontMetrics fm = g.getFontMetrics();
        int usageTextWidth = fm.stringWidth("Usage:");
        int barStartX = x + usageTextWidth + 10;
        int barY = y + font.getSize() - 10;

        if (powerUsage >= 0) {
            g.setColor(Color.GREEN);
            g.fillRect(barStartX , barY, 5, font.getSize());
        }
        if (powerUsage >= 1) {
            g.setColor(Color.GREEN);
            g.fillRect(barStartX + 10, barY, 5, font.getSize());
        }
        if (powerUsage >= 2) {
            g.setColor(Color.ORANGE);
            g.fillRect(barStartX + 20, barY, 5, font.getSize());
        }
        if (powerUsage >= 3) {
            g.setColor(Color.RED);
            g.fillRect(barStartX + 30, barY, 5, font.getSize());
        }
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPowerDrainTime >= BASE_DRAIN_INTERVAL_MS / (powerUsage + 1)) {
            powerLevel = Math.max(0, powerLevel - 1);
            lastPowerDrainTime = currentTime;
        }
    }

    public void reset() {
        powerLevel = MAX_POWER;
        powerUsage = 0;
        lastPowerDrainTime = System.currentTimeMillis();
    }

    public double getPowerLevel() {
        return powerLevel;
    }

    public int getPowerUsage() {
        return powerUsage;
    }

    public void increasePowerUsage() {
        if (powerUsage < 4) powerUsage++;
    }

    public void decreasePowerUsage() {
        if (powerUsage > 0) powerUsage--;
    }

    public boolean isPowerDepleted() {
        return powerLevel <= 0;
    }
}