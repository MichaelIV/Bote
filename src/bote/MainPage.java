package bote;

import asciiPanel.AsciiCharacterData;
import asciiPanel.Render;
import asciiPanel.TileTransformer;
import bote.game.Fish;
import bote.game.WorldMap;
import java.awt.Color;

public class MainPage extends Page {

    private final WorldMap world;
    private final Viewer viewer;

    public MainPage(int x, int y, int w, int h) {
        world = new WorldMap(x, y, w, h);
        viewer = new Viewer();
        viewer.addRenderArray(world.getFullDraw());
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(50, 120, 255);
    }

    @Override
    public Color getForegroundColor() {
        return Color.WHITE;
    }

    @Override
    public TileTransformer[] getDefaultAnimation() {
        return new TileTransformer[]{
            new TileTransformer() {
                @Override
                public void transformTile(int x, int y, AsciiCharacterData data) {
                    if (x == 60 && y == 20) {
                        data.character = ImageLib.ALPHA;
                        data.foregroundColor = new Color(200, 100, 0);
                    }
                }
            }
        };
    }

    @Override
    public Render[][] getDefaultDraw() {
        return new Render[][]{};
    }

    @Override
    public Command pageAction(int key) {
        switch (key) {
            case 65://a
            case 37://left
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        if (world.getDraw(c.getPlayer().getX() - 1, c.getPlayer().getY()).character == '~') {
                            c.getPlayer().decX();
                            world.decPlayerX();
                            c.transform(-1, 0, null);
                            c.addCol(0, world.getLeftCol());
                        }
                    }
                };
            case 87://w
            case 38://up
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        if (world.getDraw(c.getPlayer().getX(), c.getPlayer().getY() - 1).character == '~') {
                            c.getPlayer().decY();
                            world.decPlayerY();
                            c.transform(0, 1, null);
                            c.addRow(0, world.getTopRow());
                        }
                    }
                };
            case 68://d
            case 39://right
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        if (world.getDraw(c.getPlayer().getX() + 1, c.getPlayer().getY()).character == '~') {
                            c.getPlayer().incX();
                            world.incPlayerX();
                            c.transform(1, 0, null);
                            c.addCol(c.getScreenWidth() - 1, world.getRightCol());
                        }
                    }
                };
            case 83://s
            case 40://down
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        if (world.getDraw(c.getPlayer().getX(), c.getPlayer().getY() + 1).character == '~') {
                            c.getPlayer().incY();
                            world.incPlayerY();
                            c.transform(0, -1, null);
                            c.addRow(c.getScreenHeight() - 1, world.getBottomRow());
                        }
                    }
                };
            case 70: //f - goFishing
                return new Command() {
                    @Override
                    public void exe(Controller c) {
//                      //fetch goFishing
                        Fish caught = c.goFishing(world.getCurrentBiome());
                        if (caught != null) {
                            c.console("You pull up a " + caught.getName());
                        } else {
                            c.console("Nothing bites.");
                        }
                        c.console("You cast your rod...");
                    }
                };
            case 27: //esc
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.console("saved");
                        c.save();
                    }
                };
            case 77: //m
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.transform(new TileTransformer() {
                            @Override
                            public void transformTile(int x, int y, AsciiCharacterData data) {
                                data.foregroundColor = data.foregroundColor.darker();
                                data.backgroundColor = data.backgroundColor.darker();
                            }
                        }
                        );
                    }
                };
            case 82: //r
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        String s = "";
                        Fish[] fishes = c.getFish(world.getCurrentBiome());
                        if (fishes.length == 0) {
                            s = "none that you remember.";
                        }
                        for (Fish f : fishes) {
                            s += f.getName() + ", ";
                        }
                        c.console("You recall the fish in this area... there were " + s);
                    }
                };
            case 79: //o
                return new Command() {
                    @Override
                    public void exe(Controller c) {
                        c.console("You observe your surroundings. You find yourself in " + world.getCurrentBiome().toString() + " waters.");
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
        return viewer;
    }

}
