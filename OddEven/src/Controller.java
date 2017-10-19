import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Controller {

	// private static Integer[] zahlen = new Integer[4];
	private static int[] zahlen = new int[20];
	private static int maxThreads = zahlen.length / 2;
	private static CyclicBarrier barrier = new CyclicBarrier(maxThreads);
	private static MyThread[] threads = new MyThread[maxThreads];;
	private static CountDownLatch doneSignal = new CountDownLatch(maxThreads);

	public static void main(String[] args) {
		Random rnd = new Random(42);
		for (int i = 0; i < zahlen.length; ++i) {
			zahlen[i] = rnd.nextInt(1 << 16);
			System.out.print(zahlen[i] + " ");
		}
		System.out.println("\n");
		if (zahlen.length % maxThreads == 0) {
			for (int i = 0; i < zahlen.length; i = i + 2) {
				threads[i / 2] = new MyThread(zahlen[i], zahlen[i + 1], barrier, maxThreads, i / 2, doneSignal);

			}
			for (int i = 0; i < maxThreads; ++i) {
				if (i < threads.length - 1) {
					threads[i].setNext(threads[i + 1]);
				}
				new Thread(threads[i]).start();
			}
		} else {
			System.out.printf("Zahlen lassen sich nicht aufteilen");
		}

		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int[] sorted = new int[zahlen.length];
		System.out.println("\nNach dem Sortieren:\n");
		for (int i = 0; i < sorted.length; i = i + 2) {
			sorted[i] = threads[i / 2].getLeft();
			sorted[i + 1] = threads[i / 2].getRight();
		}
		for (int i = 0; i < sorted.length; ++i) {
			System.out.print(sorted[i] + " ");
		}
	}

}
