package com.kc.neteasy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.net.Network;
import android.os.Bundle;
import android.view.View;

import com.kc.neteasy.ICallBack.IResult;
import com.kc.neteasy.http.HttpUtils;

public class MainActivity extends AppCompatActivity {

    private  NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;


        navController = Navigation.findNavController(findViewById(R.id.fragment));
        NavigationUI.setupActionBarWithNavController(this,navController);

    }

    private static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }

    public NavController getNavController(){
        return navController;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}
