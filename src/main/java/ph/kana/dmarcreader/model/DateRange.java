package ph.kana.dmarcreader.model;

import java.time.OffsetDateTime;

public class DateRange {
    private OffsetDateTime begin;
    private OffsetDateTime end;

    public OffsetDateTime getBegin() {
        return begin;
    }

    public void setBegin(OffsetDateTime begin) {
        this.begin = begin;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public void setEnd(OffsetDateTime end) {
        this.end = end;
    }
}
