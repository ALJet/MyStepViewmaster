package indi.aljet.mystepview_master;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import indi.aljet.mystepview_master.fragment.DrawCanvasFragment;
import indi.aljet.mystepview_master.fragment.HorizontalStepViewFragment;
import indi.aljet.mystepview_master.fragment.VerticalStepViewFrowrdFragment;
import indi.aljet.mystepview_master.fragment.VerticalStepViewReverseFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VerticalStepViewReverseFragment mVerticalStepViewFragment =
                new VerticalStepViewReverseFragment();
        getFragmentManager().beginTransaction().replace
                (R.id.container,mVerticalStepViewFragment).
                commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        VerticalStepViewReverseFragment mVerticalStepViewFragment ;
        DrawCanvasFragment mDrawCanvasFragment;
        HorizontalStepViewFragment mHorizontalStepViewFragment;
        VerticalStepViewFrowrdFragment mVerticalStepViewReverseFragment;
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_horizontal_stepview:
                mHorizontalStepViewFragment = new HorizontalStepViewFragment();
                fragmentTransaction.replace(R.id.container, mHorizontalStepViewFragment).commit();
                break;

            case R.id.action_drawcanvas:
                mDrawCanvasFragment = new DrawCanvasFragment();
                fragmentTransaction.replace(R.id.container, mDrawCanvasFragment).commit();
                break;
            case R.id.action_vertical_reverse:
                mVerticalStepViewFragment = new VerticalStepViewReverseFragment();
                fragmentTransaction.replace(R.id.container, mVerticalStepViewFragment).commit();
                break;

            case R.id.action_vertical_forward:
                mVerticalStepViewReverseFragment = new VerticalStepViewFrowrdFragment();
                fragmentTransaction.replace(R.id.container, mVerticalStepViewReverseFragment).commit();
                break;

            case R.id.action_test_horizontal_stepview:
                startActivity(new Intent(this,TestHorizontalStepViewActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
