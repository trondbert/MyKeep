package trond.java;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class DepthFirstSearch {

    public abstract List<File> listDirectories(File rootDir);

    public static class Iterative extends DepthFirstSearch {

        public List<File> listDirectories(File rootDir) {
            List<File> visitList = new ArrayList<>();
            List<File> visitedDirs = new ArrayList<>();
            List<File> result = new ArrayList<>();
            visitList.addAll(asList(rootDir.listFiles(File::isDirectory)));

            while (!visitList.isEmpty()) {
                File currentDir = visitList.get(0);
                if (visitedDirs.contains(currentDir)) {
                    result.add(currentDir);
                    visitList.remove(0);
                }
                else {
                    visitedDirs.add(currentDir);
                    visitList.addAll(0, asList(currentDir.listFiles(File::isDirectory)));
                }
            }
            return result;
        }
    }

    public static class Recursive extends DepthFirstSearch {

        public List<File> listDirectories(File rootDir) {
            ArrayList<File> result = new ArrayList<>();
            for(File subDir : rootDir.listFiles(File::isDirectory)) {
                visit(subDir, result);
            }
            return result;
        }

        private void visit(File dir, List<File> result) {
            for(File subDir : dir.listFiles(File::isDirectory)) {
                visit(subDir, result);
            }
            result.add(dir);
        }
    }
}
