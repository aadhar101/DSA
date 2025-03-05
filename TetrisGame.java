import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TetrisGame {
    static final int WIDTH = 10;
    static final int HEIGHT = 20;
    static final int BLOCK_SIZE = 30;
    
    static int[][] gameBoard = new int[HEIGHT][WIDTH];  // Game board represented as a grid
    static Queue<Block> blockQueue = new LinkedList<>(); // Queue of falling blocks
    static Stack<int[][]> boardStack = new Stack<>();    // Stack representing the game state
    
    static Block currentBlock; // Current falling block
    
    static boolean gameOver = false;
    static int score = 0;
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris Game");
        frame.setSize(WIDTH * BLOCK_SIZE + 150, HEIGHT * BLOCK_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TetrisPanel panel = new TetrisPanel();
        frame.add(panel);
        
        panel.setFocusable(true);
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (gameOver) return;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        moveBlockLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveBlockRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        moveBlockDown();
                        break;
                    case KeyEvent.VK_UP:
                        rotateBlock();
                        break;
                }
            }
        });
        
        frame.setVisible(true);
        initializeGame(panel);
    }
    
    public static void initializeGame(TetrisPanel panel) {
        // Initialize game state and start the game loop
        Arrays.stream(gameBoard).forEach(row -> Arrays.fill(row, 0)); // Clear board
        generateNewBlock(); // Generate the first block
        gameLoop(panel);    // Start the game loop
    }

    public static void generateNewBlock() {
        Block newBlock = Block.randomBlock();
        blockQueue.add(newBlock);
        currentBlock = newBlock;
    }

    public static void gameLoop(TetrisPanel panel) {
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    moveBlockDown();
                    checkForGameOver();
                    panel.repaint();
                }
            }
        });
        timer.start();
    }

    // Move the block down and check for collision
    public static void moveBlockDown() {
        if (canMove(0, 1)) {
            currentBlock.moveDown();
        } else {
            placeBlockOnBoard();
            checkForCompletedRows();
            generateNewBlock();
        }
    }

    // Check if the current block can move to a new position (horizontal or vertical)
    public static boolean canMove(int dx, int dy) {
        for (int[] cell : currentBlock.getCoordinates()) {
            int newX = cell[0] + dx;
            int newY = cell[1] + dy;
            if (newX < 0 || newX >= WIDTH || newY >= HEIGHT || gameBoard[newY][newX] != 0) {
                return false;
            }
        }
        return true;
    }

    // Move the block left
    public static void moveBlockLeft() {
        if (canMove(-1, 0)) {
            currentBlock.moveLeft();
        }
    }

    // Move the block right
    public static void moveBlockRight() {
        if (canMove(1, 0)) {
            currentBlock.moveRight();
        }
    }

    // Rotate the current block (simple rotation logic)
    public static void rotateBlock() {
        currentBlock.rotate();
        if (!canMove(0, 0)) {
            currentBlock.undoRotation(); // If rotation results in collision, undo it
        }
    }

    // Place the current block on the game board
    public static void placeBlockOnBoard() {
        for (int[] cell : currentBlock.getCoordinates()) {
            gameBoard[cell[1]][cell[0]] = 1; // Mark cell as occupied
        }
        boardStack.push(gameBoard); // Push the current state onto the stack
    }

    // Check for completed rows and remove them
    public static void checkForCompletedRows() {
        for (int row = 0; row < HEIGHT; row++) {
            boolean fullRow = true;
            for (int col = 0; col < WIDTH; col++) {
                if (gameBoard[row][col] == 0) {
                    fullRow = false;
                    break;
                }
            }
            if (fullRow) {
                // Remove this row and shift everything above it down
                for (int r = row; r > 0; r--) {
                    gameBoard[r] = Arrays.copyOf(gameBoard[r - 1], WIDTH);
                }
                Arrays.fill(gameBoard[0], 0); // Clear the top row
                score += 100; // Increase score for clearing a row
            }
        }
    }

    // Check if the top row is filled (game over condition)
    public static void checkForGameOver() {
        for (int col = 0; col < WIDTH; col++) {
            if (gameBoard[0][col] != 0) {
                gameOver = true;
                JOptionPane.showMessageDialog(null, "Game Over! Final Score: " + score);
                return;
            }
        }
    }

    // The game panel that handles painting the game board
    static class TetrisPanel extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (gameOver) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("GAME OVER", WIDTH * BLOCK_SIZE / 4, HEIGHT * BLOCK_SIZE / 2);
            } else {
                // Draw the game board
                for (int row = 0; row < HEIGHT; row++) {
                    for (int col = 0; col < WIDTH; col++) {
                        if (gameBoard[row][col] != 0) {
                            g.setColor(Color.CYAN); // Color for the blocks
                            g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                        }
                    }
                }
                // Draw the next block preview
                g.setColor(Color.GREEN);
                g.fillRect(WIDTH * BLOCK_SIZE + 10, 50, BLOCK_SIZE * 4, BLOCK_SIZE * 4);
                for (int[] coord : blockQueue.peek().getCoordinates()) {
                    g.fillRect(WIDTH * BLOCK_SIZE + 10 + coord[0] * BLOCK_SIZE, 50 + coord[1] * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }
    }
}
