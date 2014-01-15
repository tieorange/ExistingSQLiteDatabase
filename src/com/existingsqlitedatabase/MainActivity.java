package com.existingsqlitedatabase;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ArrayList<Student> listStudents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void SaveEmployee(View v) {
		EditText txtName = (EditText) findViewById(R.id.txtName);
		EditText txtEmail = (EditText) findViewById(R.id.txtEmail);

		String name = txtName.getText().toString();
		String email = txtEmail.getText().toString();

		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		if (mDbHelper.SaveEmployee(name, email)) {
			Utility.ShowMessageBox(this, "Data saved.");
		} else {
			Utility.ShowMessageBox(this, "OOPS try again!");
		}
	}

	public void LoadEmployee(View v) {
		ListView listViewStudents = (ListView) findViewById(R.id.listViewStudents);
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();

		mDbHelper.EditStudent(new Student("Edited", "Esurname"), 1);
		Cursor testdata = mDbHelper.getTestData();

		String name = Utility.GetColumnValue(testdata, "name");
		String email = Utility.GetColumnValue(testdata, "surname");

		listStudents = mDbHelper.getAllStudents();
		ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(this,
				android.R.layout.simple_list_item_1, listStudents);
		listViewStudents.setAdapter(adapter);
		registerForContextMenu(listViewStudents);

		
		Utility.ShowMessageBox(this, "Name: " + name + " and Email: " + email);
		mDbHelper.close();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String header = listStudents.get(info.position).toString();
		menu.setHeaderTitle(header);
		if (v.getId() == R.id.listViewStudents) {
			menu.add(Menu.NONE, 1, 1, "Remove him from this list");
		}
	}

	private Adapter getListAdapter() {
		return new ArrayAdapter<Student>(this,
				android.R.layout.simple_list_item_1, listStudents);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		Student student = (Student) getListAdapter().getItem(info.position);
		
		TestAdapter mDbHelper = new TestAdapter(this);
		mDbHelper.createDatabase();
		mDbHelper.open();
		mDbHelper.RemoveStudentById(student.getId());
		mDbHelper.close();
		return true;
	}
}
