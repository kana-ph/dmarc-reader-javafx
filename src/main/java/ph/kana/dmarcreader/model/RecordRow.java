package ph.kana.dmarcreader.model;

public class RecordRow {

    private String sourceIp;
    private int count;
    private PolicyEvaluation policyEvaluated;

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PolicyEvaluation getPolicyEvaluated() {
        return policyEvaluated;
    }

    public void setPolicyEvaluated(PolicyEvaluation policyEvaluated) {
        this.policyEvaluated = policyEvaluated;
    }
}
