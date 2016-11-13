    import java.io.File;
    import java.io.FileNotFoundException;
    import java.util.*;

    public class Temp {

        private static List<String> allNames = Arrays.asList("name1", "name2", "name3", "name4", "name5", "name6");

        public static void main(String[] args) {
            new Temp().testScan();
        }

        private void testScan() {
            try {
                Scanner sc = new Scanner(new File("src/test/resources/words.txt"));
                while (sc.hasNext()) {
                    System.out.println(sc.next());
                    System.out.println(sc.hasNextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        void test() {
            List<String> list = new ArrayList<>();
            list.add("Bernie");
            addFiveRandom(list);
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + ": " + list.get(i));
            }
        }

        void addFiveRandom(List<String> toBeAddedTo) {
            List<Integer> indices = new ArrayList<>();
            while (indices.size() < 5) {
                int newIndex = new Random().nextInt(5);
                if (!indices.contains(newIndex))
                    indices.add(newIndex);
            }
            for (Integer index : indices) {
                toBeAddedTo.add(allNames.get(index));
            }
        }
    }
