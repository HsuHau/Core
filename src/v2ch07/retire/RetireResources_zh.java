package v2ch07.retire;

import java.awt.*;

public class RetireResources_zh extends java.util.ListResourceBundle{
    public static final Object[][] contents = {
    // BEGIN LOCALIZE
        {"colorPre", Color.red}, {"colorGain", Color.blue}, {"colorLoss", Color.yellow}
    // END LOCALIZE
    };

    public Object[][] getContents() {
        return contents;
    }
}
