package com.pan.chap5React;

import io.reactivex.Flowable;

import java.util.ArrayList;
import java.util.List;

public class TestRxJava {
    public static void main(String[] args) {
        List<Person> personList = makeList();

        Flowable.fromArray(personList.toArray(new Person[0]))
                .filter(person -> person.getAge()>=10)
                .map(person -> person.getName())
                .subscribe(System.out::println);
    }

    public static List<Person> makeList() {
        List<Person> personList = new ArrayList<>();
        Person p1 = new Person();
        p1.setAge(10);
        p1.setName("a");
        personList.add(p1);

        p1 = new Person();
        p1.setAge(20);
        p1.setName("b");
        personList.add(p1);

        p1 = new Person();
        p1.setAge(30);
        p1.setName("c");
        personList.add(p1);

        return personList;
    }

    static class Person {
        int age;
        String name;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
