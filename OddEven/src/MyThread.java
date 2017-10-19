
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MyThread implements Runnable {

	private Integer left, right;
	private MyThread next;
	private CyclicBarrier barrier;
	Integer myId;
	private Integer counter = 0;
	private Integer maxThreads;
	private final CountDownLatch doneSignal;
	private Integer tmp;

	public MyThread(Integer left, Integer right, CyclicBarrier barrier, int thr, Integer name, CountDownLatch done) {
		this.barrier = barrier;
		this.myId = name;
		this.maxThreads = thr;
		this.doneSignal = done;
		this.left = left;
		this.right = right;
	}

	@Override
	public void run() {

		while (counter < maxThreads * 2) {
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
		if (counter % 2 == 1 && next != null) {
			final Integer nextLeft = next.getLeft();
			if (right > nextLeft) {
				next.setLeft(right);
				right = nextLeft;
			}
		} else {
			if (right != null && left > right) {
				tmp = right;
				right = left;
				left = tmp;
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

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getRight() {
		return right;
	}

	public void setRight(Integer right) {
		this.right = right;
	}

}

// import java.util.concurrent.BrokenBarrierException;
// import java.util.concurrent.CountDownLatch;
// import java.util.concurrent.CyclicBarrier;
//
// public class MyThread implements Runnable {
// private Integer left, right, counter = 0, maxPfretts;
// private MyThread next;
// private CyclicBarrier barrier;
// private String name;
// private CountDownLatch stopSignal;
//
// public MyThread(Integer left, Integer right, CyclicBarrier barrier, Integer
// maxPfretts, String name,
// CountDownLatch stopSignal) {
// this.left = left;
// this.right = right;
// this.maxPfretts = maxPfretts;
// this.barrier = barrier;
// this.name = name;
// this.stopSignal = stopSignal;
// }
//
// @Override
// public void run() {
// try {
// barrier.await();
// } catch (InterruptedException | BrokenBarrierException e) {
// e.printStackTrace();
// System.out.println(
// "Pfrett " + name + " erlitt einen kritischen Fehlschlag! \n Wertepaar: " +
// left + "\t" + right);
// }
// while (counter < maxPfretts * 2) {
// sort();
// ++counter;
// try {
// barrier.await();
// } catch (InterruptedException | BrokenBarrierException e) {
// e.printStackTrace();
// System.out.println(
// "Pfrett " + name + " erlitt einen kritischen Fehlschlag! \n Wertepaar: " +
// left + "\t" + right);
// }
// }
// stopSignal.countDown();
//
// }
//
// private void sort() {
// if (counter % 2 == 1 && next != null) {
// final Integer nextLeft = next.getLeft();
// if (right > nextLeft) {
// next.setLeft(right);
// right = nextLeft;
// // synchronized (System.out) {
// // System.out.println(counter+"Schritt\tPfrett "+name+" tauscht
// // Wertepaar:"+right+"\t"+next.getLeft());
// // }
//
// }
// } else {
// final Integer tmp = left;
// if (right != null && left > right) {
// left = right;
// right = tmp;
// // synchronized (System.out) {
// // System.out.println(counter+"Schritt\tPfrett "+name+" tauscht
// // Wertepaar:"+right+"\t"+next.getLeft());
// // }
// }
// }
// }
//
// public CyclicBarrier getBarrier() {
// return barrier;
// }
//
// public void setBarrier(CyclicBarrier barrier) {
// this.barrier = barrier;
// }
//
// public Integer getCounter() {
// return counter;
// }
//
// public void setCounter(Integer counter) {
// this.counter = counter;
// }
//
// public Integer getLeft() {
// return left;
// }
//
// public void setLeft(Integer left) {
// this.left = left;
// }
//
// public Integer getRight() {
// return right;
// }
//
// public void setRight(Integer right) {
// this.right = right;
// }
//
// public MyThread getNext() {
// return next;
// }
//
// public void setNext(MyThread next) {
// this.next = next;
// }
//
// }
