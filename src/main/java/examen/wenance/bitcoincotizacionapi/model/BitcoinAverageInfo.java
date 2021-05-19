package examen.wenance.bitcoincotizacionapi.model;

public class BitcoinAverageInfo {

    private Double average;
    private Double percent;

    public BitcoinAverageInfo(Double average, Double percent) {
        this.average = average;
        this.percent = percent;
    }

    public BitcoinAverageInfo() {
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public void calculatePercent(Double max){
        percent = Math.round((max/average)*100 * Math.pow(10, 2)) / Math.pow(10, 2) - 100d;
    }
}
