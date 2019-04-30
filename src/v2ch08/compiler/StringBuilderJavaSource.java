package v2ch08.compiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class StringBuilderJavaSource extends SimpleJavaFileObject {
    private StringBuilder code;

    /**
     * Constructs a new StringBuilderJavaSource.
     * @param name the name of the source file represented by this file object
     */
    public StringBuilderJavaSource(String name) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        code = new StringBuilder();
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }

    public void append(String str) {
        code.append(str);
        code.append('\n');
    }
}
