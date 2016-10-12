import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.READ;

public class ReadOpenDataNorway {

    public static void main(String[] args) {
        try {
            String url = "http://hotell.difi.no/api/json/kmd/partistotte/2014?";
            InputStream inputStream = getJsonDataStream(url);

            Map<String, Object> data = new ObjectMapper().readValue(inputStream, Map.class);

            List<Map<String, Object>> entries = (List<Map<String, Object>>) data.get("entries");
            entries.forEach(entry -> System.out.println(entry.get("partinamn")));

        } catch (IOException e) {
            throw new RuntimeException("Bad!", e);
        }
    }

    private static void readOpenDatasets() {
        try {
            String page = "1";
            String url = "http://data.norge.no/api/dcat/data.json?page=" + page;
            InputStream inputStream = getJsonDataStream(url);

            OpenData openData = new ObjectMapper().readValue(inputStream, OpenData.class);

            openData.datasets.stream().forEach(dataSet -> {
                System.out.println(dataSet.title);
                System.out.println(dataSet.distribution.get(0).get("accessURL"));
            });
        } catch (IOException e) {
            throw new RuntimeException("Bad!", e);
        }
    }

    private static InputStream getJsonDataStream(String url) throws IOException {
        URL openDataSite = new URL(url);
        return openDataSite.openConnection().getInputStream();
    }

    private static InputStream getJsonDataStreamForTest() throws IOException {
        return Files.newInputStream(Paths.get("./src/main/resources/opendata.json"),READ);
    }
}

class OpenData {

    public List<DataSet> datasets;
    public String title;
    public String homepage;

}

class DataSet {
    public String id;
    public String title;
    public List<Description> description;
    public String landingPage;
    public String issued;
    public String modified;
    public List<String> language;
    public Publisher publisher;
    public List<String> keyword;
    public List<Map<String, Object>> distribution;
}

class Description {
    public String language;
    public String value;
}

class Publisher {
    public String name;
    public String mbox;
}