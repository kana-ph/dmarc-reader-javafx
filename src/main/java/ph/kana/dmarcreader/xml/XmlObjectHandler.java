package ph.kana.dmarcreader.xml;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

class XmlObjectHandler<P, E> implements XmlElementHandler {
    private final String path;
    private final Supplier<E> objSupplier;
    private final BiConsumer<P, E> parentAttach;

    public XmlObjectHandler(String path, Supplier<E> objSupplier, BiConsumer<P, E> parentAttach) {
        this.path = path;
        this.objSupplier = objSupplier;
        this.parentAttach = parentAttach;
    }

    @Override
    public String getPath() {
        return path;
    }

    public E getObject() {
        return objSupplier.get();
    }

    public String getParentPath() {
        var lastDotIndex = path.lastIndexOf('.');
        return path.substring(0, lastDotIndex);
    }

    public void attachToParent(P parent, E element) {
        parentAttach.accept(parent, element);
    }
}
