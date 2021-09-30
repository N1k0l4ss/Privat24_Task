package logic;

public class Profit {
    private String name;
    private String hourProfit;
    private String dollarProfit;

    public Profit(String name, String hourProfit, String dollarProfit) {
        this.name = name;
        this.hourProfit = hourProfit;
        this.dollarProfit = dollarProfit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHourProfit() {
        return hourProfit;
    }

    public void setHourProfit(String hourProfit) {
        this.hourProfit = hourProfit;
    }

    public String getDollarProfit() {
        return dollarProfit;
    }

    public void setDollarProfit(String dollarProfit) {
        this.dollarProfit = dollarProfit;
    }
}
