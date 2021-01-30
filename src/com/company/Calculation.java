package com.company;

import java.util.concurrent.RecursiveTask;
import java.util.stream.DoubleStream;

//extends RecursiveTask of the ForkJoin Framework represents a task which returns a value.
public class Calculation extends RecursiveTask {
    private double myArr[];
    double finalSum;

    public Calculation(double[] myArr) {
        this.myArr = myArr;
    }

    @Override
    protected Object compute() {
        finalSum = DoubleStream.of(this.myArr).parallel().map(d-> 1/d).sum();
        return finalSum;
    }
}
