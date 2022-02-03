package ph.kana.dmarcreader.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import ph.kana.dmarcreader.model.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static ph.kana.dmarcreader.model.AuthType.DKIM;
import static ph.kana.dmarcreader.model.AuthType.SPF;

class DmarcXmlDocumentHandler extends DefaultHandler {

    private DmarcFeedback feedback;
    private Stack<String> elementStack;
    private Map<String, Object> xmlObjects;
    private static final Map<String, XmlElementHandler> HANDLERS = initializeElementHandlers();

    @Override
    public void startDocument() {
        elementStack = new Stack<>();
        xmlObjects = new LinkedHashMap<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        String thisPath;
        if (elementStack.isEmpty()) {
            thisPath = "feedback";
        } else {
            var lastPath = elementStack.peek();
            thisPath = lastPath + "." + qName;
        }
        elementStack.push(thisPath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void endElement(String uri, String localName, String qName) {
        var thisPath = elementStack.pop();
        var obj = xmlObjects.get(thisPath);
        var handler = HANDLERS.get(thisPath);
        if (obj instanceof DmarcFeedback feedback) {
            this.feedback = feedback;
        } else if (obj != null && handler instanceof XmlObjectHandler objectHandler) {
            var parentPath = objectHandler.getParentPath();
            var parentObj = xmlObjects.get(parentPath);
            objectHandler.attachToParent(parentObj, obj);
        }
        xmlObjects.remove(thisPath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void characters(char[] ch, int start, int length) {
        var thisPath = elementStack.peek();
        var handler = HANDLERS.get(thisPath);
        if (handler instanceof XmlObjectHandler objHandler) {
            var obj = objHandler.getObject();
            xmlObjects.putIfAbsent(thisPath, obj);
        } else if (handler instanceof XmlValueHandler valHandler) {
            var lastSeparator = thisPath.lastIndexOf('.');
            var parentPath = thisPath.substring(0, lastSeparator);
            var parentObj = xmlObjects.get(parentPath);
            valHandler.attachValue(parentObj, new String(ch, start, length));
        }
    }

    public DmarcFeedback getFeedback() {
        return feedback;
    }

    private static Map<String, XmlElementHandler> initializeElementHandlers() {
        var handlers = List.of(
            new XmlObjectHandler<>("feedback", DmarcFeedback::new, (p, e) -> {}),
            new XmlObjectHandler<>("feedback.report_metadata", ReportMetadata::new, DmarcFeedback::setReportMetadata),
            new XmlValueHandler<>("feedback.report_metadata.org_name", ReportMetadata::setOrgName),
            new XmlValueHandler<>("feedback.report_metadata.email", ReportMetadata::setEmail),
            new XmlValueHandler<>("feedback.report_metadata.extra_contact_info", ReportMetadata::setExtraContact),
            new XmlValueHandler<>("feedback.report_metadata.report_id", ReportMetadata::setReportId),
            new XmlObjectHandler<>("feedback.report_metadata.date_range", DateRange::new, ReportMetadata::setDateRange),
            new XmlValueHandler<>("feedback.report_metadata.date_range.begin", (DateRange dr, String s) -> dr.setBegin(epochStringToOdt(s))),
            new XmlValueHandler<>("feedback.report_metadata.date_range.end", (DateRange dr, String s) -> dr.setEnd(epochStringToOdt(s))),
            new XmlObjectHandler<>("feedback.policy_published", DmarcPolicy::new, DmarcFeedback::setPolicyPublished),
            new XmlValueHandler<>("feedback.policy_published.domain", DmarcPolicy::setDomain),
            new XmlValueHandler<>("feedback.policy_published.adkim", DmarcPolicy::setDkimAlignment),
            new XmlValueHandler<>("feedback.policy_published.aspf", DmarcPolicy::setSpfAlignment),
            new XmlValueHandler<>("feedback.policy_published.p", DmarcPolicy::setDomainPolicy),
            new XmlValueHandler<>("feedback.policy_published.sp", DmarcPolicy::setSubdomainPolicy),
            new XmlValueHandler<>("feedback.policy_published.pct", (DmarcPolicy p, String s) -> p.setPercentage(parseInt(s))),
            new XmlObjectHandler<>("feedback.record", DmarcRecord::new, DmarcFeedback::addRecord),
            new XmlObjectHandler<>("feedback.record.row", RecordRow::new, DmarcRecord::setRow),
            new XmlValueHandler<>("feedback.record.row.source_ip", RecordRow::setSourceIp),
            new XmlValueHandler<>("feedback.record.row.count", (RecordRow r, String s) -> r.setCount(parseInt(s))),
            new XmlObjectHandler<>("feedback.record.row.policy_evaluated", PolicyEvaluation::new, RecordRow::setPolicyEvaluated),
            new XmlValueHandler<>("feedback.record.row.policy_evaluated.disposition", PolicyEvaluation::setDisposition),
            new XmlValueHandler<>("feedback.record.row.policy_evaluated.dkim", PolicyEvaluation::setDkim),
            new XmlValueHandler<>("feedback.record.row.policy_evaluated.spf", PolicyEvaluation::setSpf),
            new XmlObjectHandler<>("feedback.record.identifiers", Identifiers::new, DmarcRecord::setIdentifiers),
            new XmlValueHandler<>("feedback.record.identifiers.header_from", Identifiers::setHeaderFrom),
            new XmlObjectHandler<>("feedback.record.auth_results", ArrayList<AuthResult>::new, DmarcRecord::setAuthResults),
            new XmlObjectHandler<>("feedback.record.auth_results.dkim", () -> new AuthResult(DKIM), (List<AuthResult> l, AuthResult a) -> l.add(a)),
            new XmlObjectHandler<>("feedback.record.auth_results.spf", () -> new AuthResult(SPF), (List<AuthResult> l, AuthResult a) -> l.add(a)),
            new XmlValueHandler<>("feedback.record.auth_results.dkim.domain", AuthResult::setDomain),
            new XmlValueHandler<>("feedback.record.auth_results.dkim.result", AuthResult::setResult),
            new XmlValueHandler<>("feedback.record.auth_results.dkim.selector", AuthResult::setSelector),
            new XmlValueHandler<>("feedback.record.auth_results.spf.domain", AuthResult::setDomain),
            new XmlValueHandler<>("feedback.record.auth_results.spf.result", AuthResult::setResult)
        );
        return handlers.stream()
            .collect(toUnmodifiableMap(XmlElementHandler::getPath, identity()));
    }

    private static OffsetDateTime epochStringToOdt(String string) {
        var epoch = parseLong(string);
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }
}
