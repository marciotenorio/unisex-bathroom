package br.imd.unisexbathroom.main;

import br.imd.unisexbathroom.problem.Person;
import br.imd.unisexbathroom.problem.PersonGenerator;
import br.imd.unisexbathroom.problem.UnisexBathroom;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int capacity;
        int numberOfPeople = 8;

        if(args.length > 0){
            capacity = Integer.parseInt(args[0]);
        }
        else {
            capacity = 3;
        }

        UnisexBathroom unisexBathroom = new UnisexBathroom(capacity);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfPeople);
        List<Person> personList = new PersonGenerator().generate(numberOfPeople, unisexBathroom, countDownLatch);
        ExecutorService executorService = Executors.newWorkStealingPool();

        System.out.println("Inicio do uso do banheiro com: " + capacity + " de capacidade e "
                + "com " + numberOfPeople + " pessoas para usar.");

        for(Person person : personList){
            executorService.submit(person);
        }

        try {
            countDownLatch.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Fim do uso do banheiro xD");
    }
}