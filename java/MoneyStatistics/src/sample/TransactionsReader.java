package sample;

import javafx.geometry.Point2D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class TransactionsReader {

    private BufferedReader bufferedReader;

    public TransactionsReader(String s) {
        InputStream stream = getClass().getResourceAsStream(s);
        bufferedReader = new BufferedReader(new InputStreamReader(stream));
    }

    public List<Point2D> read() {
        ArrayList<Point2D> list = new ArrayList<>();

        for ( String line = readLine() ; ; line = readLine()) {
            if ("".equals(line))
                continue;
            if (line == null)
                break;

            String[] values = line.split(";");

            int     dato   = Integer.parseInt(values[0].replaceAll("[-\"]", ""));
            String rawBetalt = values[5];

            double  betalt = rawBetalt.isEmpty() ? 0.0 : Double.parseDouble(rawBetalt);
            list.add(new Point2D(dato, betalt));
        }

        list.forEach(point2D -> System.out.println(point2D.getX() + " " + point2D.getY()));

        return list;

    }

    private String readLine() {
        String line = null;
        try {
            line = bufferedReader.readLine();
            if (line != null && !line.matches("^\"20.*")) {  //Not interesting
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
