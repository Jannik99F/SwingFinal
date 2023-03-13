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

    //TODO: DONE Konstruktor mit String für namen übergeben und Player Objekt mit namen erzeugen
    //TODO: Fenster bei erzeugung vergrößern
    public Clickpage(String name) {

        player.setName(name);
        setTitle("My Swing Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        score = 0;
        timeLeft = 30;
        greenButtonIndex = -1;
        random = new Random();

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
    Color backgroundColor = new Color(255, 120, 120);
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttons = new JButton[4];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton();
            buttons[i].setPreferredSize(new Dimension(150, 100));
            buttons[i].setEnabled(false);
            int buttonIndex = i;
            buttons[i].addActionListener(e -> {
                if (buttonIndex == greenButtonIndex) {
                    score += timeLeft <= 15 ? 2 : 1;
                } else {
                    score--;
                    topPanel.setBackground(backgroundColor);
                    bottomPanel.setBackground(backgroundColor);
                    Timer timer = new Timer(180, e1 -> {
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
            if (greenButtonIndex != -1 && timeLeft % 2 == 0) {
                changeGreenButton();
            }
        });
        timer.start();
        startGame();
    }

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

    private void changeGreenButton() {
        buttons[greenButtonIndex].setBackground(Color.RED);
        int randomIndex = random.nextInt(4);
        while (randomIndex == greenButtonIndex) {
            randomIndex = random.nextInt(4);
        }
        buttons[randomIndex].setBackground(Color.GREEN);
        greenButtonIndex = randomIndex;
    }

    private void endGame() {
        timer.stop();
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
        //TODO: route back to main menu with score in scoreboard
        player.setScore(score);
        ArrayList<Player> players = FileHandler.readFile("src/scores.txt");
        try {
            FileHandler.write(players, player, "src/scores.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        routeBackToStart();
    }
    private void routeBackToStart() {
        Start startpage = new Start(2);
        startpage.readScoresFromFile();
        startpage.setVisible(true);
        dispose();
    }


}

