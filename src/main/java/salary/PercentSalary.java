package salary;

import java.util.Objects;

public class PercentSalary {
    private double sellsMoney;
    private double percent;

    public PercentSalary(double sellsMoney, double percent) {
        this.sellsMoney = sellsMoney;
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "PercentSalary{" +
                "sellsMoney=" + sellsMoney +
                ", percent=" + percent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PercentSalary)) return false;
        PercentSalary that = (PercentSalary) o;
        return Double.compare(that.sellsMoney, sellsMoney) == 0 && Double.compare(that.percent, percent) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellsMoney, percent);
    }

    public double getSellsMoney() {
        return sellsMoney;
    }

    public void setSellsMoney(double sellsMoney) {
        this.sellsMoney = sellsMoney;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
