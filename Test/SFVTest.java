import SFV.CheckFile;
import SFV.CheckSfv;
import SFV.CreateSfv;
import SFV.ICheck;
import SFV.ICreate;
import SFV.SFV;
import SFV.SfvObjFab;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SFVTest {

    private String getCurrentPath() {
        return Paths.get("").toAbsolutePath().toString();
    }

    private String generateFilenames(String filename) {
        return getCurrentPath() + filename;
    }

    @Test
    public void testSFV() throws IOException {
        ICheck oSfv = new CheckSfv(generateFilenames("/Test/testfile.sfv"));
        oSfv.check();
        Assertions.assertEquals(2, ((SFV) oSfv).getCrcOk());
        Assertions.assertEquals(1, ((SFV) oSfv).getCrcFail());
        Assertions.assertEquals(1, ((SFV) oSfv).getCrcMiss());
        Assertions.assertEquals(4, ((SFV) oSfv).getTotal());
    }

    @Test
    public void testFilenameCRC() throws IOException {
        String[] str = new String[2];
        str[0] = generateFilenames("/Test/testfile [A458F50D].txt");
        str[1] = generateFilenames("/Test/testfilebig [C8029225].txt");

        ICheck oSfv = new CheckFile();
        ((SFV) oSfv).set_files(str);

        oSfv.check();
        Assertions.assertEquals(2, ((SFV) oSfv).getCrcOk());
        Assertions.assertEquals(2, ((SFV) oSfv).getTotal());
    }

    @Test
    public void testCreate() throws IOException {
        String testFilename = "_Test.sfv";
        String delFile = getCurrentPath() + "/Test/" + testFilename;
        File file = new File(delFile);
        if (file.isFile()) {
            boolean res = file.delete();
            Assertions.assertTrue(res);
        }

        ICreate cSfv = SfvObjFab.get("create");
        Assertions.assertTrue(cSfv instanceof CreateSfv);
        ((CreateSfv) cSfv).set_comment("This is a test");

        String[] str = new String[1];
        str[0] = generateFilenames("/Test/testfilebig [C8029225].txt");
        ((SFV) cSfv).set_files(str);

        System.out.println("Create SFV...");
        cSfv.create();
        System.out.println("");

        ICheck oSfv = new CheckSfv(generateFilenames("/Test/" + testFilename));
        oSfv.check();
        Assertions.assertEquals(1, ((SFV) oSfv).getCrcOk());
        Assertions.assertEquals(0, ((SFV) oSfv).getCrcFail());
        Assertions.assertEquals(0, ((SFV) oSfv).getCrcMiss());
        Assertions.assertEquals(1, ((SFV) oSfv).getTotal());

        if (file.isFile()) {
            boolean res = file.delete();
            Assertions.assertTrue(res);
        }
    }
}
