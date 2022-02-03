package ph.kana.dmarcreader.model;

import java.util.ArrayList;
import java.util.List;

public class DmarcFeedback {

    private ReportMetadata reportMetadata;
    private DmarcPolicy policyPublished;
    private final List<DmarcRecord> records = new ArrayList<>();

    public ReportMetadata getReportMetadata() {
        return reportMetadata;
    }

    public void setReportMetadata(ReportMetadata reportMetadata) {
        this.reportMetadata = reportMetadata;
    }

    public DmarcPolicy getPolicyPublished() {
        return policyPublished;
    }

    public void setPolicyPublished(DmarcPolicy policyPublished) {
        this.policyPublished = policyPublished;
    }

    public List<DmarcRecord> getRecords() {
        return records;
    }

    public void addRecord(DmarcRecord record) {
        records.add(record);
    }
}
