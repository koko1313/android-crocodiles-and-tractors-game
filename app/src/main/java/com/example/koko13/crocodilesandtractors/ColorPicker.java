package com.example.koko13.crocodilesandtractors;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ColorPicker implements View.OnClickListener {
    private View colorPicker;
    private HashMap<Button, Integer> colorPickerButtons; // бутоните от ColorPicker-а
    private GameActivity gameActivity;

    /**
     * Конструктор
     * @param activity активитито, което съдържа Color Picker-а
     * @param colorPickerId id-то на Color Picker-а
     * @param gameActivity Активитито с игровото поле, за да използваме публичните му методи
     */
    public ColorPicker(Activity activity, int colorPickerId, GameActivity gameActivity) {
        this.colorPicker = activity.findViewById(colorPickerId);

        this.colorPickerButtons = findColorPickerButtons((LinearLayout)colorPicker);
        setColorPickerOnClickListener(colorPickerButtons);

        this.gameActivity = gameActivity;
    }

    /**
     * Намира и връща списък с бутоните от Color Picker-а
     * @param colorPicker Color Picker-а
     * @return връща HashMap<Button, Integer> с бутоните от Color Picker-а. Button - бутона, Integer - означението му
     */
    private HashMap<Button, Integer> findColorPickerButtons(LinearLayout colorPicker) {
        HashMap<Button, Integer> colorPickerButtons = new HashMap<>();

        int colorPickerRowsCount = colorPicker.getChildCount();

        for(int i=0; i<colorPickerRowsCount; i++) { // за всеки ред
            LinearLayout colorPickerRow = (LinearLayout)colorPicker.getChildAt(i); // взимаме ред
            int columnCount = colorPickerRow.getChildCount(); // взимаме броя на клетките в реда

            for(int j=0; j<columnCount; j++) { // за всяка клетка
                Button btn = (Button)colorPickerRow.getChildAt(j); // взимаме бутон
                int btnSignature = Integer.parseInt((String)btn.getTag()); // взимаме означението на бутона
                colorPickerButtons.put(btn, btnSignature);
            }
        }

        return colorPickerButtons;
    }

    /**
     * Сетва onClickListener на бутоните от color picker-а
     * @param buttons Листа с бутоните, на които трябва да се сетне listener
     */
    private void setColorPickerOnClickListener(HashMap<Button, Integer> buttons) {
        for(Button btn : buttons.keySet()) {
            btn.setOnClickListener(this);
        }
    }

    public void show() {
        colorPicker.setVisibility(View.VISIBLE);
    }

    public void hide() {
        colorPicker.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        ArrayList<LinkedHashMap<GridButton, Integer>> rowsButtons = gameActivity.getRowsButtons();
        Button lastClickedGridButton = gameActivity.getLastClickedGridButton();
        int rowNow = gameActivity.getRowNow();

        this.hide();
        Drawable choosenColor = view.getBackground();
        int buttonSignature = colorPickerButtons.get(view);
        lastClickedGridButton.setBackground(choosenColor);

        // търси последно натиснатия бутон в списъка с всички бутони за текущия ред и заменя стойността му.
        for(GridButton btn : rowsButtons.get(rowNow).keySet()) {
            if(btn.getButton() == lastClickedGridButton) {
                rowsButtons.get(rowNow).put(btn, buttonSignature);
            }
        }

        gameActivity.checkFullRow(rowsButtons.get(rowNow));
    }
}
