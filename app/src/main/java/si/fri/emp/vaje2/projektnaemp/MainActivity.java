package si.fri.emp.vaje2.projektnaemp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  {

    Button btn_Matic,btn_SignIn, btn_Info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Matic = (Button)findViewById(R.id.btnMatic);
        btn_SignIn = (Button)findViewById(R.id.btnSignIn);
        btn_Info = (Button)findViewById(R.id.btnInfo);

        btn_Matic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ListOfEventsActivity.class);
                startActivity(intent);
            }
        });

        btn_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,InformationEventActivity.class);
                startActivity(intent);
            }
        });

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
