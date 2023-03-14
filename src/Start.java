import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class Start extends JFrame {

    public static JTextArea scoresTextArea;
    private final JTextField nameInput = new JTextField();
    private final JButton startButton = new JButton("Start");
    private String playerName; // Name des Spielers

    public static Clickpage clickpage;

    Start(int j){ //j = 1 wenn es das erste Spiel ist, wird eingesetzt um Survey erst nach einmaligem Spiel zu zeigen
        setTitle("StartPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());


        //evalLink ist der Link zur Survey
        JLabel evalLink = createLinkLabel("Click here to take our survey", "https://forms.gle/wsoBcjnE9GfFPiF7A");
        evalLink.setMaximumSize(new Dimension(165, 40));
        evalLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Wenn es das erste Spiel ist, wird der Link zur Survey nicht angezeigt
        if(j == 1)
            evalLink.setVisible(false);
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

        scrollPane.setMaximumSize(new Dimension(330, 40));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        readScoresFromFile();
        add(scrollPane);

        add(Box.createVerticalStrut(20));

        nameInput.setMaximumSize(new Dimension(400, 40));
        nameInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameInput.setText("Enter your name here");

        nameInput.addFocusListener(new FocusAdapter() {
            //Methode damit Nameinput nur dann Text hat wenn es nicht geklickt wird
            @Override
            public void focusGained(FocusEvent e) {
                nameInput.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameInput.getText().isEmpty()) {
                    nameInput.setText("Enter your name here");
                }
            }
        });
        add(nameInput);

        add(Box.createVerticalStrut(20));


        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //startButton routet zu Clickpage
        startButton.addActionListener(e -> {
            if (e.getSource() == startButton) {
                setPlayerName();
                routeToClickpage();
            }
        });
        add(startButton);

        add(Box.createVerticalGlue());

        // center horizontally
        setLocationRelativeTo(null);

        setVisible(true);
    }
    private JLabel createLinkLabel(String label, String url) { //Methode um Link zu erstellen
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
        String input = nameInput.getText().trim(); // trim() entfernt Leerzeichen am Anfang und Ende
        // Wenn der Spieler keinen Namen eingegeben hat, wird "John_Doe" als Name verwendet
        playerName = (input.isEmpty() || input.equals("Enter your name here")) ? "John_Doe" : input.replaceAll("\\s", "_");
    }
    private void routeToClickpage() {
        clickpage = new Clickpage(playerName);
        clickpage.setVisible(true);
        dispose(); // Startseite wird geschlossen
    }
}
