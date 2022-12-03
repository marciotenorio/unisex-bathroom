package br.imd.unisexbathroom.problem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UnisexBathroom {

    private List<Person> personList;

    private int capacity;

    private String currentGender;

    private Lock lock;

    private Condition isFullOrWrongGender;

    private Condition isEmptyOrSameGender;

    public UnisexBathroom(int capacity) {
        this.personList = new ArrayList<>();
        this.capacity = capacity;
        this.lock = new ReentrantLock(true);
        this.isFullOrWrongGender = lock.newCondition();
        this.isEmptyOrSameGender = lock.newCondition();
        this.currentGender = "Empty";
    }


    public void enter(Person person){
        lock.lock();

        try{
            while (personList.size() == capacity) {
                System.out.println("Desculpe, segura ai que o banheiro ta cheio. "
                        + person.getName() + " " + person.getGender());
                isFullOrWrongGender.await();
            }


            while(!currentGender.equals(person.getGender())){
                if(currentGender.equals("Empty"))
                    break;

                System.out.println("Desculpe, pessoas com seu gênero não podem entrar agora. "
                        + person.getGender() + " " + person.getName());
                isFullOrWrongGender.await();
            }

            personList.add(person);
            currentGender = person.getGender();

            System.out.println("Entrou " + person.getName() + " " + person.getGender());
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        finally {
            isEmptyOrSameGender.signal();
            lock.unlock();
        }
    }

    public void exit(Person person){
        lock.lock();

        try {
            while (personList.isEmpty()) {
                isEmptyOrSameGender.await();
            }

            while (notInList(person)){
                isEmptyOrSameGender.await();
            }

            personList.remove(person);

            System.out.println("Quantidade atual de pessoas no banheiro: " + this.personList.size() +
                    " do gênero " + currentGender);

            if(personList.isEmpty())
                currentGender = "Empty";

            System.out.println("Saiu " + person.getName() + " " + person.getGender());

            isFullOrWrongGender.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public boolean notInList(Person person){
        return !personList.contains(person);
    }
}
