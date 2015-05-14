import java.util.List;

public class PersonCalculator {

    public static int sumOfAges(List<Person> personList) {
        return personList.stream()
                .map(person -> person.age)
                .reduce(Operations::sum).get();
    }

    public static double averageAgeOfNonCancerPersons(List<Person> persons) {
        int nofPersonsWithCancer = (int) (persons.stream().filter(p -> p.contractedCancer).count());

        return persons.stream()
                .filter(p -> p.contractedCancer)
                .map(p -> p.age)
                .reduce( (i, j) -> i + j ).get() / (double) nofPersonsWithCancer;
    }
}

class Operations {

    static int sum(int i, int j) {
        return i + j;
    }
}
