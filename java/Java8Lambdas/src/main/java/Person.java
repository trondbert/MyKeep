/**
 * Created by trond on 10/09/14.
 */
public class Person {

    public Person(int age, Gender gender, boolean contractedCancer) {
        this.age = age;
        this.gender = gender;
        this.contractedCancer = contractedCancer;
    }

    public enum Gender { MALE, FEMALE }

    final int age;

    final Gender gender;

    final boolean contractedCancer;

}
