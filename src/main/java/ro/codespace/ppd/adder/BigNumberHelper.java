package ro.codespace.ppd.adder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BigNumberHelper {

    public static final Integer DONE_MARKER = -1;

    public static List<Integer> ZERO() {
        return new ArrayList<>(Collections.singletonList(0));
    }

    public static Queue<Integer> toDigitQueue(List<Integer> number) {
        var queue = new ConcurrentLinkedQueue<>(number);
        queue.add(DONE_MARKER);
        return queue;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static List<Integer> fromDigitQueue(Queue<Integer> number) {
        var digits = new ArrayList<Integer>();
        while (true) {
            while (number.peek() == null) ;
            var digit = number.remove();
            if (digit.equals(DONE_MARKER)) {
                break;
            }
            digits.add(digit);
        }
        return digits;
    }

    public static String toString(List<Integer> number) {
        return toLong(number).toString();
    }

    public static Long toLong(List<Integer> number) {
        long base = 1;
        long result = 0;
        for (Integer integer : number) {
            result += integer * base;
            base *= 10;
        }
        return result;
    }

}
