package View;

import javax.swing.*;
import java.util.ArrayList;

public abstract class Menu {
    protected ArrayList<JButton> buttonList = new ArrayList<>();

    public Menu() {
    }

    public ArrayList<JButton> getButtonList() {
        return buttonList;
    }
}
