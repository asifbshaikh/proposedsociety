package utils;

import java.util.concurrent.atomic.AtomicLong;

import common.TextRandomizer;

public class DefaultPaymentCodeGenerator implements PaymentCodeGenerator {
    private static final int RANDOM_PART_LENGTH = 5;
    private static final long COUNTER_MAX = 100000;
    private final TextRandomizer randomizer = new TextRandomizer();
    private final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
 
    public DefaultPaymentCodeGenerator() {
        
    }
    @Override 
    public String generate() {
        return randomizer.nextRandomString(RANDOM_PART_LENGTH) + String.format("%05d", counter.incrementAndGet() % COUNTER_MAX);
    }
}
