package Utils;

import java.util.Random;

public class Utils {
    @FunctionalInterface
    public interface TriPredicate<S,U,V> {
        boolean test(S s, U u, V v);
    }

    public enum GlobRandom {
        INSTANCE;
        public final Random random;
        GlobRandom() {
            random = new Random();
        }
    }

    public static class StopWatch {
        private long startTime;
        private long stopTime;

        public void startTime(){
            this.startTime = System.nanoTime();
        }

        public void stopTime() {
            this.stopTime = System.nanoTime();
        }

        public long getTimeNano() {
            return stopTime - startTime;
        }

        public double getTimeSeconds() {
            return getTimeNano() / Math.pow(10, 9);
        }
    }
}
