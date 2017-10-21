import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Controller {

	// private static Integer[] unsorted = { 7, 5, 3, 6, 2, 4, 1, 0 };
	private static Integer[] unsorted = new Integer[200000];
	private static int maxThreads = 4;
	private static Integer[] digits;
	private static CyclicBarrier barrier = new CyclicBarrier(maxThreads);
	private static MyThread[] threads = new MyThread[maxThreads];;
	private static CountDownLatch doneSignal = new CountDownLatch(maxThreads);
	private static boolean bubble = false;

	public static void main(String[] args) {
		Random rnd = new Random(42);
		for (int i = 0; i < unsorted.length; ++i) {
			unsorted[i] = rnd.nextInt(1 << 16);
			// System.out.print(unsorted[i] + " ");
		}
		System.out.println("\n");
		if (unsorted.length % maxThreads == 0) {
			digits = new Integer[unsorted.length / maxThreads];
			for (int i = 0; i < threads.length; ++i) {
				System.arraycopy(unsorted, (i * unsorted.length / maxThreads), digits, 0, digits.length);
				threads[i] = new MyThread(digits, barrier, maxThreads, i, doneSignal, bubble);

			}
			for (int i = 0; i < maxThreads; ++i) {
				if (i < threads.length - 1) {
					threads[i].setNext(threads[i + 1]);
				}
			}

			final Long start = System.nanoTime();
			for (int i = 0; i < maxThreads; ++i) {
				new Thread(threads[i]).start();
			}

			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			final Long end = System.nanoTime();
			Integer[] sorted = new Integer[unsorted.length];
			Integer[] test;
			System.out.println("\nNach dem Sortieren:\n");
			for (int i = 0; i < threads.length; ++i) {
				System.arraycopy(threads[i].getDigits(), 0, sorted, i * threads[i].getDigits().length,
						threads[i].getDigits().length);
			}
			// for (int i = 0; i < sorted.length; ++i) {
			// System.out.print(sorted[i] + " ");
			// }
			System.out.println("\n------------------------");
			System.out.println("\nBenötigte Zeit in ms: \t" + (end - start) / 1000000.0);
			System.out.println("\nBenötigte Zeit in ns: \t" + (end - start));
			System.out.println("\nLänge vorher: " + unsorted.length + "\tLänge nachher: " + sorted.length);
			Arrays.sort(unsorted);
			if (checkNumbers(sorted)) {
				System.out.println("\nAlle Zahlen vorhanden");
				if (checkSort(sorted)) {
					System.out.println("\nZahlen sind sortiert");
				} else {
					System.out.println("\nZahlen sind nicht sortier");
				}
			} else {
				System.out.println("Ein böser Hobbit hat zahlen geklaut!");
			}

		} else {
			System.out.printf("Zahlen lassen sich nicht aufteilen");
		}
	}

	private static boolean checkNumbers(Integer[] sorted) {
		if (sorted.length == unsorted.length) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean checkSort(Integer[] sorted) {
		boolean sort = true;
		int i = 0;
		while (sort && i < sorted.length - 1) {
			if (sorted[i] > sorted[i + 1]) {
				sort = false;
			}
			++i;
		}
		return sort;
	}
}
