import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsReader {

    private static final int ERR_NO_INPUT_DIRECTORY = 1;
    private static final int ERR_NO_INPUT_FILES = 2;

    public static void main(String[] args) {
        try (BufferedWriter resultWriter = new BufferedWriter(new FileWriter("result.neo"))) {
            File resourcesDir = new File("resources");
            if (!resourcesDir.exists() || !resourcesDir.isDirectory()) {
                System.exit(ERR_NO_INPUT_DIRECTORY);
            }
            File[] files = resourcesDir.listFiles();
            if (files == null) {
                System.exit(ERR_NO_INPUT_FILES);
            }
            for (File file : files) {
                if (file.getName().matches(".*\\.html")) {
                    System.out.println("//" + file.getAbsolutePath());
                    interpret(file, resultWriter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void interpret(File file, BufferedWriter result) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = null;
            List<String> directors = findDirectors(br);

            List<String> actors = findActors(br);
            for (String director : directors) {
                for (String actor : actors) {
                    result.write(("MERGE (d:Person {name: '$director'}) " +
                            "MERGE (a:Person {name: '$actor'}) " +
                            "CREATE (d)-[:DIRECTED]->(a);\n")
                            .replaceAll("\\$director", director)
                            .replaceAll("\\$actor", actor));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> findDirectors(BufferedReader br) throws IOException {
        String line;
        boolean directorLineFound = false;
        List<String> directors = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.matches(".*Directors?:.*")) {
                directorLineFound = true;
                continue;
            }
            if (directorLineFound) {
                if (line.matches(".*</div>.*")) {
                    return directors;
                }
                String name = extractName(line);
                if (name != null) {
                    directors.add(prepareName(name));
                }
            }
        }
        return directors;
    }

    private static List<String> findActors(BufferedReader br) throws IOException {
        String line;
        List<String> actors = new ArrayList<>();
        boolean actorsFound = false;
        while ((line = br.readLine()) != null) {
            if (line.contains("Stars:")) {
                actorsFound = true;
                continue;
            }
            if (actorsFound) {
                if (line.matches(".*</div>.*")) {
                    return actors;
                }
                String name = extractName(line);
                if (name != null) {
                    actors.add(prepareName(name));
                }
            }
        }
        return actors;
    }

    private static String prepareName(String name) {
        return name.replaceAll("'", "`").trim();
    }

    private static String extractName(String line) {
        String stuffInAngleBrackets = ".*>([A-z][^<>|]+[A-z])<.*";
        String plaintext1 = "^[\\w\\.\\s]+,?\\s*";
        String plaintext2 = "^[\\w\\.\\s]+\\s*</div>";
        if (line.matches(stuffInAngleBrackets) && !line.matches("See full cast")) {
            return line.replaceAll(stuffInAngleBrackets, "$1");
        }
        else if (line.matches(plaintext1) || line.matches(plaintext2)) {
            return line.replaceAll(",", "").replaceAll("</div>", "");
        }
        return null;
    }
}
