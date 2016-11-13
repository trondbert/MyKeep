package trond.java;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class DepthFirstSearchTest {

    @Before
    public void setup() {
        String cwd = System.getProperty("user.dir");
        File testRoot = Paths.get(cwd, "testRoot").toFile();
        recursiveDelete(testRoot);
    }

    private void recursiveDelete(File dir) {
        if (!dir.exists()) return;

        for (File subDir : dir.listFiles(File::isDirectory)) {
            recursiveDelete(subDir);
        }
        dir.delete();
    }

    @Test
    public void depth_first_easy() throws Exception {
        String cwd = System.getProperty("user.dir");

        Paths.get(cwd, "testRoot", "dirA").toFile().mkdirs();
        Paths.get(cwd, "testRoot", "dirB").toFile().mkdirs();
        Paths.get(cwd, "testRoot", "dirC").toFile().mkdirs();

        testImplementationEasy(cwd, new DepthFirstSearch.Iterative());
        testImplementationEasy(cwd, new DepthFirstSearch.Recursive());
    }

    private void testImplementationEasy(String cwd, DepthFirstSearch implementation) {
        List<File> files = implementation.listDirectories(Paths.get(cwd, "testRoot").toFile());

        Assert.assertArrayEquals(new File[]{
                Paths.get(cwd, "testRoot", "dirA").toFile(),
                Paths.get(cwd, "testRoot", "dirB").toFile(),
                Paths.get(cwd, "testRoot", "dirC").toFile()}, files.toArray());
    }

    @Test
    public void depth_first_hard() throws Exception {
        String cwd = System.getProperty("user.dir");

        Paths.get(cwd, "testRoot", "dirA").toFile().mkdirs();
        Paths.get(cwd, "testRoot", "dirB", "dirB.A").toFile().mkdirs();
        Paths.get(cwd, "testRoot", "dirB", "dirB.B").toFile().mkdirs();
        Paths.get(cwd, "testRoot", "dirB", "dirB.C", "dirB.C.A").toFile().mkdirs();
        Paths.get(cwd, "testRoot", "dirC", "dirC.A", "dirC.A.A").toFile().mkdirs();

        testImplementationHard(cwd, new DepthFirstSearch.Iterative());
        testImplementationHard(cwd, new DepthFirstSearch.Recursive());
    }

    private void testImplementationHard(String cwd, DepthFirstSearch implementation) {
        List<File> files = implementation.listDirectories(Paths.get(cwd, "testRoot").toFile());

        Assert.assertArrayEquals(new File[]{
                Paths.get(cwd, "testRoot", "dirA").toFile(),
                Paths.get(cwd, "testRoot", "dirB", "dirB.A").toFile(),
                Paths.get(cwd, "testRoot", "dirB", "dirB.B").toFile(),
                Paths.get(cwd, "testRoot", "dirB", "dirB.C", "dirB.C.A").toFile(),
                Paths.get(cwd, "testRoot", "dirB", "dirB.C").toFile(),
                Paths.get(cwd, "testRoot", "dirB").toFile(),
                Paths.get(cwd, "testRoot", "dirC", "dirC.A", "dirC.A.A").toFile(),
                Paths.get(cwd, "testRoot", "dirC", "dirC.A").toFile(),
                Paths.get(cwd, "testRoot", "dirC").toFile()
        }, files.toArray());
    }

    @Test
    public void depth_first_empty() throws Exception {
        String cwd = System.getProperty("user.dir");

        Paths.get(cwd, "testRoot").toFile().mkdirs();

        testImplementationEmpty(cwd, new DepthFirstSearch.Iterative());
        testImplementationEmpty(cwd, new DepthFirstSearch.Recursive());
    }

    private void testImplementationEmpty(String cwd, DepthFirstSearch implementation) {
        List<File> files = implementation.listDirectories(Paths.get(cwd, "testRoot").toFile());

        Assert.assertTrue(files.isEmpty());
    }
}
