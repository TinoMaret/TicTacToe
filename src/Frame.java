import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;

public class Frame implements ActionListener {
    JFrame frame;
    JLabel label;

    JPanel turnPanel;

    JPanel gamePanel;

    JPanel toolPanel;

    JButton[][] buttons = new JButton[3][3];

    JButton undo = new JButton();

    private char turn;

    private Stack<int[]> Moves = new Stack<>();

    public Frame() {
        Turn();

        turnPanel = new JPanel();
        turnPanel.setPreferredSize(new Dimension(500, 100));
        turnPanel.setBackground(Color.darkGray);
        gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(500, 400));
        gamePanel.setBackground(Color.black);
        gamePanel.setLayout(new GridLayout(3, 3));
        toolPanel = new JPanel();
        toolPanel.setPreferredSize(new Dimension(500,100));
        toolPanel.setBackground(Color.darkGray);

        label = new JLabel();
        label.setText("Player \"" + turn + "\" turn");
        label.setForeground(Color.magenta);
        label.setFont(new Font("Serif", Font.BOLD, 30));

        turnPanel.add(label);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].addActionListener(this);
                buttons[i][j].setFocusable(false);
                gamePanel.add(buttons[i][j]);
            }
        }
        undo.setFont(new Font("Arial", Font.PLAIN,20));
        undo.setText("Undo move");
        undo.setFocusable(false);
        undo.addActionListener(this);
        undo.setEnabled(false);
        toolPanel.add(undo);

        frame = new JFrame();
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout(0, 0));
        frame.add(turnPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(toolPanel, BorderLayout.SOUTH);
        frame.pack();
    }


    private void Turn() {
        Random r = new Random();
        int choice = r.nextInt(2);
        if (choice == 0)
            turn = 'X';
        else
            turn = 'O';
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == undo){
            int[] moveToUndo = Moves.pop();
            buttons[moveToUndo[0]][moveToUndo[1]].setText("");
            buttons[moveToUndo[0]][moveToUndo[1]].addActionListener(this);
            checkIfEmpty();

        }
        else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (e.getSource() == buttons[i][j]) {
                        buttons[i][j].setForeground(Color.GREEN);
                        buttons[i][j].setFont(new Font("MV Boli", Font.BOLD, 120));
                        buttons[i][j].setText(Character.toString(turn));
                        buttons[i][j].removeActionListener(this);
                        Moves.add(new int[]{i, j});
                        checkIfEmpty();
                    }
                }
            }
        }
        if (checkWin()) {
            if (turn == 'X')
                turn = 'O';
            else
                turn = 'X';
            label.setText("Player \"" + turn + "\" turn");
        } else {
            label.setText("Player " + turn + " won!");
            endGame();
        }

    }

    public boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (Objects.equals(buttons[i][0].getText(), buttons[i][1].getText()) && Objects.equals(buttons[i][1].getText(), buttons[i][2].getText())
                    && !Objects.equals(buttons[i][0].getText(), "")) {
                return false;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (Objects.equals(buttons[0][i].getText(), buttons[1][i].getText()) && Objects.equals(buttons[1][i].getText(), buttons[2][i].getText())
                    && !Objects.equals(buttons[0][i].getText(), "") && !Objects.equals(buttons[1][i].getText(), "")) {
                return false;
            }
        }

        if (Objects.equals(buttons[0][0].getText(), buttons[1][1].getText()) && Objects.equals(buttons[1][1].getText(), buttons[2][2].getText())
                && !Objects.equals(buttons[0][0].getText(), "")) {
            return false;
        } else if (Objects.equals(buttons[0][2].getText(), buttons[1][1].getText()) && Objects.equals(buttons[1][1].getText(), buttons[2][0].getText())
                && !Objects.equals(buttons[2][0].getText(), "")) {
            return false;
        }
        return true;
    }


    public void endGame(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].removeActionListener(this);
            }
        }
        undo.setEnabled(false);
    }

    public void checkIfEmpty(){
        if(Moves.empty())
            undo.setEnabled(false);
        else
            undo.setEnabled(true);
    }


}
