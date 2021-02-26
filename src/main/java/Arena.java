import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {

    private int width;
    private int height;
    private Hero hero;
    private List<Wall> walls;
    private List<Coin> coins;
    private List<Monster> monsters;

    public Arena(int width, int height) {
        this.width = width;
        this.height = height;
        hero = new Hero(10, 10);
        this.walls = createWalls();
        this.coins = createCoins();
        this.monsters = createMonsters();

    }

    private List<Wall> createWalls(){
        List<Wall> walls = new ArrayList<>();

        for(int c = 0; c< width; c++){
            walls.add(new Wall(c, 0));
            walls.add(new Wall(c, height - 1));
        }

        for(int r =1; r < height - 1; r++){
            walls.add(new Wall(0, r));
            walls.add(new Wall(width - 1, r));
        }
        return walls;
    }

    private List<Coin> createCoins(){

        Random random = new Random();
        List<Coin> coins = new ArrayList<>();
        for(int i=0; i<5; i++){
            int x, y;
            boolean overlap;
            do {
                x = random.nextInt(width - 2) + 1;
                y = random.nextInt(height - 2) + 1;

                //Check collision with other coins
                overlap = false;
                for(Coin coin : coins){
                    if(coin.getPosition().equals(new Position(x, y))) overlap = true;
                }

            } while(overlap || !hasNoCollisions(new Position(x, y), true));
            coins.add(new Coin(x, y));
        }
        return coins;
    }

    private List<Monster> createMonsters(){
        Random random = new Random();
        List<Monster> monsters = new ArrayList<>();
        for(int i=0; i<5; i++){
            int x, y;
            boolean overlap;
            do {
                x = random.nextInt(width - 2) + 1;
                y = random.nextInt(height - 2) + 1;

                //Check collision with other coins
                overlap = false;
                for(Monster monster : monsters){
                    if(monster.getPosition().equals(new Position(x, y))) overlap = true;
                }

            } while(overlap || !hasNoCollisions(new Position(x, y), true));
            monsters.add(new Monster(x, y));
        }
        return monsters;
    }


    private boolean hasNoCollisions(Position position, boolean checkHero){
        //Check collision with walls
        for(Wall wall : walls){
            if(wall.getPosition().equals(position)){
                return false;
            }
        }

        if(checkHero && position.equals(hero.getPosition())) return false;
        return true;
    }

    private boolean canMoveHero(Position position){
        //Check collision with walls
        for(Wall wall : walls){
            if(wall.getPosition().equals(position)){
                return false;
            }
        }
        return !(position.getX() < 0 || position.getX() >= width || position.getY() < 0 || position.getY() >= height);
    }

    /**
     * Update hero position
     * @param position
     */
    private void moveHero(Position position){
        if(canMoveHero(position)) {
            hero.setPosition(position);
            retrieveCoins();
        }
    };

    private void retrieveCoins(){
        Coin selectedCoin = null;
        for(Coin coin : coins){
            if(coin.getPosition().equals(hero.getPosition())){
                selectedCoin = coin;
                break;
            }
        }
        if(selectedCoin != null){
            coins.remove(selectedCoin);
        }
    }

    private void moveMonsters(){
        Random random = new Random();
        for(Monster monster : monsters){
            Position position;
            do {
                switch (random.nextInt(4)) {
                    case 0 -> position = (monster.moveUp());
                    case 1 -> position = (monster.moveDown());
                    case 2 -> position = (monster.moveLeft());
                    default -> position = (monster.moveRight());
                }
            }while(!hasNoCollisions(position, false));
            monster.setPosition(position);
        }
    }

    public boolean verifyMonsterCollisions(){
        for(Monster monster : monsters){
            if(monster.getPosition().equals(hero.getPosition())){
                return true;
            }
        }
        return false;
    }
    /**
     * Uses AI to process the key, moving the hero...
     * @param key
     */
    public void processKey(KeyStroke key){
        switch (key.getKeyType()) {
            case ArrowUp -> moveHero(hero.moveUp());
            case ArrowDown -> moveHero(hero.moveDown());
            case ArrowLeft -> moveHero(hero.moveLeft());
            case ArrowRight -> moveHero(hero.moveRight());
        }
        moveMonsters();
    }

    public void draw(TextGraphics graphics){

        graphics.setBackgroundColor(TextColor.Factory.fromString("#336699"));
        graphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
        for (Wall wall : walls)
            wall.draw(graphics);
        for(Coin coin : coins){
            coin.draw(graphics);
        }
        for(Monster monster : monsters){
            monster.draw(graphics);
        }
        hero.draw(graphics);
    }

}
