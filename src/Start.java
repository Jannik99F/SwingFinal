import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class Start extends JFrame {

    public static JTextArea scoresTextArea;
    private final JTextField nameInput = new JTextField();
    private final JButton startButton = new JButton("Start");
    private String playerName; // Name des Spielers

    public static Clickpage clickpage;

    Start(){
        setTitle("StartPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());


        JLabel evalLink = createLinkLabel("Click here to take our survey", "https://forms.gle/wsoBcjnE9GfFPiF7A");
        evalLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(evalLink);

        add(Box.createVerticalStrut(20));

        JLabel scoreLabel = new JLabel();
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scoreLabel);

        add(Box.createVerticalStrut(20));

        scoresTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(scoresTextArea);
        {
            scrollPane.setPreferredSize(new Dimension(400, 300));
        }
        scrollPane.setViewportView(scoresTextArea);
        scoresTextArea.setEditable(false);
        scoresTextArea.setLineWrap(true);
        scoresTextArea.setWrapStyleWord(true);

        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        readScoresFromFile();
        add(scrollPane);

        add(Box.createVerticalStrut(20));

        nameInput.setMaximumSize(new Dimension(400, 40));
        nameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(nameInput);

        add(Box.createVerticalStrut(20));


        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.addActionListener(e -> {
            if (e.getSource() == startButton) {
                setPlayerName();
                showClickpage();
            }
        });
        add(startButton);

        add(Box.createVerticalGlue());

        // center horizontally
        setLocationRelativeTo(null);

        setVisible(true);
    }
    private JLabel createLinkLabel(String label, String url) {
        JLabel linkLabel = new JLabel("<html><u>" + label + "</u></html>");
        linkLabel.setToolTipText(url);
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return linkLabel;
    }
    //https://stackoverflow.com/questions/15798532/how-to-clear-jtextarea#:~:text=JTextArea0.,string%2C%20effectively%20clearing%20the%20JTextArea.
    /*
    public void readScoresFromFile() {
        URL url = getClass().getResource("/scores.txt");
        try {
            assert url != null;
            try (InputStream inputStream = url.openStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                scoresTextArea.selectAll();
                scoresTextArea.replaceSelection("");

                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" ");
                    int score = Integer.parseInt(parts[0].replace("(", "").replace(")", ""));
                    String name = parts[1];
                    scoresTextArea.append(name + "  =  " + score + "\n");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
    public void readScoresFromFile() {
        try {
            File file = new File("src/scores.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            scoresTextArea.selectAll();
            scoresTextArea.replaceSelection("");

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int score = Integer.parseInt(parts[0].replace("(", "").replace(")", ""));
                String name = parts[1];
                scoresTextArea.append(name + "  =  " + score + "\n");
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setPlayerName() {
        String input = nameInput.getText().trim();
        playerName = input.isEmpty() ? "John Doe" : input;
    }
    private void showClickpage() {
        clickpage = new Clickpage(playerName);
        clickpage.setVisible(true);
        dispose(); // ersetzen durch setVisible(false) dann muss noch highscore update erfolgen
    }
}
