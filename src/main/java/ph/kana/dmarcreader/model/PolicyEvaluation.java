package ph.kana.dmarcreader.model;

public class PolicyEvaluation {

    private String disposition;
    private String dkim;
    private String spf;

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getDkim() {
        return dkim;
    }

    public void setDkim(String dkim) {
        this.dkim = dkim;
    }

    public String getSpf() {
        return spf;
    }

    public void setSpf(String spf) {
        this.spf = spf;
    }
}
