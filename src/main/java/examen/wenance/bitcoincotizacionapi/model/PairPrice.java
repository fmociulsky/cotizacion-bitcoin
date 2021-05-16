package examen.wenance.bitcoincotizacionapi.model;

public class PairPrice {
    private String curr1;
    private String curr2;
    private String lprice;

    public PairPrice() {
    }

    public PairPrice(String curr1, String curr2, String lprice) {
        this.curr1 = curr1;
        this.curr2 = curr2;
        this.lprice = lprice;
    }

    public String getCurr1() {
        return curr1;
    }

    public void setCurr1(String curr1) {
        this.curr1 = curr1;
    }

    public String getCurr2() {
        return curr2;
    }

    public void setCurr2(String curr2) {
        this.curr2 = curr2;
    }

    public String getLprice() {
        return lprice;
    }

    public void setLprice(String lprice) {
        this.lprice = lprice;
    }
}
