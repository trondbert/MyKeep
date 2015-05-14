import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trond on 10/09/14.
 */
public class PersonCalculatorTest {

    @Test
    public void calculator_sums_ages_of_two_persons() {

        List<Person> persons = new ArrayList() {{
            add(new Person(20, Person.Gender.MALE, false));
            add(new Person(30, Person.Gender.FEMALE, true));
        }};

        Assertions.assertThat(PersonCalculator.sumOfAges(persons)).isEqualTo(50);
    }

    @Test
    public void calculator_sums_ages_of_tree_persons() throws Exception {
        List<Person> persons = new ArrayList() {{
            add(new Person(20, Person.Gender.MALE, false));
            add(new Person(30, Person.Gender.FEMALE, true));
            add(new Person(10, Person.Gender.FEMALE, false));
        }};

        Assertions.assertThat(PersonCalculator.sumOfAges(persons)).isEqualTo(60);
    }

    @Test
    public void calculates_average_of_persons_without_cancer() throws Exception {
        ArrayList<Person> persons = new ArrayList() {{
            add(new Person(20, Person.Gender.FEMALE, false));
            add(new Person(70, Person.Gender.MALE, true));
            add(new Person(30, Person.Gender.FEMALE, true));
            add(new Person(65, Person.Gender.FEMALE, true));
            add(new Person(55, Person.Gender.MALE, false));
        }};

        Assertions.assertThat(PersonCalculator.averageAgeOfNonCancerPersons(persons)).isEqualTo(55);

    }



}
