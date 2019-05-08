package hcmus.tuphi.mychecklist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText et;
    ListView lv;
    Button btn;
    Button btn2;
    Button btn3;
    ArrayList<String> arrayList;
    ArrayList<String> selectedItems;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et= (EditText) findViewById(R.id.edit_list);
        lv= (ListView)findViewById(R.id.list_check);
        btn= (Button)findViewById(R.id.btn_add);
        btn2=(Button)findViewById(R.id.btn_delete);
        btn3=(Button)findViewById(R.id.btn_edit);

        selectedItems=new ArrayList<String>();
        arrayList= new ArrayList<String>();
        adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_checked,
                arrayList);
        lv.setAdapter(adapter);
//        gọi button add, delele
        onBtnClick();
        onBtnDelClick();
        onBtnEditClick();
    }


    public void onStart(){
        super.onStart();

        //create an instance of ListView
        ListView chl=(ListView) findViewById(R.id.list_check);
        //set multiple selection mode
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selectedItem = ((TextView) view).getText().toString();
                if(selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    selectedItems.add(selectedItem); //add selected item to the list of selected items

            }

        });
    }
//    xử lý button Add
    public void onBtnClick(){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String result=et.getText().toString();
                arrayList.add(result);
                adapter.notifyDataSetChanged();
            }}
            );
    }
//    Xử lý button delete

    public void onBtnDelClick(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
               adapter.notifyDataSetChanged();
            }
        });
    }
    public void onBtnEditClick(){
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView item_each=(ListView) findViewById(R.id.list_check);
                item_each.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // selected item
                        String selectedItem = ((TextView) view).getText().toString();
                        String result=et.getText().toString();
                        selectedItem=result;
                        adapter.notifyDataSetChanged();

                    }

                });
            }
        });
    }




}
