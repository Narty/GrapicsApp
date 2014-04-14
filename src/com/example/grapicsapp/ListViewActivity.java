package com.example.grapicsapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewActivity extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] modelNames = getResources().getStringArray(R.array.modelNames);
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.textView, modelNames));
		
		ListView listView = getListView();
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String modelName = ((TextView) view).getText().toString();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("modelName", modelName);
				startActivity(intent);
			}
		});
	}
}
