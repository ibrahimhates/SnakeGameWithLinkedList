public class Snake {
    public Snake tail;
    public static int snakeTailCount = 0;
    private int x,y;

    public Snake(int x, int y) {
        tail = null;
        snakeTailCount++;
        this.x = x;
        this.y = y;
    }

    public void yeniKonum(int newX,int newY){
        if(tail != null){
            tail.yeniKonum(this.x,this.y);
        }
        x = newX;
        y = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
