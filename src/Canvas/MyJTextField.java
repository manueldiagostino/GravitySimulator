package Canvas;

import javax.swing.*;
import java.awt.event.*;

public class MyJTextField extends JTextField {
    public final ControlsPanel cpanel;

    public MyJTextField(ControlsPanel cpanel, String s) {
        super(s);
        this.cpanel = cpanel;

        this.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "update");
        this.getActionMap().put("update", cpanel.getUpdateAction());
    }
}
