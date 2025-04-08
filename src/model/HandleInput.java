package model;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HandleInput implements KeyListener {

    private boolean up, down, left, right, space;

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isSpace() {
        return space;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) up = true;
        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) down = true;
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) left = true;
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) right = true;
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_X) space = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) up = false;
        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) down = false;
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) left = false;
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) right = false;
        if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_X) space = false;
    }

}
