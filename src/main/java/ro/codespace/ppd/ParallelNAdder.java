package ro.codespace.ppd;

import ro.codespace.ppd.adder.BigNumberHelper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelNAdder {
    public List<Integer> add(List<Queue<Integer>> current) {
        var runners = new ArrayList<Adder>();
        while (current.size() > 1) {
            List<Queue<Integer>> next = new ArrayList<>();

            for (var i = 0; i + 1 < current.size(); i += 2) {
                Queue<Integer> a = current.get(i);
                Queue<Integer> b = current.get(i + 1);

//                System.out.println("Adding " + BigNumberHelper.toString(BigNumberHelper.fromDigitQueue(new ArrayDeque<>(a))) + " and " + BigNumberHelper.toString(BigNumberHelper.fromDigitQueue(new ArrayDeque<>(b))));

                var adder = new Adder(a, b);
                next.add(adder.getOutput());
                runners.add(adder);
            }

            if (current.size() % 2 == 1) {
                next.add(current.get(current.size() - 1));
            }
            current = next;
        }
        Thread thread = null;
        for (var adder : runners) {
            thread = new Thread(adder);
            thread.start();
        }

        try {
            if (thread != null) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return BigNumberHelper.fromDigitQueue(current.get(0));
    }


    private static class Adder implements Runnable {
        private Queue<Integer> a;
        private Queue<Integer> b;
        private Queue<Integer> output;

        private Adder(Queue<Integer> a, Queue<Integer> b) {
            this.a = a;
            this.b = b;
            this.output = new ConcurrentLinkedQueue<>();
        }

        Queue<Integer> getOutput() {
            return output;
        }


        @SuppressWarnings({"StatementWithEmptyBody", "Duplicates"})
        @Override
        public void run() {
            var aDone = false;
            var bDone = false;
            var carry = 0;
            while (!aDone || !bDone) {
                var aDigit = 0;
                if (!aDone) {
                    while (a.peek() == null) ;
                    aDigit = a.poll();
                    aDone = aDigit == BigNumberHelper.DONE_MARKER;
                }


                var bDigit = 0;
                if (!bDone) {
                    while (b.peek() == null) ;
                    bDigit = b.poll();
                    bDone = bDigit == BigNumberHelper.DONE_MARKER;
                }

                var total = (aDone ? 0 : aDigit) + (bDone ? 0 : bDigit) + carry;

                carry = total / 10;
                var value = total % 10;

                output.offer(value);
            }
            if (carry != 0) {
                output.offer(carry);
            }

            output.offer(BigNumberHelper.DONE_MARKER);
        }
    }
}
