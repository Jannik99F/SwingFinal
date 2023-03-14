import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Clickpage extends JFrame {
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JButton[] buttons;
    private int score;
    private int timeLeft;
    private int greenButtonIndex;
    private Timer timer;
    private final Random random;
    private final Player player = new Player();

    // Name wird dem Konstruktor übergeben, daraus wird ein Player Objekt erstellt
    public Clickpage(String name) {

        player.setName(name);
        setTitle("Click-Competition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        score = 0;
        timeLeft = 30;
        greenButtonIndex = -1;
        random = new Random();


        // Es wird ein topPanel erstellt, welches die Labels für Score und Time enthält
        // Es wird ein bottomPanel erstellt, welches die Buttons enthält
        // Timer wird erstellt
        createTopPanel();
        createBottomPanel();
        createTimer();

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel topPanel;
    private void createTopPanel() {
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timeLabel = new JLabel("Time: " + timeLeft);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(scoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.NORTH);
    }

    // Es wird eine Farbe erstellt, die bei einem falschen Klick auf die Buttons angezeigt wird
    Color backgroundColor = new Color(255, 120, 120);
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttons = new JButton[4];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(150, 100));
            buttons[i].setEnabled(false);
            int buttonIndex = i;
            buttons[i].addActionListener(e -> {
                if (buttonIndex == greenButtonIndex) {
                    score += timeLeft <= 15 ? 2 : 1; // wenn weniger als 15 Sekunden übrig sind, gibt es 2 Punkte, sonst 1
                } else {
                    score--;
                    topPanel.setBackground(backgroundColor);
                    bottomPanel.setBackground(backgroundColor);
                    Timer timer = new Timer(180, e1 -> {
                        // nach 180ms wird die Hintergrundfarbe wieder auf Standard gesetzt
                        topPanel.setBackground(null);
                        bottomPanel.setBackground(null);
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                scoreLabel.setText("Score: " + score);
            });
            bottomPanel.add(buttons[i]);
        }
        add(bottomPanel, BorderLayout.CENTER);
    }


    private void createTimer() {
        timer = new Timer(1000, e -> {
            timeLeft--;
            timeLabel.setText("Time: " + timeLeft);
            if (timeLeft == 0) {
                endGame();
            }
            if (greenButtonIndex != -1) {
                int randomTime = (int) (Math.random() * 100);
                if (randomTime < 80) {
                    changeGreenButton();
                }
            }
        });
        timer.start();
        startGame();
    }


    //startgame() setzt die Startwerte für das Spiel
    private void startGame() {
        int randomIndex = random.nextInt(4);
        buttons[randomIndex].setBackground(Color.GREEN);
        greenButtonIndex = randomIndex;
        for (JButton button : buttons) {
            if (button != buttons[randomIndex]) {
                button.setBackground(Color.RED);
            }
            button.setEnabled(true);
        }
    }

    // Der Button, der grün ist, wird auf rot gesetzt und ein neuer Button wird nach Zufallsprinzip auf grün gesetzt
    private void changeGreenButton() {
        buttons[greenButtonIndex].setBackground(Color.RED);
        int randomIndex = random.nextInt(4);
        while (randomIndex == greenButtonIndex) {
            randomIndex = random.nextInt(4);
        }
        buttons[randomIndex].setBackground(Color.GREEN);
        greenButtonIndex = randomIndex;
    }

    // Die Methode endGame() wird aufgerufen, wenn die Zeit abgelaufen ist
    // Die Buttons werden deaktiviert und der Spieler wird zurück zum Startfenster geleitet
    // Der Score wird in die Datei scores.txt geschrieben
    private void endGame() {
        timer.stop();
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
        //TODO: route back to main menu with score in scoreboard
        player.setScore(score);
        ArrayList<Player> players = FileHandler.readFile(Main.path);
        try {
            FileHandler.write(players, player, Main.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        routeBackToStart();
    }
    // Die Methode routeBackToStart() leitet nach Ablauf der Zeit den Spieler zurück zum Startfenster
    private void routeBackToStart() {
        Start startpage = new Start(2);
        startpage.readScoresFromFile();
        startpage.setVisible(true);
        dispose();
    }


}

