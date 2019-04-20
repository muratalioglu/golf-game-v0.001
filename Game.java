import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel {
    private static final long serialVersionUID = 239778176347549875L;
    private int x = 0;
    private int y = 0;
    private int speed = 10;
    private int score = 0;
    private String key = new String();    
    private Random random = new Random();    
    private int targetX = setTargetX();
    private int targetY = setTargetY();
    
    public Game() {
        KeyListener listener = new KeyListener() {            
            @Override
            public void keyTyped(KeyEvent e) {              
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 32: bringBallToStartPoint();
                        break;
                    case 37: case 65:
                        key = "A, Left";
                        moveBallLeft();
                        break;
                    case 38: case 87:
                        key = "W, Up";
                        moveBallUp();
                        break;
                    case 39: case 68:
                        key = "D, Right";
                        moveBallRight();
                        break;
                    case 40: case 83:
                        key = "S, Down";
                        moveBallDown();
                        break;
                }                
                speedUp();
                
                if (isXinTarget() && isYinTarget()) {
                    try {
                        JOptionPane.showMessageDialog(null, "Your score is: " + ++score);
                        Thread.sleep(250);
                    } catch (InterruptedException interrEx) {
                        interrEx.printStackTrace();
                    } finally {
                        bringBallToStartPoint();
                        targetX = setTargetX();
                        targetY = setTargetX();
                    }
                }
            }            

            private boolean isXinTarget() {
                return x > targetX && x < targetX + 50;
            }
            
            private boolean isYinTarget() {
                return y > targetY && y < targetY + 50;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                speed = 10;
                repaint();
            }
            
            private void speedUp() {
                speed++;
            }            
        };
        
        addKeyListener(listener);
        setFocusable(true);
    }
    
    private int setTargetX() {
        return setTargetY();
    }
    
    private int setTargetY() {
        return random.nextInt(525);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBackground(g);      
        drawTarget(g);
        drawBall(g);
        drawScoreBoard(g);
    }

    private void drawBackground(Graphics g) {
        Graphics2D background = (Graphics2D) g;
        File imageFile = new File("bg.png");
        BufferedImage bgImage = null;
        try {
            bgImage = ImageIO.read(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Paint p = new TexturePaint(bgImage, new Rectangle(0, 0, 60, 60));
        background.setPaint(p);
        
        for (int i = 0; i < 600; i += 60) {
            for (int j = 0; j < 600; j += 60) {
                background.fillRect(i, j, 60, 60);
            }
        }
    }

    private void drawScoreBoard(Graphics g) {
        Color textColor = Color.WHITE;
        drawPosition(g, textColor);        
        drawSpeed(g, textColor);        
        drawPressedKey(g, textColor);        
        drawScore(g);
    }

    private void drawScore(Graphics g) {
        Graphics2D successText = (Graphics2D) g;
        successText.drawString("Score: " + score, 10, 80);
    }

    private void drawPressedKey(Graphics g, Color textColor) {
        Graphics2D keyText = (Graphics2D) g;
        keyText.setColor(textColor);
        keyText.drawString("Key: " + key, 10, 60);
    }

    private void drawSpeed(Graphics g, Color textColor) {
        Graphics2D speedText = (Graphics2D) g;
        speedText.setColor(textColor);
        speedText.drawString("Speed: " + String.valueOf(speed), 10, 40);
    }

    private void drawPosition(Graphics g, Color textColor) {
        Graphics2D positionText = (Graphics2D) g;
        positionText.setColor(textColor);
        positionText.setFont(new Font("Arial", Font.PLAIN, 20));
        positionText.drawString(String.format("(x: %3d, y: %3d)", x, y), 10, 20);
    }

    private void drawBall(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.fillOval(x, y, 20, 20);
    }

    private void drawTarget(Graphics g) {
        Graphics2D target = (Graphics2D) g;
        target.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        target.setColor(Color.WHITE);
        target.fillOval(targetX, targetY, 50, 50);
    }
    
    private void moveBallLeft() {
        x -= speed;
        repaint();
    }
    
    private void moveBallUp() {
        y -= speed;
        repaint();
    }
    
    private void moveBallRight() {
        x += speed;
        repaint();
    }
    
    private void moveBallDown() {
        y += speed;
        repaint();
    }
    
    private void bringBallToStartPoint() {
        x = 0;
        y = 0;
        speed = 10;
        key = new String();
        repaint();
    }
    
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Game");
        Game game = new Game();
        frame.add(game);        
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
