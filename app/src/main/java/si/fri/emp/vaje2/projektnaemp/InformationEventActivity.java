package si.fri.emp.vaje2.projektnaemp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InformationEventActivity extends AppCompatActivity {

    TextView tvEventName,tvDescription,tvPrice,tvTime,tvPlace,tvTags;
    ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_event);

        tvEventName = (TextView)findViewById(R.id.tvEventName);
        tvDescription = (TextView)findViewById(R.id.tvDescription);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvTime = (TextView)findViewById(R.id.tvTime);
        tvPlace = (TextView)findViewById(R.id.tvPlace);
        tvTags = (TextView)findViewById(R.id.tvTags);

        ivPicture =  findViewById(R.id.ivPicture);


    }
}
