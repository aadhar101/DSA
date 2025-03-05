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
    private boolean printZero = true;

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void startThreads() {
        Thread zeroThread = new Thread(() -> {
            for (int i = 1; i <= n; i++) {
                synchronized (lock) {
                    while (!printZero) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    printer.printZero();
                    printZero = false;
                    lock.notifyAll();
                }
            }
        });

        Thread evenThread = new Thread(() -> {
            for (int i = 2; i <= n; i += 2) {
                synchronized (lock) {
                    while (printZero || i % 2 != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    printer.printEven(i);
                    printZero = true;
                    lock.notifyAll();
                }
            }
        });

        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= n; i += 2) {
                synchronized (lock) {
                    while (printZero || i % 2 == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    printer.printOdd(i);
                    printZero = true;
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
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int n = 6; // Change n as needed
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);
        controller.startThreads();
    }
}
