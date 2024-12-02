package dev.phoenixofforce.adventofcode.solver;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public class Maths {

    public static long lcm(Collection<Long> numbers) {
        long out = 1;
        for(long l: numbers) {
            long gcd = gcd(out, l);
            out = (out * l) / gcd;
        }
        return out;
    }

    public static long gcd(Collection<Long> numbers) {
        AtomicLong out = new AtomicLong(numbers.stream().findFirst().get());
        numbers.stream()
            .skip(1)
            .forEach(l -> out.set(gcd(out.get(), l)));
        return out.get();
    }

    private static long gcd(long n1, long n2) {
        if (n2 == 0) {
            return n1;
        }
        return gcd(n2, n1 % n2);
    }

}
