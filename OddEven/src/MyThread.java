
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MyThread implements Runnable {

	// private Integer left, right;
	private MyThread next;
	private CyclicBarrier barrier;
	private Integer myId;
	private Integer counter = 0;
	private Integer maxThreads;
	private final CountDownLatch doneSignal;
	private Integer[] digits;
	private boolean bubble;
	private Integer tmp;

	public MyThread(Integer[] digits, CyclicBarrier barrier, int thr, Integer name, CountDownLatch done,
			boolean bubble) {
		this.barrier = barrier;
		this.myId = name;
		this.maxThreads = thr;
		this.doneSignal = done;
		this.digits = new Integer[digits.length];
		System.arraycopy(digits, 0, this.digits, 0, digits.length);
		this.bubble = bubble;
		firstSort();
	}

	private void firstSort() {
		if (!bubble) {
			Arrays.sort(digits);
		} else {
			int temp;
			for (int i = 1; i < digits.length; i++) {
				for (int j = 0; j < digits.length - i; j++) {
					if (digits[j] > digits[j + 1]) {
						temp = digits[j];
						digits[j] = digits[j + 1];
						digits[j + 1] = temp;
					}
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e1) {
			e1.printStackTrace();
		}
		while (counter < maxThreads) {
			sort();
			++counter;
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e1) {
				e1.printStackTrace();
			}
		}
		doneSignal.countDown();
	}

	public void sort() {
		if (myId % 2 == counter % 2) {
			// System.out.println("Schritt: " + counter + "\tKern: " + myId);
			if (next != null) {
				final Integer[] nextDigits = next.getDigits();
				Integer[] newDigits = new Integer[digits.length + nextDigits.length];
				int i = 0, j = 0;
				for (int k = 0; k < newDigits.length; ++k) {
					if (i >= digits.length) {
						newDigits[k] = nextDigits[j];
						++j;
					} else if (j >= nextDigits.length) {
						newDigits[k] = digits[i];
						++i;
					} else {
						if (digits[i] <= nextDigits[j]) {
							newDigits[k] = digits[i];
							++i;
						} else if (digits[i] > nextDigits[j]) {
							newDigits[k] = nextDigits[j];
							++j;
						}
					}
				}
				System.arraycopy(newDigits, 0, digits, 0, digits.length);
				System.arraycopy(newDigits, digits.length, nextDigits, 0, digits.length);
				if (next != null) {
					next.setDigits(nextDigits);
				}
			}
		}
	}

	public void setNext(MyThread n) {
		this.next = n;
	}

	public MyThread getNext() {
		return this.next;
	}

	public Integer getName() {
		return this.myId;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public Integer[] getDigits() {
		return digits;
	}

	public void setDigits(Integer[] digits) {
		this.digits = digits;
	}

}