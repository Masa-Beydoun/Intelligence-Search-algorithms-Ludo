import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameGui extends JFrame {

    List<State> visited = new ArrayList<>();
    Icon[] nerdImages = {
            new ImageIcon("1.png"),
            new ImageIcon("2.png"),
            new ImageIcon("3.png"),
            new ImageIcon("4.png"),
            new ImageIcon("5.png"),
            new ImageIcon("6.png"),
    };
    Icon[] stonesImages = {
            new ImageIcon("red.png"),
            new ImageIcon("green.png"),
            new ImageIcon("yellow.png"),
            new ImageIcon("blue.png"),
    };
    State state;
    JButton[][] buttons;
    JPanel game, southPanel;
    JButton nerdButton = new JButton();
    JButton nextButton = new JButton("Next");
    JLabel turnLabel;
    int ran = 0;

    GameGui(State state, String mode) {
        this.state = state;
        createGrid(mode);

        southPanel = new JPanel(new FlowLayout());
        nerdButton.setPreferredSize(new Dimension(40, 40));
        System.out.println("state gui : "+ state);
        turnLabel = new JLabel("NEXT Turn: " + (state.turn) + " ,  possibility " + state.possibility + ",  heu "+ Heuristic.calculateHeuristic(state) );
        southPanel.add(nerdButton);
        southPanel.add(turnLabel);
        if(!mode.equals("user"))southPanel.add(nextButton);
        JButton startButton = new JButton("print");
        southPanel.add(startButton);
        startButton.addActionListener(e->{
            Heuristic.printingHeuristic(this.state);
        });
        nerdButton.addActionListener(e -> nerdActionListener(mode));
        nextButton.addActionListener(e -> nextActionListener(mode));
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(game, BorderLayout.CENTER);
        this.setBounds(0, 0, 15 * 52, 15 * 50 + 50);
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        refreshStones();
    }

    private void nextActionListener(String mode) {

        if(mode.equals("simple") || mode.equals("advanced")) {
            this.dispose();
            List<State>updatedStates = state.getListWithoutRep(mode);
            System.out.println("without rep : " + updatedStates.size());
            for (State nextState : updatedStates) {
                new GameGui(nextState, mode);
            }
        }
        else if(mode.equals("simpleAlgorithm")){
            Algorithm algorithm = new Algorithm(AlgorithmType.SIMPLE);
            this.state = algorithm.bestState(state, ran);
            this.state.played = true;
            refreshStones();
//            new GameGui(state,mode);
        }
        else if(mode.equals("advancedAlgorithm")){
            Algorithm algorithm = new Algorithm(AlgorithmType.ADVANCED);
            this.state = algorithm.bestState(state, ran);
            this.state.played = true;
            refreshStones();
        }

    }

    public void nerdActionListener(String mode) {
        if (!state.played) return;
        ran = state.getNerdNumber(mode);
        nerdButton.setIcon(nerdImages[ran - 1]);
        if (state.played) {
            turnLabel.setText("NEXT Turn: " + (state.turn));
        }
        if(mode.equals("simpleAlgorithm")){
            Algorithm algorithm = new Algorithm(AlgorithmType.SIMPLE);
            this.state = algorithm.bestState(state, ran);
            this.state.played = true;
            refreshStones();
//            new GameGui(state,mode);
        }
    }

    public void refreshStones() {

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setIcon(null);
            }
        }

        for (Player player : state.players) {
            for (Stone s : player.stones) {
                buttons[s.position.i][s.position.j].setIcon(stonesImages[player.playerID]);
            }
        }
        turnLabel.setText("Next turn : " + state.turn + " , possibility : "+state.possibility+ " heu : "+ Heuristic.calculateHeuristic(state) );

    }

    public void createGrid(String mode) {
        this.setLayout(new BorderLayout());
        game = new JPanel(new GridLayout(16, 16));
        buttons = new JButton[16][16];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                buttons[i][j] = new JButton();
                if (i == 15) {
                    buttons[i][j].setText(String.valueOf(j));
                    game.add(buttons[i][j]);
                    continue;
                }
                if (j == 15) {
                    buttons[i][j].setText(String.valueOf(i));
                    game.add(buttons[i][j]);
                    continue;
                }
                buttons[i][j].setText("");
                int finalI = i;
                int finalJ = j;
                if(mode.equals("user"))buttons[i][j].addActionListener(e -> moveListener(finalI, finalJ));

                if ((i == 7 && j >= 1 && j <= 5) || (i == 6 && j == 1)) buttons[i][j].setBackground(Color.red);
                else if ((i == 7 && j >= 9 && j <= 13) || (i == 8 && j == 13))
                    buttons[i][j].setBackground(Color.YELLOW);
                else if ((j == 7 && i >= 1 && i <= 5) || (i == 1 && j == 8)) buttons[i][j].setBackground(Color.green);
                else if ((j == 7 && i >= 9 && i <= 13) || (i == 13 && j == 6)) buttons[i][j].setBackground(Color.blue);
                    //////////////////////////////////////////////////////////////
                else if (i == 8 && j == 2)
                    buttons[i][j].setBackground(Color.red);
                else if ((i == 0 && j <= 5) || (i <= 5 && j == 0) || (i == 5 && j <= 5) || (i <= 5 && j == 5))
                    buttons[i][j].setBackground(new Color(100, 0, 40));
                    ////////////////////////////////////////////////////////////
                else if (i == 2 && j == 6)
                    buttons[i][j].setBackground(Color.green);
                else if (((i == 0 || i == 5) && j >= 9) || (i <= 5 && (j == 9 || j == 14)))
                    buttons[i][j].setBackground(new Color(0, 100, 0));
                    //////////////////////////////////////////////////////////
                else if (i == 12 && j == 8)
                    buttons[i][j].setBackground(Color.blue);
                else if ((i == 14 && j <= 5) || (i == 9 && j <= 5) || (i >= 9 && j == 0) || (i >= 10 && j == 5))
                    buttons[i][j].setBackground(new Color(40, 0, 100));
                    ////////////////////////////////////////////////////////////
                else if (i == 6 && j == 12)
                    buttons[i][j].setBackground(Color.yellow);
                else if ((i == 14 && j >= 9) || (i == 9 && j >= 9) || (i >= 9 && j == 14) || (i >= 9 && j == 9))
                    buttons[i][j].setBackground(new Color(150, 150, 0));
                    ////////////////////////////////////////////////////////////

                else if (i >= 6 && i <= 8 && j >= 6 && j <= 8) buttons[i][j].setBackground(Color.black);
                else buttons[i][j].setBackground(Color.WHITE);

                if (((i >= 6 && i <= 8) || (j >= 6 && j <= 8)) && !(i >= 6 && i <= 8 && j >= 6 && j <= 8))
                    buttons[i][j].setBorder(new LineBorder(Color.GRAY, 4));
                buttons[i][j].setFocusable(false);
                game.add(buttons[i][j]);
            }

        }
    }

    public void moveListener(int i, int j) {
        System.out.println(state);
        if (state.played) {
            System.out.println("already did the move");
            System.out.println();
            return;
        }
        System.out.println("cell in " + i + " " + j + " have been clicked");

        List<Stone> s = state.players.get(state.turn).getStoneInPlace(i, j);
        if (s.isEmpty()) return;
        System.out.println("stone in place " + s);
        State state1 = state.move(s.get(0).id, ran);
        if (state1 == null) return;
        System.out.println("state 1 is played : " + state1.played);
        if (!state1.played) return;

        state = state1;
        System.out.println("stateeeeeeeeeeeeeeeeeee");
        System.out.println(state);
        turnLabel.setText("NEXT Turn: " + (state.turn) + " ,  possibility "+state.possibility + ",  heu "+Heuristic.calculateHeuristic(state));
        System.out.println("the move has been processed in GameGui class");

//        System.out.println("next states \n " +state.nextStates());
        refreshStones();
    }
}

