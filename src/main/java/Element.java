import com.googlecode.lanterna.graphics.TextGraphics;

abstract class Element {
    protected Position position;



    public Element(int x, int y) {
        this.position = new Position(x, y);
    }

    public abstract void draw(TextGraphics graphics);


    public Position moveUp(){
        return new Position(position.getX(), position.getY() - 1);
    }

    public Position moveDown(){
        return new Position(position.getX(), position.getY() + 1);
    }

    public Position moveLeft(){
        return new Position(position.getX() - 1, position.getY());
    }

    public Position moveRight(){
        return new Position(position.getX() + 1, position.getY());
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
