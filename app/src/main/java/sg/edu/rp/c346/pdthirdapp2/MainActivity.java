package sg.edu.rp.c346.pdthirdapp2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;
    private int aiPoints;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewTurn;

    public int index;
    private Button ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_1);
        textViewPlayer2 = findViewById(R.id.text_view_2);
        textViewTurn = findViewById(R.id.textViewTurn);

        for(int i =0; i < 3; i ++){
            for(int j = 0; j < 3; j ++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        Button buttonResetBoard = findViewById(R.id.button_resetBoard);
        buttonResetBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetBoard();
            }
        });

        Intent i = getIntent();
        index = i.getIntExtra("index", 0);

        if (index == 0) {
            textViewPlayer2.setText("AI : 0");
            textViewTurn.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        // same when AI and player
        if (!((Button)view).getText().toString().equals("")){
            return;
        }
        if (index == 1) {
            if (player1Turn) {
                textViewTurn.setText("Player 2 Turn!");
                ((Button) view).setText("X");
            } else {
                textViewTurn.setText("Player 1 Turn!");
                ((Button) view).setText("O");
            }
            roundCount++;

            if (checkWin() == true) {
                if (player1Turn) {
                    player1Win();
                } else {
                    player2Win();
                }
                unClickable();
            } else if (roundCount == 9) {
                draw();
                unClickable();
            } else {
                player1Turn = !player1Turn;
            }
        }

        if (index == 0){ // play with AI
            if(player1Turn){ // player 1 turn
                ((Button) view).setText("X");

                roundCount++;
                if (checkWin() == true){
                    player1Win();
                    unClickable();
                    return;
                }
            }
            String[] array = {"00","01", "02", "10", "11", "12", "20", "21", "22"};
            Collections.shuffle(Arrays.asList(array));
            String buttonID = "button_" + array[0];

            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            ai = findViewById(resID); // int
            String text = ai.getText().toString();

            while (!text.equals("")){
                Collections.shuffle(Arrays.asList(array));
                buttonID = "button_" + array[0];
                resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                ai = findViewById(resID);
                text = ai.getText().toString();
            }

            ai.setText("O");
            Log.d("player 1 turn", "" + player1Turn);
            roundCount++;

            if (checkWin() == true) {
                if (player1Turn) {
                    player1Win();
                } else {
                    aiWin();
                }
                unClickable();
            } else if (roundCount == 9) {
                draw();
                unClickable();
            }
        }
    }

    private boolean checkWin(){
        String [][] test = new String[3][3];
        for (int i = 0; i < 3 ; i ++){
            for (int j = 0; j < 3; j ++){
                test[i][j] = buttons[i][j].getText().toString();
                String ii = String.valueOf(i);
                String jj = String.valueOf(j);
                Log.d("abcd", ii + "," + jj + " " + test[i][j]);
            }
        }
        // [0,0] [0,1] [0,2]
        // [1,0] [1,1] [1,2]
        // [2,0] [2,1] [2,2]

        // da heng
        for (int i = 0; i < 3; i ++){
            if (!test[i][0].equals("") && test[i][0].equals(test[i][1]) && test[i][0].equals(test[i][2])  && test[i][1].equals(test[i][2]) ){
                return true;
            }
        }
        // da zhi
        for (int i = 0; i < 3; i ++){
            if (!test[0][i].equals("") && test[0][i].equals(test[1][i]) && test[0][i].equals(test[2][i]) && test[1][i].equals(test[2][i]) ){
                return true;
            }
        }
        // zuo shang wang you xia
        if (!test[0][0].equals("") && test[0][0].equals(test[1][1]) && test[0][0].equals(test[2][2])){
            return true;
        }
        // you shang wang zuo xia
        if (!test[0][2].equals("") && test[0][2].equals(test[1][1]) && test[0][2].equals(test[2][0])){
            return true;
        }
        return false;
    }

    private void player1Win() {
        player1Points++;
        Toast.makeText(this, "Player 1 Win!",Toast.LENGTH_LONG).show();
        updatePointsText();
    };
    private void player2Win() {
        player2Points++;
        Toast.makeText(this, "Player 2 Win!", Toast.LENGTH_LONG).show();
        updatePointsText();
    };
    private void aiWin() {
        aiPoints++;
        Toast.makeText(this, "AI Win!", Toast.LENGTH_LONG).show();
        updatePointsText();
    }
    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_LONG).show();
    };

    private void updatePointsText(){
        if (index == 1) {
            textViewPlayer1.setText("Player 1 : " + player1Points);
            textViewPlayer2.setText("Player 2 : " + player2Points);
        } else {
            textViewPlayer1.setText("Player 1 : " + player1Points);
            textViewPlayer2.setText("AI : " + aiPoints);
        }
    }


    private void resetBoard() {
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j ++){
                buttons[i][j].setText("");
            }
        }
        clickable();
        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
        clickable();
    }

    private void clickable() {
        for (int i = 0; i < 3 ; i ++){
            for (int j = 0; j < 3; j ++){
                String buttonID2 = "button_" + i + j;
                int resID2 = getResources().getIdentifier(buttonID2, "id", getPackageName());
                Button button = findViewById(resID2);
                button.setEnabled(true);
            }
        }
    }

    private void unClickable() {
        for (int i = 0; i < 3 ; i ++){
            for (int j = 0; j < 3; j ++){
                String buttonID2 = "button_" + i + j;
                int resID2 = getResources().getIdentifier(buttonID2, "id", getPackageName());
                Button button = findViewById(resID2);
                button.setEnabled(false);
            }
        }
    }

}
