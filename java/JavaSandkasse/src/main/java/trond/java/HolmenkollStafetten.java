package trond.java;

import static trond.java.Matcher.eq;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author trond.
 */
public class HolmenkollStafetten {

    List<HolmenkollenResultat> results;

    public static void main(final String[] args) {
        try {
            System.setIn(new FileInputStream("/tmp/results.csv"));
        }
        catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        final HolmenkollStafetten stafetten = new HolmenkollStafetten();
        stafetten.init();
        HolmenkollStafetten.solve(stafetten);
    }

    private static void solve(final HolmenkollStafetten stafetten) {

        final List<HolmenkollenResultat> a2Ok = stafetten.getResults()
                                                         .filter(it -> it.getTotalTime() != null && it.getDivision().equals("A2"))
                                                         .collect(Collectors.toList());

        System.out.println(a2Ok.get(0).getStartNumber());
        System.out.println(a2Ok.get(0).getTotalTime());
        System.out.println(a2Ok.size());

        final ToLongFunction<HolmenkollenResultat> func = (res) -> res.getTotalTime().toMillis();
        final Long sumTotalTime = a2Ok.stream().collect(Collectors.summingLong(func));
        final long avgTime = sumTotalTime / a2Ok.size();

        final Optional<HolmenkollenResultat> anjaOgDe = stafetten
                .filter(a2Ok.stream(), HolmenkollenResultat::getStartNumber, eq("7234")).findFirst();
        System.out.println(anjaOgDe.get().getTotalTime());
        System.out.println(Duration.ofMillis(avgTime));

        a2Ok.forEach((lag) -> System.out.println(lag.getStartNumber() + ";" + lag.getTotalTime().toMillis() / 1000));
    }

    public void init() {
        final Scanner scanner = new Scanner(System.in);
        final List<String> headers = readHeaders(scanner);

        results = readResults(scanner, headers);
    }

    private List<String> readHeaders(final Scanner scaner) {
        final String[] split = scaner.nextLine().split(";");
        final List<String> headers = new ArrayList<>();

        for (final String aSplit : split) {
            final String header = clean(aSplit);
            headers.add(header);
        }
        return headers;
    }

    private List<HolmenkollenResultat> readResults(final Scanner scanner, final List<String> headers) {
        final List<HolmenkollenResultat> list = new ArrayList<>();

        while (scanner.hasNext()) {
            final String[] split = scanner.nextLine().split(";");

            final Map<String, String> resultMap = new HashMap<>();
            for (int i = 0; i < split.length; i++) {
                resultMap.put(headers.get(i), clean(split[i]));
            }
            list.add(new HolmenkollenResultat(resultMap));
        }
        return list;
    }

    <T> Stream<HolmenkollenResultat> filter(final Stream<HolmenkollenResultat> inStream,
                                            final Function<HolmenkollenResultat, T> func, final Matcher matcher) {
        return inStream.filter(it -> matcher.matches(func.apply(it)));
    }

    private String clean(final String aSplit) {
        return aSplit.replace("\"", "").replace("\uFEFF", "");
    }

    public Stream<HolmenkollenResultat> getResults() {
        return results.stream();
    }
}
