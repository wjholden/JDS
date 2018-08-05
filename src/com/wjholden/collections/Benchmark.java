package com.wjholden.collections;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.function.DoubleSupplier;

public class Benchmark {
    static SecureRandom RNG = new SecureRandom();
    
    static int[] generate(int size, DoubleSupplier rng) {
        int a[] = new int[size];
        for (int i = 0 ; i < size ; i++) {
            a[i] = (int) (Math.abs(size * rng.getAsDouble()));
        }
        return a;
    }
    
    static Operations[] getOperations(int size) {
        Operations[] o = new Operations[size];
        for (int i = 0 ; i < size ; i++) {
            switch (RNG.nextInt(3)) {
                case 0: o[i] = Operations.add; break;
                case 1: o[i] = Operations.contains; break;
                case 2: o[i] = Operations.delete; break;
            }
        }
        return o;
    }
    
    enum Operations { add, delete, contains };
    
    static Duration benchmark(Storable<Integer> a, int[] d, Operations[] o) {
        Instant start = Instant.now();
        for (int i = 0 ; i < o.length ; i++) {
            switch (o[i]) {
                case add: a.add(d[i]); break;
                case contains: a.contains(d[i]); break;
                case delete: a.remove(d[i]); break;
            }
        }
        return Duration.between(start, Instant.now());
    }
    
    static double gaussian() {
        return (RNG.nextGaussian() + 1)/2;
    }
    
    public static void main(String args[]) throws InstantiationException, IllegalAccessException {
        final Class[] types = new Class[] { /*AVLTreeSet.class,*/ SplayTreeSet.class };
        final DoubleSupplier ds[] = new DoubleSupplier[] { RNG::nextDouble, Benchmark::gaussian };
        for (int i = 1 ; i < 20 ; i++) {
            for (Class t : types) {
                for (int prng : new int [] { 0, 1 }) {
                System.out.printf("%s\t%d\t%10s\t%s%n", t.getSimpleName(), 1 << i,
                        benchmark((Storable<Integer>) t.newInstance(),
                                generate(1 << i, ds[prng]), getOperations(1 << i)),
                        prng == 0 ? RNG.getAlgorithm() : "Gaussian Noise");
                }
            }
        }
    }
}
