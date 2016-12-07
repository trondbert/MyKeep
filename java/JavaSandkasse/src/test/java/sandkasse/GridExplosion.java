package sandkasse;

import org.hibernate.persister.entity.OuterJoinLoadable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GridExplosion {

    static final int ROWCOUNT = 10;
    static final int COLCOUNT = 10;
    Group[][] board = new Group[ROWCOUNT][COLCOUNT];
    boolean[][] visited = new boolean[ROWCOUNT][COLCOUNT];
    private int rowCount = 10;
    private int colCount = 10;

    Map<Integer, Faction> factions = new HashMap<>();

    public static void main(String[] args) {
        GridExplosion gridExplosion = new GridExplosion();
        gridExplosion.setup();
        gridExplosion.printBoard(gridExplosion::cellSymbol);

        gridExplosion.explode(1);
    }
    void explode(int groupId) {
        Faction faction = factions.get(groupId);
        Map<String, List<Outlier>> newOutliers = explode(faction);

        System.out.println();
        printBoard((r, c) -> this.cellSymbol(r, c, newOutliers));

        for (List<Outlier> outliers : newOutliers.values()) {
            if (outliers.size() > 1) {
                Stream<Group> distinctGroups = outliers.stream().map(o -> o.group).distinct();
                if (distinctGroups.count() > 1) { // join
                    System.out.println(outliers.get(0).row + "," + outliers.get(0).col);
                }
                else {


                }
            }
        }
        // with new outliers, join and split according to the groups

        //dividetheothergroup and join current group
        //rename current group to signify empty
    }

    private Map<String, List<Outlier>> explode(Faction faction) {
        Map<String, List<Outlier>> outlierMap = new HashMap<>();
        for (Group group : faction.groups) {
            for (Outlier outlier : group.outliers) {
                int col = outlier.col, row = outlier.row;
                int here = board[row][col].number;
                int west  = col > 0          ? board[row][col-1].number : here;
                int east  = col < colCount-1 ? board[row][col+1].number : here;
                int north = row > 0          ? board[row-1][col].number : here;
                int south = row < rowCount-1 ? board[row+1][col].number : here;
                if (west != here) {
                    addToOutlierMap(new Outlier(row, col - 1, group), outlierMap);
                }
                if (east != here) {
                    addToOutlierMap(new Outlier(row, col + 1, group), outlierMap);
                }
                if (north != here) {
                    addToOutlierMap(new Outlier(row - 1, col, group), outlierMap);
                }
                if (south != here) {
                    addToOutlierMap(new Outlier(row + 1, col, group), outlierMap);
                }
            }
        }
        return outlierMap;
    }

    private void addToOutlierMap(Outlier outlier, Map<String, List<Outlier>> outliersMap) {
        String key = outlier.row + "," + outlier.col;
        outliersMap.putIfAbsent(key, new ArrayList<>());
        outliersMap.get(key).add(outlier);
    }

    private void setup() {
        factions.put(1, new Faction(1));
        factions.put(2, new Faction(2));

        Group g1 = Group.newGroup(factions.get(1));
        Group g2 = Group.newGroup(factions.get(2));

        board[0] = new Group[]{g1,g1,g1,g1,g2,g2,g2,g2,g2,g2};
        board[1] = new Group[]{g1,g1,g1,g2,g2,g2,g2,g2,g2,g2};
        board[2] = new Group[]{g1,g1,g1,g2,g2,g2,g2,g2,g2,g2};
        board[3] = new Group[]{g1,g1,g1,g1,g1,g2,g2,g2,g2,g2};
        board[4] = new Group[]{g1,g1,g1,g1,g2,g2,g2,g2,g2,g2};
        board[5] = new Group[]{g2,g2,g1,g2,g2,g2,g2,g2,g2,g2};
        board[6] = new Group[]{g2,g2,g1,g2,g2,g2,g2,g2,g2,g2};
        board[7] = new Group[]{g2,g2,g1,g2,g2,g2,g2,g2,g2,g2};
        board[8] = new Group[]{g2,g2,g1,g2,g2,g2,g2,g2,g2,g2};
        board[9] = new Group[]{g2,g2,g1,g2,g2,g2,g2,g2,g2,g2};

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                if (visited[row][col]) continue;

                Group group = board[row][col];
                visit(row, col, group);
            }
        }
        for (Outlier o : g1.outliers) {
            System.out.println(o.row + "," + o.col);
        }
    }

    private void visit(int row, int col, Group group) {
        if (visited[row][col]) return;
        if (board[row][col] != group) return;

        visited[row][col] = true;
        if (!allNeighborsAreInGroup(row, col)) {
            Outlier outlier = new Outlier(row, col, group);
            group.outliers.add(outlier);
            Outlier.put(row, col, outlier);
        }
        if (row > 0) visit(row-1, col, group);
        if (col < colCount-1) visit(row, col+1, group);
        if (row < rowCount-1) visit(row+1, col, group);
        if (col > 0) visit(row, col-1, group);
    }

    private boolean allNeighborsAreInGroup(int row, int col) {
        int here = board[row][col].number;
        int west  = col > 0          ? board[row][col-1].number : here;
        int east  = col < colCount-1 ? board[row][col+1].number : here;
        int north = row > 0          ? board[row-1][col].number : here;
        int south = row < rowCount-1 ? board[row+1][col].number : here;

        return north == here && south == here && east == here && west == here;
    }

    private void printBoard(BiFunction<Integer, Integer, String> cellValueLogic) {
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                String cell = cellValueLogic.apply(row, col);
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private String cellSymbol(int row, int col) {
        Faction faction = board[row][col].faction;
        String cell;
        Outlier outlier = Outlier.get(row, col);
        if (outlier != null && outlier.group.faction == faction)
            cell = String.valueOf(2+faction.number);
        else
            cell = String.valueOf(faction.number);
        return cell;
    }

    private String cellSymbol(int row, int col, Map<String, List<Outlier>> outliers) {
        int factionNumber = board[row][col].faction.number;
        String[] factionNames = new String[] {"a", "b", "c"};
        List<Outlier> reduced = outliers.values().stream().reduce(new ArrayList<Outlier>(), (li, lis) -> {
            li.addAll(lis);
            return li;
        });

        Optional<Outlier> outlier = reduced.stream().filter(o -> o.row == row && o.col == col).findFirst();
        if (outlier.isPresent())
            return factionNames[outlier.get().group.faction.number];
        else
            return String.valueOf(factionNumber);
    }
}

