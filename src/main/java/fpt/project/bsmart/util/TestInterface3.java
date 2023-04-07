package fpt.project.bsmart.util;

interface Printable{
    default void print() {
        System.out.println("a");
    };
}
interface Showable{

    default void print() {
        System.out.println("b");
    };
}

// method signature

interface ss extends Printable, Showable{

    @Override
    default void print() {
        Showable.super.print();
    }
}
