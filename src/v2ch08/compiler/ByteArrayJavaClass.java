package v2ch08.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class ByteArrayJavaClass extends SimpleJavaFileObject {
    private ByteArrayOutputStream stream;

    /**
     * Constructs a new ByteArrayJavaClass
     * @param name the name of the class file represented by this file object
     */
    public ByteArrayJavaClass(String name) {
        super(URI.create("bytes:///" + name), Kind.CLASS);
        stream = new ByteArrayOutputStream();
    }

    public OutputStream openOutputStream() throws IOException {
        return stream;
    }

    public byte[] getBytes() {
    return stream.toByteArray();
    }
}
