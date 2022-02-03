package ph.kana.dmarcreader.xml;

import org.xml.sax.SAXException;
import ph.kana.dmarcreader.model.DmarcFeedback;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class DmarcXmlParser {

    private final SAXParser saxParser = initializeSaxParser();

    public DmarcFeedback parse(InputStream xmlInputStream) {
        try {
            var handler = new DmarcXmlDocumentHandler();
            saxParser.parse(xmlInputStream, handler);
            return handler.getFeedback();
        } catch (SAXException | IOException e) {
            throw new DmarcXmlException("Failed to parse XML stream!", e);
        }
    }

    private static SAXParser initializeSaxParser() {
        try {
            return SAXParserFactory.newInstance()
                    .newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new DmarcXmlException("Cannot initialize SAX parser!", e);
        }
    }
}
