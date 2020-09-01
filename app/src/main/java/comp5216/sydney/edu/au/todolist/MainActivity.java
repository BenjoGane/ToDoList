package comp5216.sydney.edu.au.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText addItemText;
    private List<String> items;
    private ArrayAdapter<String> itemsAdapter;

    public final int EDIT_ITEM_REQUEST_CODE = 647;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        addItemText = (EditText) findViewById(R.id.addItemText);

        items = new ArrayList<>();
        items.add("item one");

        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);

        listView.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void addItemOnClick(View view) {
        String toAddString = addItemText.getText().toString();
        if (toAddString.length() > 0) {
            itemsAdapter.add(toAddString);
            addItemText.setText("");
        }
    }

    private void setupListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String updateItem = items.get(position);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);

                intent.putExtra("item", updateItem);
                intent.putExtra("position", position);

                startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                itemsAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                items.remove(position);
                                itemsAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Do nothing
                            }
                        });

                builder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            String editedItem = data.getStringExtra("item");
            int position = data.getIntExtra("position", -1);
            items.set(position, editedItem);
            Toast.makeText(this, "updated: " + editedItem, Toast.LENGTH_SHORT).show();
            itemsAdapter.notifyDataSetChanged();
        }
    }
}