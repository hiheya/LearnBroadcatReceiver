package work.icu007.learnbroadcatreceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import work.icu007.learnbroadcatreceiver.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    TextView textView;

    private MyReceiver myReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        textView = findViewById(R.id.tvShowMsg);
        findViewById(R.id.btnSendMsg).setOnClickListener(this);
        findViewById(R.id.btnReg).setOnClickListener(this);
        findViewById(R.id.btnUnreg).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendMsg:
//                Intent intent = new Intent(this,MyReceiver.class);
                Intent intent = new Intent(MyReceiver.ACTION);
                intent.putExtra("data","this is Charlie_Liao's LearnBroadcastReceiver Demo");
                sendBroadcast(intent);
                break;
            case R.id.btnReg:
                if (myReceiver == null){
                    myReceiver = new MyReceiver();
                    registerReceiver(myReceiver,new IntentFilter(MyReceiver.ACTION));
                }
                break;
            case R.id.btnUnreg:
                if (myReceiver != null){
                    unregisterReceiver(myReceiver);
                    myReceiver = null;
                }
                break;
        }
    }
}