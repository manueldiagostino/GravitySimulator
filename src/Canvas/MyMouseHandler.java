package Canvas;

import MovingPoint.MovingPoint;
import Vectors.Vector;
import Vectors.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MyMouseHandler implements MouseListener, MouseMotionListener {
    private final MyPanel panel;
    public int x;
    public int dx;
    public int y;
    public int dy;

    public MyMouseHandler(MyPanel panel) {
        this.panel = panel;
        this.panel.addMouseListener(this);
        this.panel.addMouseMotionListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
//        panel.getTimer().stop();
        x = mouseEvent.getX();
        y = mouseEvent.getY();

        System.out.println(x + ", " + y);
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        dx = mouseEvent.getX()-x;
        dy = mouseEvent.getY()-y;

        for (MovingPoint mp : panel.getElements())
            if (mp.contains(x,y)) {
                mp.isTrajectory = true;
                mp.velTrajectory = new Vector2D(-5*dx, -5*dy);
                panel.repaint();
            }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        for (MovingPoint mp : panel.getElements())
            if (mp.contains(x,y)) {
                mp.isTrajectory = false;
                mp.setVelocity(new Vector2D(-5*dx, -5*dy));
            }

        panel.getTimer().start();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
