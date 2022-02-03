package ph.kana.dmarcreader.xml;

import java.util.function.BiConsumer;

class XmlValueHandler<E> implements XmlElementHandler {
    private final String path;
    private final BiConsumer<E, String> attachFunction;

    public XmlValueHandler(String path, BiConsumer<E, String> attachFunction) {
        this.path = path;
        this.attachFunction = attachFunction;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void attachValue(E element, String value) {
        attachFunction.accept(element, value);
    }
}
