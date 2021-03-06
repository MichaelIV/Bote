package bote;

import asciiPanel.Render;
import asciiPanel.TileTransformer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class IntroPage extends Page {

    @Override
    public Color getBackgroundColor() {
        return Color.BLACK;
    }

    @Override
    public Color getForegroundColor() {
        return new Color(0, 170, 255);
    }

    @Override
    public Render[][] getDefaultDraw() {
        return ImageLib.getIntro();
    }

    @Override
    public Command pageAction(int key) {
        switch (key) {
            case 67://c
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        //load game data
                        c.loadGame();
                        c.setPage(new MainPage(c.getPlayer().getX(), c.getPlayer().getY(),
                                c.getScreenWidth(), c.getScreenHeight()));
                    }
                };
            case 78://n
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        //new game
                        c.setPage(new NewPlayerPage());
                    }
                };
        }
        return new Command() {
            @Override
            public void exe(Controller c) {
            }
        };
    }

    @Override
    public Viewer playViewer() {
        return null;
    }

    private static Render[] makeRenderArray(int x, int y, Color fg, Color bg, String[] img) {
        List<Render> temp = new ArrayList<>();
        for (int row = 0; row < img.length; row++) {
            for (int col = 0; col < img[row].length(); col++) {
                temp.add(new Render(img[row].charAt(col), x + col, y + row, fg, bg));
            }
        }
        return temp.toArray(new Render[temp.size()]);
    }

    @Override
    public TileTransformer[] getDefaultAnimation() {
        return new TileTransformer[]{};
    }

} 