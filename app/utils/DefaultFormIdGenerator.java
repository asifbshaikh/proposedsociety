package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultFormIdGenerator implements FormIdGenerator {
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddhhmmss");
        }
    };
    
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    
    private static final int MAX_COUNT = 100000;
    
    @Override
    public String generateApplicationFormId() {
        return DATE_FORMAT_THREAD_LOCAL.get().format(new Date()) + String.format("%06d", COUNTER.getAndIncrement() % MAX_COUNT);
    }
}
