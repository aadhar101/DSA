class NumberPrinter {
    public void printZero() {
        System.out.print("0");
    }

    public void printEven(int number) {
        System.out.print(number);
    }

    public void printOdd(int number) {
        System.out.print(number);
    }
}

class ThreadController {
    private int n;
    private NumberPrinter printer;
    private final Object lock = new Object();
    private int counter = 1; // Start the counter at 1 since 0 is printed first

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void startThreads() {
        // Zero thread will print "0" every time
        Thread zeroThread = new Thread(() -> {
            for (int i = 1; i <= n; i++) {
                synchronized (lock) {
                    while (counter != 1) { // Wait until it's time to print 0
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    printer.printZero();
                    counter = (i % 2 == 0) ? 2 : 3; // Next should be Even if i is even, Odd if i is odd
                    lock.notifyAll();
                }
            }
        });

        // Even thread will print even numbers
        Thread evenThread = new Thread(() -> {
            for (int i = 2; i <= n; i += 2) {
                synchronized (lock) {
                    while (counter != 2) { // Wait until it's time to print an even number
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    printer.printEven(i);
                    counter = 3; // After printing even, the next should be odd
                    lock.notifyAll();
                }
            }
        });

        // Odd thread will print odd numbers
        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= n; i += 2) {
                synchronized (lock) {
                    while (counter != 3) { // Wait until it's time to print an odd number
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    printer.printOdd(i);
                    counter = 1; // After printing odd, the next should be zero
                    lock.notifyAll();
                }
            }
        });

        zeroThread.start();
        evenThread.start();
        oddThread.start();

        try {
            zeroThread.join();
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        int n = 6; // Example input
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);
        controller.startThreads();
    }
}
