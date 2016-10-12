package trond.java.files.organising;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author trond.
 */
public class Duplicates {

    static ArrayList<Path> allFiles;
    static Predicate<Path> isDuplicate;
    static Predicate<Path> isNotDuplicate;
    static Predicate<Path> doesNotExist;

    public static void main(final String[] args) throws Exception {
        getAllFilesRecursively(Paths.get("/Volumes/MY PASSPORT/Filmklipp_alle/"),
                               (allFiles = new ArrayList<>()));
        isDuplicate = file -> {
            final Stream<Path> equalFiles = allFiles.stream().filter((f) -> areEqual(file, f));
            return equalFiles.count() > 1;
        };
        isNotDuplicate = file -> {
            final Stream<Path> equalFiles = allFiles.stream().filter((f) -> areEqual(file, f));
            return equalFiles.count() <= 1;
        };
        doesNotExist = file -> {
            final Stream<Path> equalFiles = allFiles.stream().filter((f) -> areEqual(file, f));
            return equalFiles.count() < 1;
        };

        final List<Path> result = new ArrayList<>();
        wrap(() -> {
            findMatches(Paths.get("/Volumes/MY PASSPORT/Dans/Balboa/StudioHop 2015/Disk 1").toString(), doesNotExist);
        });

        //findAllDups();
    }

    private static void findMatches(final String directory, final Predicate<Path> predicate) throws IOException {
        final List<Path> files =
                Files.list(Paths.get(directory))
                     .collect(toList());

        final List<Path> matchingFiles = files.stream().filter(predicate).collect(toList());

        matchingFiles.forEach( (matching) -> {
            System.out.println(matching);
            allFiles.stream().filter((f) -> areEqual(f, matching))
                    .forEach( (dup) -> System.out.println("- " + dup));
        });
        System.out.println(matchingFiles.size() + " matches");
    }

    private static void findAllDups() throws IOException {
        final List<Path> result = new ArrayList<>();
        getAllDirectoriesRecursively(Paths.get("/Users/trond/Documents/Bilder_alle/"), result);
        result.forEach((dir) -> {
            wrap(()-> Files.list(dir).filter(isDuplicate).forEach(System.out::println));
        });
    }

    private static boolean areEqual(final Path file1, final Path file2) {
        try {
            return file2.getFileName().equals(file1.getFileName()) &&
                    Files.getAttribute(file1, "size").equals(Files.getAttribute(file2, "size"));
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> T wrap(final ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void wrap(final ThrowingRunnable runnable) {
        try {
            runnable.run();
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void getAllFilesRecursively(final Path directory, final List<Path> result) throws IOException {
        result.addAll(
            Files.list(directory).filter(Files::isRegularFile).collect(toList()));

        Files.list(directory).filter(Files::isDirectory).forEach((dir) ->
                wrap(() -> getAllFilesRecursively(dir, result))
        );
    }

    public static void getAllDirectoriesRecursively(final Path directory, final List<Path> result) throws IOException {
        result.add(directory);
        Files.list(directory).filter(Files::isDirectory).forEach((dir) ->
                                                                         wrap(() -> getAllDirectoriesRecursively(dir, result))
        );
    }
}

interface ThrowingSupplier<T> {
    T get() throws Exception;
}

interface ThrowingRunnable {
    void run() throws Exception;
}
