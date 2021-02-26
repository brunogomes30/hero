import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {
    private Screen screen;

    private Arena arena;
    public Game() {
        arena = new Arena(80, 40);
        try {
            //Terminal terminal = new DefaultTerminalFactory().createTerminal();
            TerminalSize terminalSize = new TerminalSize(80, 40);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();

            screen = new TerminalScreen(terminal);

            screen.setCursorPosition(null);
            screen.startScreen();
            screen.doResizeIfNecessary();
        } catch(IOException exception){
            exception.printStackTrace();
        }

    }

    private void  draw(){
        screen.clear();
        arena.draw(screen.newTextGraphics());
        try {
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void processKey(KeyStroke key){
        arena.processKey(key);
    }

    public void run() throws IOException{
        draw();
        KeyStroke key;

        do{
            key = screen.readInput();
            processKey(key);
            draw();
            if(arena.verifyMonsterCollisions()){
                System.out.println("End game!");
                break;
            }
        }while(!(KeyType.Character == key.getKeyType() && key.getCharacter() == 'q'));
    }
}
