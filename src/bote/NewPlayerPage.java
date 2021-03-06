package bote;

import asciiPanel.Render;
import asciiPanel.TileTransformer;
import bote.game.Player;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NewPlayerPage extends Page {

    private char[] name;
    private int index;

    public NewPlayerPage() {
        name = new char[10];
        index = 0;
    }

    @Override
    public Color getBackgroundColor() {
        return Color.BLACK;
    }

    @Override
    public Color getForegroundColor() {
        return new Color(0, 170, 255);
    }

    @Override
    public TileTransformer[] getDefaultAnimation() {
        return new TileTransformer[]{};
    }
    
    @Override
    public Render[][] getDefaultDraw() {
        Render[][] temp = new Render[][]{
            makeRenderArray(60, 29, Color.YELLOW, getBackgroundColor(), "press (enter) to finalize your name".toCharArray()),
            makeRenderArray(18, 8, Color.WHITE, getBackgroundColor(), "Go ahead and type your name:".toCharArray())
        };
        return temp;
    }

    @Override
    public Command pageAction(int key) {
//        System.out.println((char) key + " " + index);
        if (key > 256) {
            return new Command() {
                @Override
                public void exe(Controller c) {
                }
            };
        }
        if (key == 10) {
            return new Command() {
                @Override
                public void exe(Controller c) {
                    String plName = "";
                    for (int i = 0; i < name.length; i++) {
                        if (name[i] < 256) {
                            plName = plName + name[i] + "";
                        }
                    }
                    // trim gets rid of messy null char spaces
                    plName = plName.trim();
                    Player player = new Player(plName);
                    //here is one of the only times we dont use seed based rand
                    Random r = new Random();
                    player.setX(r.nextInt());
                    player.setY(r.nextInt());
                    //set new player
                    c.newPlayer(player);
                    c.setPage(new MainPage(player.getX(), player.getY(),
                            c.getScreenWidth(), c.getScreenHeight()));
                }
            };
        }
        switch (key) {
            case 8://backsp
                if (index > 0) {
                    index = index - 1;
                    name[index] = ' ';
                }
                break;
            default:
                if (index < 10) {
                    name[index] = (char) key;
                    index++;
                }
        }
        return new Command() {
            @Override
            public void exe(Controller c) {
                c.addDraw(makeRenderArray(20, 10, getForegroundColor(), getBackgroundColor(), name));
            }
        };
    }

    @Override
    public Viewer playViewer() {
        return null;
    }

    private static Render[] makeRenderArray(int x, int y, Color fg, Color bg, char[] img) {
        List<Render> temp = new LinkedList<>();
        for (int col = 0; col < img.length; col++) {
            temp.add(new Render(img[col], x + col, y, fg, bg));
        }
        return temp.toArray(new Render[temp.size()]);
    }

}
