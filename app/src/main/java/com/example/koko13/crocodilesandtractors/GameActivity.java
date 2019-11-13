package com.example.koko13.crocodilesandtractors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Списък със списък на всички бутони от игровото поле и стойността, която имат
     * всеки запис в ArrayList-а е отделен ред
     * за всеки ред имаме списък с бутоните
     */
    private ArrayList<LinkedHashMap<GridButton, Integer>> rowsButtons = new ArrayList<>();

    private ColorPicker colorPicker;

    private Button gridReadyButton; // бутона за попълнен ред

    /**
     * Пазим последния натиснат бутон от игровото поле,
     * за да му сложим избрания цвят след това от color picker-а
     */
    private Button lastClickedGridButton = null;

    /**
     * Пазим на кой ред сме сега
     */
    private int rowNow = 0;

    // Печелившата комбинация
    private ArrayList<Integer> winCombination = new ArrayList<>();

    /**
     * Списък с всички редове от игровото поле
     */
    private ArrayList<View> playgroundRowsList = new ArrayList<>();

    // Броя на редовете на игровото поле
    private int rowsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        colorPicker = new ColorPicker(this, R.id.colorPicker, this);

        LinearLayout playground = findViewById(R.id.grid);

        rowsCount = gridRowsCount(playground);
        playgroundRowsList = findPlaygroundRows(playground);

        rowsButtons.add(findPlaygroundButtonsByRow((LinearLayout)playgroundRowsList.get(rowNow)));
        GridButton.setOnClickListener(this, rowsButtons.get(rowNow));

        setEmptyIconOnRow((LinearLayout)playgroundRowsList.get(rowNow));

        gridReadyButton = findViewById(R.id.gridReadyButton);
        gridReadyButton.setOnClickListener(this);

        generateWinCombination(7, 11);
        Log.d("Test", winCombination.toString());
    }

    /**
     * Намира и връща всички редове в игровото поле
     * @param playground игровото поле
     * @return връща списък ArrayList<View>, в който всеки запис е отделен ред от игровото поле
     */
    private ArrayList<View> findPlaygroundRows(LinearLayout playground) {
        ArrayList<View> rowsList = new ArrayList<>();

        for(int i=0; i<playground.getChildCount(); i++) {
            rowsList.add(playground.getChildAt(i));
        }

        return rowsList;
    }

    /**
     * Намира и връща бутоните на игровото поле от съответния ред
     * @param playgroundRow реда, от който ще се вземат бутоните
     * @return връща бутоните във вид на LinkedHashMap<GridButton, Integer> - второто е стойността на бутона
     */
    private LinkedHashMap<GridButton, Integer> findPlaygroundButtonsByRow(LinearLayout playgroundRow) {
        LinkedHashMap<GridButton, Integer> rowButtons = new LinkedHashMap<>();
        int cellsCount = playgroundRow.getChildCount();

        for(int i=0; i<cellsCount; i++) {
            Button btn = playgroundRow.getChildAt(i).findViewWithTag("button");
            rowButtons.put(GridButton.findGridButtonByButtonInside(btn), 0);
        }

        return rowButtons;
    }

    /**
     * Намира и връща броя на редовете от игровото поле
     * @param playground игровото поле
     * @return връща броя редове от игровото поле
     */
    private int gridRowsCount(LinearLayout playground) {
        return playground.getChildCount();
    }

    /**
     * Слага empty иконки на подадения ред
     * @param row реда, на който трябва да се сложат иконки
     */
    private void setEmptyIconOnRow(LinearLayout row) {
        for(int i=0; i<row.getChildCount(); i++) {
            Button btn = row.getChildAt(i).findViewWithTag("button");
            btn.setBackgroundResource(R.drawable.empty_cell);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == gridReadyButton) {
            checkAlmostPositions(rowsButtons.get(rowNow));
            checkCorrectPositions(rowsButtons.get(rowNow));

            if(checkWin(rowsButtons.get(rowNow), winCombination)) {
                return;
            }

            GridButton.removeOnClickListener(rowsButtons.get(rowNow));

            rowNow++;
            if(rowNow < rowsCount) {
                rowsButtons.add(findPlaygroundButtonsByRow((LinearLayout)playgroundRowsList.get(rowNow)));
                GridButton.setOnClickListener(this, rowsButtons.get(rowNow));
                setEmptyIconOnRow((LinearLayout)playgroundRowsList.get(rowNow));
            }
            return;
        }

        View par = (View)view.getParent();

        // ако е цъкнато на някой от бутоните в grid-а
        if (par.getTag().toString().equals("gridCell")) {
            colorPicker.show();
            lastClickedGridButton = (Button)view;
        }
    }

    /**
     * Проверява дали реда е пълен, ако е - показва ready бутона
     * @param rowButtons реда, който ще проверяваме
     */
    public void checkFullRow(HashMap<GridButton, Integer> rowButtons) {
        boolean isFull = true;
        for(int btnColor : rowButtons.values()) {
            if(btnColor == 0) {
                isFull = false;
                break;
            }
        }

        if(isFull) {
            gridReadyButton.setVisibility(View.VISIBLE);
        } else {
            gridReadyButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Отбелязва цветовете, които са на правилните си позиции в съответния ред
     * @param rowButtons реда, който трябва да се провери
     */
    private void checkCorrectPositions(HashMap<GridButton, Integer> rowButtons) {
        int i=0;
        for(GridButton gBtn : rowButtons.keySet()) {
            if(rowButtons.get(gBtn) == (int)winCombination.get(i)) {
                gBtn.showCorrectSymbol();
            }
            i++;
        }
    }

    /**
     * Отбелязва цветовете, които ги има в реда, но не са на правилните си позиции за съответния ред
     * @param rowButtons реда, който трябва да се провери
     */
    private void checkAlmostPositions(HashMap<GridButton, Integer> rowButtons) {
        for(GridButton gBtn : rowButtons.keySet()) {
            if(winCombination.contains(rowButtons.get(gBtn))) {
                gBtn.showAlmostSymbol();
            }
        }
    }

    /**
     * Проверява за победа и връща (true/false) за подадения ред
     * @param rowButtons реда, който ще се проверява
     * @param winCombination комбинацията, с която трябва да се сравни
     * @return връща TRUE ако реда съвпада с комбинацията и FALSE в противен случай
     */
    private boolean checkWin(HashMap<GridButton, Integer> rowButtons, ArrayList winCombination) {
        boolean win = true;
        int i=0; // брояч - кой елемент от комбинацията проверяваме

        for(int input : rowButtons.values()) {
            if(input != (int)winCombination.get(i)) {
                win = false;
                break;
            }
            i++;
        }

        gridReadyButton.setVisibility(View.INVISIBLE);

        if(win) {
            Log.d("Test", "YOU WON");
            Toast toast = Toast.makeText(getApplicationContext(), "You Won!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        return false;
    }

    /**
     * Генерира рандом печелившата комбинация
     * @param length броя на цифрите в комбинацията
     * @param max най-голямото число в комбинацията
     */
    private void generateWinCombination(int length, int max) {
        for(int i=0; i<length; i++) {
            while(true) {
                int randomNum = getRandomNumberInRange(1, max);
                // ако не се съдържа вече
                if (!winCombination.contains(randomNum)) {
                    winCombination.add(randomNum);
                    break;
                }
            }
        }
    }

    /**
     * Генерира рандом число в определен диапазон
     * @param min от
     * @param max до
     * @return връща рандом число
     */
    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public ArrayList<LinkedHashMap<GridButton, Integer>> getRowsButtons() {
        return rowsButtons;
    }

    public Button getLastClickedGridButton() {
        return lastClickedGridButton;
    }

    public int getRowNow() {
        return rowNow;
    }
}
