package ph.kana.dmarcreader.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ph.kana.dmarcreader.model.DmarcRecord;

import static ph.kana.dmarcreader.model.AuthType.DKIM;
import static ph.kana.dmarcreader.model.AuthType.SPF;

public class DmarcTableRecord {
    private final StringProperty sourceIp = new SimpleStringProperty(this, "sourceIp");
    private final StringProperty count = new SimpleStringProperty(this, "count");
    private final StringProperty disposition = new SimpleStringProperty(this, "disposition");
    private final StringProperty spf = new SimpleStringProperty(this, "spf");
    private final StringProperty dkim = new SimpleStringProperty(this, "dkim");
    private final StringProperty headerFrom = new SimpleStringProperty(this, "headerFrom");
    private final StringProperty domainSpfResult = new SimpleStringProperty(this, "domainSpfResult");
    private final StringProperty domainDkimResult = new SimpleStringProperty(this, "domainDkimResult");
    private final StringProperty forwarderResult = new SimpleStringProperty(this, "forwarderResult");

    public DmarcTableRecord() { }

    DmarcTableRecord(DmarcRecord record) {
        sourceIp.set(record.getRow().getSourceIp());
        count.set(Integer.toString(record.getRow().getCount()));
        disposition.set(record.getRow().getPolicyEvaluated().getDisposition());
        spf.set(record.getRow().getPolicyEvaluated().getSpf().toUpperCase());
        dkim.set(record.getRow().getPolicyEvaluated().getDkim().toUpperCase());
        headerFrom.set(record.getIdentifiers().getHeaderFrom());
        var domainSpfValue = record.getAuthResults()
            .stream()
            .filter(ar -> SPF == ar.getType())
            .map(ar -> ar.getDomain() + " / " + ar.getResult().toUpperCase())
            .findFirst()
            .orElse("");
        domainSpfResult.set(domainSpfValue);
        var domainDkimValue = record.getAuthResults()
            .stream()
            .filter(ar -> DKIM == ar.getType())
            .map(ar -> ar.getDomain() + " / " + ar.getSelector() + " / " + ar.getResult().toUpperCase())
            .findFirst()
            .orElse("");
        domainDkimResult.set(domainDkimValue);
    }

    public String getSourceIp() {
        return sourceIp.get();
    }

    public StringProperty sourceIpProperty() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp.set(sourceIp);
    }

    public String getCount() {
        return count.get();
    }

    public StringProperty countProperty() {
        return count;
    }

    public void setCount(String count) {
        this.count.set(count);
    }

    public String getDisposition() {
        return disposition.get();
    }

    public StringProperty dispositionProperty() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition.set(disposition);
    }

    public String getSpf() {
        return spf.get();
    }

    public StringProperty spfProperty() {
        return spf;
    }

    public void setSpf(String spf) {
        this.spf.set(spf);
    }

    public String getDkim() {
        return dkim.get();
    }

    public StringProperty dkimProperty() {
        return dkim;
    }

    public void setDkim(String dkim) {
        this.dkim.set(dkim);
    }

    public String getHeaderFrom() {
        return headerFrom.get();
    }

    public StringProperty headerFromProperty() {
        return headerFrom;
    }

    public void setHeaderFrom(String headerFrom) {
        this.headerFrom.set(headerFrom);
    }

    public String getDomainSpfResult() {
        return domainSpfResult.get();
    }

    public StringProperty domainSpfResultProperty() {
        return domainSpfResult;
    }

    public void setDomainSpfResult(String domainSpfResult) {
        this.domainSpfResult.set(domainSpfResult);
    }

    public String getDomainDkimResult() {
        return domainDkimResult.get();
    }

    public StringProperty domainDkimResultProperty() {
        return domainDkimResult;
    }

    public void setDomainDkimResult(String domainDkimResult) {
        this.domainDkimResult.set(domainDkimResult);
    }

    public String getForwarderResult() {
        return forwarderResult.get();
    }

    public StringProperty forwarderResultProperty() {
        return forwarderResult;
    }

    public void setForwarderResult(String forwarderResult) {
        this.forwarderResult.set(forwarderResult);
    }
}
