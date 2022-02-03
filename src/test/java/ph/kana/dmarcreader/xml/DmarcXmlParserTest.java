package ph.kana.dmarcreader.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ph.kana.dmarcreader.model.AuthType.DKIM;
import static ph.kana.dmarcreader.model.AuthType.SPF;

class DmarcXmlParserTest {

    private DmarcXmlParser parser;
    private static final ZoneOffset SYSTEM_OFFSET = OffsetDateTime.now().getOffset();

    @BeforeEach
    public void setup() {
        parser = new DmarcXmlParser();
    }

    @Test
    public void setup_xmlString_fromResourcesDir() {
        var xmlStream = getClass().getResourceAsStream("/test-dmarc_1-record.xml");
        var xml = new BufferedReader(new InputStreamReader(xmlStream))
            .lines()
            .collect(Collectors.joining());

        assertTrue(xml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><feedback>"));
        assertTrue(xml.endsWith("</feedback>"));
    }

    @Test
    public void parse_returnFeedback_fromXmlStream() {
        var xmlStream = getClass().getResourceAsStream("/test-dmarc_1-record.xml");
        var feedback = parser.parse(xmlStream);

        assertNotNull(feedback);

        var reportMetadata = feedback.getReportMetadata();
        assertNotNull(reportMetadata);
        assertEquals("google.com", reportMetadata.getOrgName());
        assertEquals("noreply-dmarc-support@google.com", reportMetadata.getEmail());
        assertEquals("https://support.google.com/a/answer/2466580", reportMetadata.getExtraContact());
        assertEquals("164388011082xxxxxxx", reportMetadata.getReportId());

        var dateRange = reportMetadata.getDateRange();
        assertNotNull(dateRange);
        assertEquals(localOffsetDateTime("2022-02-01T08:00:00"), dateRange.getBegin());
        assertEquals(localOffsetDateTime("2022-02-02T07:59:59"), dateRange.getEnd());

        var policyPublished = feedback.getPolicyPublished();
        assertNotNull(policyPublished);
        assertEquals("kana.ph", policyPublished.getDomain());
        assertEquals("r", policyPublished.getDkimAlignment());
        assertEquals("r", policyPublished.getSpfAlignment());
        assertEquals("reject", policyPublished.getDomainPolicy());
        assertEquals("reject", policyPublished.getSubdomainPolicy());
        assertEquals(100, policyPublished.getPercentage());

        var records = feedback.getRecords();
        assertNotNull(records);
        assertEquals(1, records.size());
        var record = records.get(0);

        var row = record.getRow();
        assertNotNull(row);
        assertEquals("192.168.0.1", row.getSourceIp());
        assertEquals(1, row.getCount());

        var policy = row.getPolicyEvaluated();
        assertNotNull(policy);
        assertEquals("none", policy.getDisposition());
        assertEquals("pass", policy.getDkim());
        assertEquals("pass", policy.getSpf());

        var identifiers = record.getIdentifiers();
        assertNotNull(identifiers);
        assertEquals("kana.ph", identifiers.getHeaderFrom());

        var authResults = record.getAuthResults();
        assertNotNull(authResults);
        assertEquals(2, authResults.size());

        var dkimResult = authResults.get(0);
        assertEquals(DKIM, dkimResult.getType());
        assertEquals("kana.ph", dkimResult.getDomain());
        assertEquals("pass", dkimResult.getResult());
        assertEquals("selector", dkimResult.getSelector());

        var spfResult = authResults.get(1);
        assertEquals(SPF, spfResult.getType());
        assertEquals("kana.ph", spfResult.getDomain());
        assertEquals("pass", spfResult.getResult());
    }

    @Test
    public void parse_returnFeedbackWith2Records_from2RecordsXml() {
        var xmlStream = getClass().getResourceAsStream("/test-dmarc_2-records.xml");
        var feedback = parser.parse(xmlStream);

        assertNotNull(feedback);

        var reportMetadata = feedback.getReportMetadata();
        assertNotNull(reportMetadata);
        assertEquals("Yahoo", reportMetadata.getOrgName());
        assertEquals("dmarchelp@yahooinc.com", reportMetadata.getEmail());
        assertEquals("16436xxxxx.yyyyyy", reportMetadata.getReportId());

        var dateRange = reportMetadata.getDateRange();
        assertNotNull(dateRange);
        assertEquals(localOffsetDateTime("2022-01-31T08:00:00"), dateRange.getBegin());
        assertEquals(localOffsetDateTime("2022-02-01T07:59:59"), dateRange.getEnd());

        var policyPublished = feedback.getPolicyPublished();
        assertNotNull(policyPublished);
        assertEquals("kana.ph", policyPublished.getDomain());
        assertEquals("r", policyPublished.getDkimAlignment());
        assertEquals("r", policyPublished.getSpfAlignment());
        assertEquals("reject", policyPublished.getDomainPolicy());
        assertEquals(100, policyPublished.getPercentage());

        var records = feedback.getRecords();
        assertNotNull(records);
        assertEquals(2, records.size());
        var record1 = records.get(0);

        var row = record1.getRow();
        assertNotNull(row);
        assertEquals("192.168.0.1", row.getSourceIp());
        assertEquals(1, row.getCount());

        var policy = row.getPolicyEvaluated();
        assertNotNull(policy);
        assertEquals("none", policy.getDisposition());
        assertEquals("fail", policy.getDkim());
        assertEquals("pass", policy.getSpf());

        var identifiers = record1.getIdentifiers();
        assertNotNull(identifiers);
        assertEquals("kana.ph", identifiers.getHeaderFrom());

        var authResults = record1.getAuthResults();
        assertNotNull(authResults);
        assertEquals(1, authResults.size());

        var spfResult = authResults.get(0);
        assertEquals(SPF, spfResult.getType());
        assertEquals("kana.ph", spfResult.getDomain());
        assertEquals("pass", spfResult.getResult());

        var record2 = records.get(1);

        row = record2.getRow();
        assertNotNull(row);
        assertEquals("192.168.0.2", row.getSourceIp());
        assertEquals(1, row.getCount());

        policy = row.getPolicyEvaluated();
        assertNotNull(policy);
        assertEquals("none", policy.getDisposition());
        assertEquals("fail", policy.getDkim());
        assertEquals("pass", policy.getSpf());

        identifiers = record2.getIdentifiers();
        assertNotNull(identifiers);
        assertEquals("kana.ph", identifiers.getHeaderFrom());

        authResults = record2.getAuthResults();
        assertNotNull(authResults);
        assertEquals(1, authResults.size());

        spfResult = authResults.get(0);
        assertEquals(SPF, spfResult.getType());
        assertEquals("kana.ph", spfResult.getDomain());
        assertEquals("pass", spfResult.getResult());
    }

    private OffsetDateTime localOffsetDateTime(String string) {
        var ldt = LocalDateTime.parse(string);
        return OffsetDateTime.of(ldt, SYSTEM_OFFSET);
    }
}
