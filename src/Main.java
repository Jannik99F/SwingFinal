import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        //Code um Nimbus Design zu verwenden
        //source: https://stackoverflow.com/questions/4617615/how-to-set-nimbus-look-and-feel-in-main
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("An error has occurred, program exit.");
            }
        }
        EventQueue.invokeLater(() -> {
            try {
                Start start = new Start( 1);//Variable damit festgestellt werden kann ob es das erste Spiel ist
                start.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}