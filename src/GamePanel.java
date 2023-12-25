import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private final SnakeManager snakeManager;
    static final int UNIT_SIZE = 25;
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    // Yilanin basi burda tanimladim.
    static Snake head = null;
    static boolean running = false;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    static int applesEaten = 2;
    char direction = 'R';
    Timer timer;
    Random random;

    GamePanel(){
        snakeManager = new SnakeManager(UNIT_SIZE, random);
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.green);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    // 5 adet bogum olusturmak icin method
    public void startGame () {
        Snake.snakeTailCount = 0;
        direction = 'R';
        head = snakeManager.resetSnake();
        applesEaten = 2;
        snakeManager.newApple();
        snakeManager.newBomb();
        running=true;
        timer= new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            //draw the grid line on the screen
            for(int i = 0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){
                g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }
            //draw the food on the screen on a random place
            g.setColor(Color.red);
            g.fillOval(snakeManager.appleX, snakeManager.appleY,UNIT_SIZE,UNIT_SIZE);

            g.setColor(Color.black);
            g.fillOval(snakeManager.bombX, snakeManager.bombY,UNIT_SIZE,UNIT_SIZE);

            // Duvar cizimi
            for(int x = 0; x < SCREEN_HEIGHT; x+=UNIT_SIZE){
                for(int y = 0;y < SCREEN_WIDTH;y+=UNIT_SIZE){
                    if(x == 0 || x == SCREEN_HEIGHT - UNIT_SIZE || y == 0 || y == SCREEN_WIDTH - UNIT_SIZE){
                        g.setColor(Color.GRAY);
                        g.fillRect(x,y,UNIT_SIZE,UNIT_SIZE);
                    }
                }
            }
            //draw the head and the body of the snake
            Snake temp = head;
            // yilani bagli liste kullanarak cizme yontemi
            while(temp != null){
                if(temp == head)
                    g.setColor(new Color(197, 0, 0));
                else
                    g.setColor(new Color(255,0,0));
                g.fillRect(temp.getX(), temp.getY(), UNIT_SIZE,UNIT_SIZE);
                //score
                temp = temp.tail;
                g.setColor(Color.red);
                g.setFont(new Font("Ink Free",Font.BOLD,40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Ölüme Kalan: "+applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Ölüme Kalan: "+applesEaten))/2,g.getFont().getSize());
            }
        }else {
            gameOver(g);
        }

    }

    public void move(){
        switch (direction){
            case 'U':
                head.yeniKonum(head.getX(), head.getY() - UNIT_SIZE);
                break;
            case 'D':
                head.yeniKonum(head.getX(), head.getY() + UNIT_SIZE);
                break;
            case 'L':
                head.yeniKonum(head.getX()  - UNIT_SIZE, head.getY());
                break;
            case 'R':
                head.yeniKonum(head.getX()  + UNIT_SIZE, head.getY());
                break;
        }
    }

    public void gameOver(Graphics g){
        //score
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Puan: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Puan : "+applesEaten))/2, g.getFont().getSize());
        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Oyun Bitti",(SCREEN_WIDTH - metrics.stringWidth("Oyun Bitti"))/2,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            snakeManager.checkApple();
            snakeManager.checkCollision(timer);
            snakeManager.checkBomb();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(!running){
                        startGame();
                    }
            }
        }
    }
}
