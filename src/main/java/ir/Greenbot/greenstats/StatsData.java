package ir.Greenbot.greenstats;

public class StatsData {

    private int kills;
    private int deaths;
    private int currentStreak;
    private int bestStreak;

    public StatsData() {
        this.kills = 0;
        this.deaths = 0;
        this.currentStreak = 0;
        this.bestStreak = 0;
    }

    public StatsData(int kills, int deaths, int currentStreak, int bestStreak) {
        this.kills = kills;
        this.deaths = deaths;
        this.currentStreak = currentStreak;
        this.bestStreak = bestStreak;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public void setBestStreak(int bestStreak) {
        this.bestStreak = bestStreak;
    }

    public void addKill() {
        this.kills++;
        this.currentStreak++;
        if (this.currentStreak > this.bestStreak) {
            this.bestStreak = this.currentStreak;
        }
    }

    public void addDeath() {
        this.deaths++;
        this.currentStreak = 0;
    }

    // نسبت کشتن به مرگ (KDR)
    public double getKdr() {
        if (deaths == 0) return kills;
        return Math.round((double) kills / deaths * 100.0) / 100.0;
    }
}
