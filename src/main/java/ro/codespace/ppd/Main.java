package ro.codespace.ppd;

import ro.codespace.ppd.adder.BigNumberHelper;
import ro.codespace.ppd.adder.SimpleNAdder;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {


    public static void main(String... args) {
        int N = 100000;
        var numbers = getRandomNumbers(N, 122124);
//        numbers = Arrays.asList(Arrays.asList(8, 7, 1, 7, 2), Arrays.asList(4, 2));
//        System.out.println(numbers.stream().map(BigNumberHelper::toString).collect(Collectors.joining(" + ")));

        var longNumbers = numbers.stream().map(BigNumberHelper::toLong).collect(Collectors.toList());
        var simpleStartTime = System.currentTimeMillis();
        var simpleResult = new SimpleNAdder().add(longNumbers);
        var simpleTime = System.currentTimeMillis() - simpleStartTime;
        System.out.println("Simple took " + simpleTime + "ms and result is " + simpleResult);


        var queueNumbers = numbers.stream().map(BigNumberHelper::toDigitQueue).collect(Collectors.toList());
        var parallelStartTime = System.currentTimeMillis();
        var parallelResult = new ParallelNAdder().add(queueNumbers);
        var parallelTime = System.currentTimeMillis() - parallelStartTime;
        String parallelResultString = BigNumberHelper.toString(parallelResult);
        System.out.println("Parallel took " + parallelTime + "ms and result is " + parallelResultString);

        assert simpleResult.toString().equals(parallelResultString);
    }

    private static List<List<Integer>> getRandomNumbers(int n, int seed) {
        var random = new Random(seed);
        return IntStream.range(0, n).mapToObj(i -> random.ints(random.nextInt(4) + 2, 1, 9).boxed().collect(Collectors.toList())).collect(Collectors.toList());
    }

}
