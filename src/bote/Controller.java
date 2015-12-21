package bote;

import asciiPanel.AsciiCharacterData;
import asciiPanel.Render;
import asciiPanel.TileTransformer;
import bote.game.DayClock;
import bote.game.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Controller {

    private static final Controller controller = new Controller();
    private static Screen screen;
    private static TextArea console;
    private Player player;
    private Page page;
    private DayClock clock;

    public Controller() {
        screen = Screen.getInstance();
        console = TextArea.getInstance();
        setPage(new IntroPage());
        clock = new DayClock();
    }

    public final void setPage(Page p) {
        this.clearDraws();
        page = p;
        screen.setBackgroundColor(page.getBackgroundColor());
        screen.setForegroundColor(page.getForegroundColor());
        screen.addAnimation(page.getDefaultAnimation());
        //play viewer
        this.playViewer(page.playViewer());
        this.addDraw(page.getDefaultDraw());
    }

    public void playViewer(Viewer v) {
        if (v != null) {
            v.play();
            while (v.isPlaying()) {
                screen.clearScreen();
                screen.addDraw(v.getCurrentRender());
                screen.render();
                v.stop();
            }
        }
    }

    public void transform(int x, int y, AsciiCharacterData data) {
        screen.transform(x, y, data);
    }

    public void transform(TileTransformer t) {
        screen.transform(t);
    }
    
    public void addFilter(TileTransformer t){
        screen.filter(t);
    }

    public void console(String out) {
        console.write(out);
    }

    public void render() {
        screen.render();
    }

    public void addAnimation(TileTransformer t) {
        screen.addAnimation(t);
    }

    public void addAnimation(TileTransformer[] t) {
        screen.addAnimation(t);
    }

    public void addDraw(Render r) {
        screen.addDraw(r);
    }

    public void addDraw(Render[] r) {
        screen.addDraw(r);
    }

    public void addDraw(Render[][] r) {
        screen.addDraw(r);
    }

    public void addCol(int x, AsciiCharacterData[] r) {
        if (r.length == this.getScreenHeight()) {
            for (int y = 0; y < r.length; y++) {
                screen.addDraw(new Render(x, y, r[y]));
            }
        }
    }

    public void addRow(int y, AsciiCharacterData[] r) {
        if (r.length == this.getScreenWidth()) {
            for (int x = 0; x < r.length; x++) {
                screen.addDraw(new Render(x, y, r[x]));
            }
        }
    }

    public void addDraw(AsciiCharacterData[][] r) {
        screen.addDraw(r);
    }

    private void execute(Command c) {
        if (c != null) {
            c.exe(getInstance());
        }
    }

    public void clearDraws() {
        screen.clearView();
    }

    public void takeInput(int keyCode) {
        console.write(keyCode + " pressed");
        execute(page.pageAction(keyCode));
//        if (clock.tick().isSunset()) {
//            console.write("The sun sets over the horizon");
//            this.addFilter(new TileTransformer() {
//                @Override
//                public void transformTile(int x, int y, AsciiCharacterData data) {
//                    data.foregroundColor = data.foregroundColor.darker();
//                    data.backgroundColor = data.backgroundColor.darker();
//                }
//            });
//        }
//        if (clock.tick().isSunrise()) {
//            console.write("The sun rises");
//            this.addFilter(new TileTransformer() {
//                @Override
//                public void transformTile(int x, int y, AsciiCharacterData data) {
//                    data.foregroundColor = data.foregroundColor.brighter();
//                    data.backgroundColor = data.backgroundColor.brighter();
//                }
//            });
//        }
//        switch (keyCode) {
//            case 65://a
//            case 37://left
//                break;
//            case 87://w
//            case 38://up
//                break;
//            case 68://d
//            case 39://right
//                break;
//            case 83://s
//            case 40://down
//                break;
//        }
    }

    public Player getPlayer() {
        return player;
    }

    public void newPlayer(Player p) {
        player = p;
        this.save();
    }

    public Player loadGame() {
        Player load = null;
        try {
            FileInputStream fIn = new FileInputStream("pl.data");
            ObjectInputStream objIn = new ObjectInputStream(fIn);
            load = (Player) objIn.readObject();
        } catch (Exception e) {
            System.out.println("I didn't load the player");
        }
        try {
            FileInputStream fIn = new FileInputStream("tm.data");
            ObjectInputStream objIn = new ObjectInputStream(fIn);
            clock = (DayClock) objIn.readObject();
        } catch (Exception e) {
            System.out.println("I didn't load the clock");
        }
        System.out.println(load.toString());
        return load;
    }

    public void save() {
        try {
            File f = new File("pl.data");
            f.delete();
        } catch (Exception io) {
            System.out.println("couldn't delete the pl.data file");
        }
        try {
            FileOutputStream fOut = new FileOutputStream("pl.data");
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(player);
            fOut.close();
            objOut.close();
        } catch (Exception e) {
            System.out.println("I didn't save it");
        }
        
        try {
            File f = new File("tm.data");
            f.delete();
        } catch (Exception io) {
            System.out.println("couldn't delete the tm.data file");
        }
        try {
            FileOutputStream fOut = new FileOutputStream("tm.data");
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(clock);
            fOut.close();
            objOut.close();
        } catch (Exception e) {
            System.out.println("I didn't save the clock");
        }
        System.out.println("saving done");
    }

    public int getScreenWidth() {
        return screen.getAsciiPanelWidth();
    }

    public int getScreenHeight() {
        return screen.getAsciiPanelHeight();
    }

    public static Controller getInstance() {
        if (controller == null) {
            Controller controller = new Controller();
        }
        return controller;
    }
}
