package ph.kana.dmarcreader.model;

public class DmarcPolicy {

    private String domain;
    private String dkimAlignment;     // adkim
    private String spfAlignment;      // aspf
    private String domainPolicy;      // p
    private String subdomainPolicy;   // sp
    private int percentage;           // pct

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDkimAlignment() {
        return dkimAlignment;
    }

    public void setDkimAlignment(String dkimAlignment) {
        this.dkimAlignment = dkimAlignment;
    }

    public String getSpfAlignment() {
        return spfAlignment;
    }

    public void setSpfAlignment(String spfAlignment) {
        this.spfAlignment = spfAlignment;
    }

    public String getDomainPolicy() {
        return domainPolicy;
    }

    public void setDomainPolicy(String domainPolicy) {
        this.domainPolicy = domainPolicy;
    }

    public String getSubdomainPolicy() {
        return subdomainPolicy;
    }

    public void setSubdomainPolicy(String subdomainPolicy) {
        this.subdomainPolicy = subdomainPolicy;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
