import java.util.Random;
import javax.swing.Timer;
public class SnakeManager {
    private final int bodyParts = 5;
    private final int UNIT_SIZE;
    private final Random random;
    public int appleX,bombX;
    public int appleY,bombY;
    public SnakeManager(int UNIT_SIZE, Random random) {
        this.UNIT_SIZE = UNIT_SIZE;
        this.random = new Random();
    }
    public Snake resetSnake(){

        int i = bodyParts - 1;

        // Yilanin basinin konumu random olusturulmasi
        int[] point = getRandomPoint();

        while(true){
            // Yilanin basinin konumunun yemegin konumu ile cakismasi durumu
            boolean confilctApple = point[0] == appleX && point[1] == appleY;
            if(confilctApple){
                point = getRandomPoint();
                continue;
            }
            // Yilanin basinin konumunun bombanin konumu ile cakismasi durumu
            boolean confilctBomb = point[0] == bombX && point[1] == bombY;
            if(confilctBomb){
                point = getRandomPoint();
                continue;
            }

            break;
        }

        Snake firstTail = new Snake(point[0],point[1]);
        Snake temp = firstTail;
        while(i > 0){
            temp.tail = new Snake(point[0],point[1]);
            temp = temp.tail;
            i--;
        }

        return firstTail;
    }

    private int[] getRandomPoint(){
        int x = random.nextInt((int) (GamePanel.SCREEN_WIDTH/UNIT_SIZE));
        int y = random.nextInt((int) (GamePanel.SCREEN_HEIGHT/UNIT_SIZE));

        return checkPointOnWall(x,y);
    }

    private int[] checkPointOnWall(int x,int y){
        int pointX = (x == 0?x+1:(x == (GamePanel.SCREEN_WIDTH/UNIT_SIZE)-1?x-2:x))*UNIT_SIZE;
        int pointY = (y == 0?y+1:(y == (GamePanel.SCREEN_HEIGHT/UNIT_SIZE)-1?y-2:y))*UNIT_SIZE;

        return new int[]{pointX,pointY};
    }

    public Snake addNewTail(Snake head){
        Snake temp = head;
        while (temp != null) {
            if(temp.tail == null){
                temp.tail = new Snake(temp.getX(),temp.getY());
                break;
            }
            temp = temp.tail;
        }
        return head;
    }

    public Snake removeThirdTail(Snake head){
        int count = 3;

        Snake temp = head;
        Snake next = null;
        while (temp != null){
            count--;
            if(count == 0){
                next = temp.tail.tail;
                temp.tail = next;
                Snake.snakeTailCount--;
                break;
            }
            temp = temp.tail;
        }
        return head;
    }

    public void newApple(){
        int[] point = getRandomPoint();

        appleX = point[0];
        appleY = point[1];
    }

    public void newBomb(){
        int[] point = getRandomPoint();

        bombX = point[0];
        bombY = point[1];
    }

    public void checkCollision(Timer timer){
        // **** Yilanin basi ile kuyruklarindan herhangi birtanesinin cakismasi durumunda oyun biter
        Snake temp = GamePanel.head.tail;
        Snake tempHead = GamePanel.head;
        while(temp != null){
            if(tempHead.getX() == temp.getX()
                    && tempHead.getY() == temp.getY()){
                GamePanel.running = false;
                break;
            }
            temp = temp.tail;
        }
        // Yilan duvardan gecmeye calisirsa yani duvarin konumu ile yilan konumu ayni olursa oyun biter
        if(tempHead.getX() >= GamePanel.SCREEN_WIDTH - UNIT_SIZE
                || tempHead.getX() < UNIT_SIZE
                || tempHead.getY() >= GamePanel.SCREEN_HEIGHT - UNIT_SIZE
                || tempHead.getY() < UNIT_SIZE)
            GamePanel.running = false;

        // **** Yilanin bogum sayisi 3 olursa oyun biter
        if(Snake.snakeTailCount == 3)
            GamePanel.running = false;

        if(!GamePanel.running){
            timer.stop();
        }
    }

    public void checkApple(){
        Snake temp = GamePanel.head;
        if((temp.getX() == appleX) && (temp.getY() == appleY)){
            GamePanel.head = addNewTail(temp);
            GamePanel.applesEaten++;
            newApple();
        }
    }
    public void checkBomb() {
        int count = 3;

        Snake temp = GamePanel.head;
        while (temp != null){
            count--;
            if(count == 0){
                if((temp.getX() == bombX) && (temp.getY() == bombY)){
                    GamePanel.head = removeThirdTail(GamePanel.head);
                    GamePanel.applesEaten--;
                    newBomb();
                }
                break;
            }
            temp = temp.tail;
        }
    }

}


