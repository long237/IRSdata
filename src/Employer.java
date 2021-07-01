public class Employer {
    private String EINnum;
    private String tax_pd;

    Employer (String EINnum, String tax_pd){
        this.EINnum = EINnum;
        this.tax_pd = tax_pd;
    }

    public String getEINnum() {
        return EINnum;
    }

    public void setEINnum(String EINnum) {
        this.EINnum = EINnum;
    }

    public String getTax_pd() {
        return tax_pd;
    }

    public void setTax_pd(String tax_pd) {
        this.tax_pd = tax_pd;
    }

    @Override
    public String toString() {
        return "Employer{" +
                "EINnum='" + EINnum + '\'' +
                ", tax_pd='" + tax_pd + '\'' +
                '}';
    }
}
