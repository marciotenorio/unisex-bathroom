package br.imd.unisexbathroom.problem;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class Person implements Runnable{

    private String name;

    private String gender;

    private int bathroomDuration;

    private UnisexBathroom unisexBathroom;

    private CountDownLatch countDownLatch;

    public Person(UnisexBathroom unisexBathroom, CountDownLatch countDownLatch) {
        this.unisexBathroom = unisexBathroom;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        unisexBathroom.enter(this);

        try {
            Thread.sleep(Duration.ofSeconds(this.getBathroomDuration()).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        unisexBathroom.exit(this);
        this.countDownLatch.countDown();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBathroomDuration() {
        return bathroomDuration;
    }

    public void setBathroomDuration(int bathroomDuration) {
        this.bathroomDuration = bathroomDuration;
    }
}
