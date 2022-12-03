package br.imd.unisexbathroom.problem;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class PersonGenerator {

    private Random random;

    public PersonGenerator() {
        try {
            this.random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public List<Person> generate(int number, UnisexBathroom unisexBathroom, CountDownLatch countDownLatch){
        List<Person> personList = new ArrayList<>();
        int personName;
        int bathroomDuration;

        for (int i = 0; i < number; i++) {
            Person person = new Person(unisexBathroom, countDownLatch);
            personName = random.nextInt(1000);
            bathroomDuration = random.nextInt(5);

            person.setName("Pessoa nº " + personName);
            person.setGender( personName % 2 == 0 ? "Masculino" : "Feminino");
            person.setBathroomDuration(bathroomDuration);

            personList.add(person);
        }

        return personList;
    }
}
