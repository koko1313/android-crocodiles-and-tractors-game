package com.example.koko13.crocodilesandtractors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton moreButton;
    private LinearLayout mainMenuFragment;
    private RelativeLayout background;
    private ImageButton startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = findViewById(R.id.background);
        background.setOnClickListener(this);

        moreButton = findViewById(R.id.moreButton);
        moreButton.setOnClickListener(this);

        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);

        mainMenuFragment = findViewById(R.id.mainMenuFragment);
    }

    @Override
    public void onBackPressed() {
        showExitConfirmDialog(this);
    }

    /**
     * Показва диалог за излизане от играта
     * @param activity Активити, върху което ще се покаже диалога
     */
    public static void showExitConfirmDialog(final Activity activity) {
        AlertDialog.Builder confirmExitBuilder = new AlertDialog.Builder(activity);
        confirmExitBuilder.setMessage("Изход от играта?")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Не", null);
        AlertDialog confirmExitDialog = confirmExitBuilder.create();
        confirmExitDialog.setTitle("Затваряне на приложението");
        confirmExitDialog.show();
    }

    /**
     * Показва/скрива менюто
     */
    private void toggleMenuVisibility() {
        if(mainMenuFragment.getVisibility() == View.INVISIBLE) {
            mainMenuFragment.setVisibility(View.VISIBLE);
        } else {
            mainMenuFragment.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.moreButton :
                toggleMenuVisibility();
                break;
            case R.id.background :
                mainMenuFragment.setVisibility(View.INVISIBLE);
                break;
            case R.id.startGameButton :
                Intent gameActivity = new Intent(this, GameActivity.class);
                startActivity(gameActivity);
                break;
        }
    }

}
