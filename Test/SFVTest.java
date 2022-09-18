import SFV.*;
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
        CheckSfv oSfv = new CheckSfv(generateFilenames("\\Test\\testfile.sfv"));
        oSfv.check();
        Assertions.assertEquals(2, oSfv.getCrcOk());
        Assertions.assertEquals(1, oSfv.getCrcFail());
        Assertions.assertEquals(1, oSfv.getCrcMiss());
        Assertions.assertEquals(4, oSfv.getTotal());
    }

    @Test
    public void testFilenameCRC() throws IOException {
        String[] str = new String[3];
        str[0] = generateFilenames("\\Test\\testfile [A458F50D].txt");
        str[1] = generateFilenames("\\Test\\testfilebig [C8029225].txt");
        str[2] = generateFilenames("\\Test\\testfile [GAGAGUGU].txt");

        CheckFile oSfv = new CheckFile();
        oSfv.set_files(str);

        oSfv.check();
        Assertions.assertEquals(1, oSfv.getNoCRC());
        Assertions.assertEquals(2, oSfv.getCrcOk());
        Assertions.assertEquals(3, oSfv.getTotal());
    }

    @Test
    public void testCreateAndValidate() throws IOException {
        String testFilename = "_Test.sfv";
        String delFile = getCurrentPath() + "\\Test\\" + testFilename;
        File file = new File(delFile);
        if (file.isFile()) {
            Assertions.assertTrue(file.delete());
        }

        CreateSfv cSfv = (CreateSfv) SfvObjFab.get("create");
        Assertions.assertTrue(cSfv instanceof CreateSfv, "cSfv ist not an instance of CreateSfv");
        cSfv.set_comment("This is a test");

        String[] str = new String[1];
        str[0] = generateFilenames("\\Test\\testfilebig [C8029225].txt");
        cSfv.set_files(str);

        System.out.println("Create SFV...");
        cSfv.create();
        System.out.println("");

        CheckSfv oSfv = new CheckSfv(generateFilenames("\\Test\\" + testFilename));
        oSfv.check();
        Assertions.assertEquals(1, oSfv.getCrcOk());
        Assertions.assertEquals(0, oSfv.getCrcFail());
        Assertions.assertEquals(0, oSfv.getCrcMiss());
        Assertions.assertEquals(1, oSfv.getTotal());

        if (file.isFile()) {
            boolean res = file.delete();
            Assertions.assertTrue(res);
        }
    }

    @Test
    public void testDebug() throws IOException {
        CreateSfv cSfv = (CreateSfv) SfvObjFab.get("create");
        Assertions.assertTrue(cSfv instanceof CreateSfv, "cSfv ist not an instance of CreateSfv");

        cSfv.set_comment("This is a test");
        cSfv.set_files("N:\\Games\\emu\\SNES\\");
        cSfv.set_sfv(generateFilenames("\\Test\\test.sfv"));
        System.out.println("Create SFV...");
        cSfv.create();
        System.out.println("");

        CheckSfv oSfv = new CheckSfv(generateFilenames("\\Test\\test.sfv"));
        oSfv.check();
    }

    @Test
    public void testDebug2() throws IOException {
        CheckSfv oSfv = new CheckSfv(generateFilenames("\\Test\\test.sfv"));
        oSfv.check();
    }
}
