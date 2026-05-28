package game.Screens;

import bagel.Input;

public interface Screen {
    public abstract void update(Input input);
    public abstract void draw();
}
