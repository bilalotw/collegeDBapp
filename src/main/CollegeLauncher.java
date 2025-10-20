package main;

import ui.LoginUI;

public class CollegeLauncher {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LoginUI::new);
    }
}
