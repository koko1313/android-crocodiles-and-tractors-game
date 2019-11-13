package com.example.koko13.crocodilesandtractors;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenuFragment extends Fragment implements View.OnClickListener {
    private Button startGameButton;
    private Button gameRulesButton;
    private Button aboutButton;
    private Button exitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        startGameButton = view.findViewById(R.id.startGameButton);
        gameRulesButton = view.findViewById(R.id.gameRulesButton);
        aboutButton = view.findViewById(R.id.aboutButton);
        exitButton = view.findViewById(R.id.exitButton);

        startGameButton.setOnClickListener(this);
        gameRulesButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startGameButton :
                openActivity(GameActivity.class);
                break;
            case R.id.gameRulesButton :
                openActivity(GameRulesActivity.class);
                break;
            case R.id.aboutButton :
                openActivity(AboutActivity.class);
                break;
            case R.id.exitButton :
                MainActivity.showExitConfirmDialog(getActivity());
                break;
        }
    }

    /**
     * Стартита Activity по подаден клас
     * @param activityToOpen Класа на актишитито, което трябва да бъде стартирано. Пример: MainActivity.Class
     */
    private void openActivity(Class activityToOpen) {
        Intent activity = new Intent(getActivity(), activityToOpen);
        startActivity(activity);
    }

}
