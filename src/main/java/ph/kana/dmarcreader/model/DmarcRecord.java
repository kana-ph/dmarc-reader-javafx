package ph.kana.dmarcreader.model;

import java.util.List;

public class DmarcRecord {

    private RecordRow row;
    private Identifiers identifiers;
    private List<AuthResult> authResults;

    public RecordRow getRow() {
        return row;
    }

    public void setRow(RecordRow row) {
        this.row = row;
    }

    public Identifiers getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Identifiers identifiers) {
        this.identifiers = identifiers;
    }

    public List<AuthResult> getAuthResults() {
        return authResults;
    }

    public void setAuthResults(List<AuthResult> authResults) {
        this.authResults = authResults;
    }
}