class Outlier {
    public final int row;
    public final int col;
    public final Group group;

    private static Map<String, Outlier> instances = new HashMap<>();

    Outlier(int row, int col, Group group) {
        this.row = row;
        this.col = col;
        this.group = group;
    }

    static void put(int row, int col, Outlier outlier) {
        instances.put(row+","+col, outlier);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Outlier outlier = (Outlier) o;

        if (row != outlier.row) return false;
        return col == outlier.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    public static Outlier get(int row, int col) {
        return instances.get(row+","+col);
    }
}

class Faction {
    List<Group> groups = new ArrayList<>();
    public final int number;

    Faction(int number) {
        this.number = number;
    }

    Group addGroup() {
        Group group = Group.newGroup(this);
        groups.add(group);
        return group;
    }

    public int groupSize() {
        return groups.size();
    }

    public Group group(int i) {
        return groups.get(i);
    }

    Optional<Group> groupThat(Predicate<Group> condition) {
        return groups.stream().filter(condition).findFirst();
    }

}

class Group {

    static Map<Integer, Group> groupMap = new HashMap<>();

    public final int number;
    public final Faction faction;

    List<Outlier> outliers = new ArrayList<>();

    public Group(int number, Faction faction) {
        this.number = number;
        this.faction = faction;
    }

    static Group newGroup(Faction faction) {
        int number;
        do {
            number = new Random().nextInt(20000);
        } while (groupMap.containsKey(number));

        Group group = new Group(number, faction);
        groupMap.put(number, group);
        return group;
    }
}
