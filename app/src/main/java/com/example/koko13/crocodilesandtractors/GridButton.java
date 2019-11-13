package com.example.koko13.crocodilesandtractors;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;

public class GridButton {
    private Button button;
    private ImageView symbol;

    private GridButton(Button button, ImageView symbol) {
        this.button = button;
        this.symbol = symbol;
    }

    public Button getButton() {
        return button;
    }

    public void showCorrectSymbol() {
        symbol.setImageResource(R.drawable.cracodile);
        symbol.setVisibility(View.VISIBLE);
    }

    public void showAlmostSymbol() {
        symbol.setImageResource(R.drawable.tractor);
        symbol.setVisibility(View.VISIBLE);
    }

    public static GridButton findGridButtonByButtonInside(Button btn) {
        RelativeLayout par = (RelativeLayout)btn.getParent();
        ImageView correct = par.findViewWithTag("symbolImageView");
        return new GridButton(btn, correct);
    }

    public static void setOnClickListener(View.OnClickListener listener, HashMap<GridButton, Integer> buttons) {
        for(GridButton btn : buttons.keySet()) {
            btn.getButton().setOnClickListener(listener);
        }
    }

    public static void removeOnClickListener(HashMap<GridButton, Integer> buttons) {
        for(GridButton btn : buttons.keySet()) {
            btn.getButton().setOnClickListener(null);
        }
    }
}
