package si.fri.emp.vaje2.projektnaemp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListOfEventsActivity extends AppCompatActivity {
    ArrayList<String> eventList;
    ArrayAdapter<String> eventAdapter;
    ListView lvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);

        lvEvents = (ListView)findViewById(R.id.lvEvents);

        eventList = new ArrayList<String>();
        eventList.add("Event1");
        eventList.add("Event2");
        eventList.add("Event3");
        eventList.add("Event4");

        eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,eventList);

        lvEvents.setAdapter(eventAdapter);
    }
}
