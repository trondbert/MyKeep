package stablemarriages;

import static java.util.Arrays.asList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author trond.
 */
public class StableMarriages {

    enum Man    { ARE, BIRGER, CALLE, DAVID, ENDRE, FRANK, GEIR, HANS }
    enum Woman  { ANNE, BENTE, CARO,  DINA,  ELLEN, FANNY, GURI, HARRIET }

    final Map<Enum, List<Enum>> preferences;

    public StableMarriages(final Map<Enum, List<Enum>> preferences) {
        this.preferences = preferences;
    }

    public static void main(final String[] args) {
        try {
            final Map<Enum, List<Enum>> preferences = createRandomPreferences();
            new StableMarriages(preferences).run();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void run() throws Exception {
        match(Woman.class);
        match(Man.class);
    }

    private static Map<Enum, List<Enum>> createRandomPreferences() {
        return new HashMap<Enum, List<Enum>>() {{
            for (final Man man : Man.values())
                put(man, randomize(Woman.values()));
            for (final Woman woman : Woman.values())
                put(woman, randomize(Man.values()));
        }};
    }

    private void match(final Class<? extends Enum> suitorClass) throws Exception {
        final Map<Enum, Integer> status = new HashMap<>();
        final Class<? extends Enum> proposeeClass = suitorClass.equals(Woman.class)? Man.class : Woman.class;
        for (final Man man : Man.values()) {
            status.put(man, suitorClass == Man.class? -1 : Integer.MAX_VALUE);
        }
        for (final Woman woman : Woman.values()) {
            status.put(woman, suitorClass == Woman.class? -1 : Integer.MAX_VALUE);
        }

        final List<Enum> proposers = asList((Enum[]) suitorClass.getDeclaredMethod("values").invoke(null));
        final List<Enum> suitors = new ArrayList<>();
        suitors.addAll(proposers);

        while (!suitors.isEmpty()) {
            runProposals(status, suitors);
        }

        proposers.forEach( s -> {
            final Enum fiance = preferences.get(s).get(status.get(s));
            final Stream<String> names = preferences.get(s).stream().map(option -> option.equals(fiance)?
                                                                                   option.toString() :
                                                                                   option.toString().substring(0, 1));
            System.out.println(s.toString() + ", " + names.collect(Collectors.joining(", ")));
        });
        final List<Enum> proposees = proposeeClass.equals(Man.class) ? asList(Man.values()) : asList(Woman.values());
        proposees.forEach( s -> {
            final Enum fiance = preferences.get(s).get(status.get(s));
            final Stream<String> names = preferences.get(s).stream().map(option -> option.equals(fiance)?
                                                                                   option.toString() :
                                                                                   option.toString().substring(0, 1));
            System.out.println(s.toString() + ", " + names.collect(Collectors.joining(", ")));
        });
    }

    private void runProposals(final Map<Enum, Integer> status, final List<Enum> suitors) {
        final List<Enum> justEngaged = new ArrayList<>();
        final List<Enum> justBrokenUp = new ArrayList<>();

        for (final Enum suitor : suitors) {
            final Enum target = preferences.get(suitor).get(status.get(suitor) + 1);
            final List<Enum> ranking = preferences.get(target);
            final int rankingWithTarget = ranking.indexOf(suitor);
            final Integer currentFiance = status.get(target);

            if (rankingWithTarget < currentFiance) {
                if (currentFiance < ranking.size())
                    justBrokenUp.add(ranking.get(currentFiance));

                System.out.println(target.toString() + " accepted " + suitor.toString());
                status.put(target, rankingWithTarget);
                justEngaged.add(suitor);
            }
            else {
                System.out.println(target.toString() + " rejected " + suitor.toString());
            }
            status.put(suitor, status.get(suitor) + 1);
        }
        justEngaged.forEach(suitors::remove);
        justBrokenUp.forEach(suitors::add);
    }

    private static List<Enum> randomize(final Enum[] values) {
        final ArrayList<Enum> result = new ArrayList<>();
        result.addAll(asList(values));
        final int size = result.size();

        for (int i = 0; i < 100; i++) {
            final Enum object = result.remove(new Random().nextInt(size));
            result.add(new Random().nextInt(size-1), object);
        }
        return result;
    }
}
