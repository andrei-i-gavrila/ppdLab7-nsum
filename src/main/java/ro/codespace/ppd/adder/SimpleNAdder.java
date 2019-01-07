package ro.codespace.ppd.adder;

import java.util.List;

public class SimpleNAdder {
    public Long add(List<Long> numbers) {
        return numbers.stream().mapToLong(Long::longValue).sum();
    }
}
