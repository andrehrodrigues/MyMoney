package br.com.saturno.android.mymoney.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import br.com.saturno.android.database.datasources.DataSource;
import br.com.saturno.android.database.entities.Category;
import br.com.saturno.android.database.datasources.CategoryDataSource;
import br.com.saturno.android.mymoney.R;

public class CategoryActivity extends Activity {

	EditText catName;
	ListView catLV;
	ArrayAdapter<String> categoryArrayAdapter;
	Category currentItem;
	DataSource ds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		populateListView();

		catLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			// http://stackoverflow.com/questions/2468100/android-listview-click-howto
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//currentItem = getCurrentItem(catLV.getItemAtPosition(position).toString());
				catName = (EditText) findViewById(R.id.inputCatName);
				catName.setText(currentItem.getName());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.category, menu);
		return true;
	}

	public void populateListView() {
		catLV = (ListView) findViewById(R.id.listvCategories);
		List<String> cats = convertCategoryList(getAllCategories());
		categoryArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, cats);
		catLV.setAdapter(categoryArrayAdapter);
	}

	public void insertCategory(View view) {
		catName = (EditText) findViewById(R.id.inputCatName);

		if (categoryNameValidator()) {
			ds = new CategoryDataSource(this);
			ds.add(new Category(catName.getText().toString()));
			categoryArrayAdapter.add(catName.getText().toString());
			categoryArrayAdapter.notifyDataSetChanged();
			catName.setText("");
			Toast.makeText(getApplicationContext(), "Insert successful =)",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void editCategory(View view) {
		catName = (EditText) findViewById(R.id.inputCatName);

		if (categoryNameValidator()) {
			ds = new CategoryDataSource(this);
			ds.update(new Category(currentItem.getId(), catName
					.getText().toString()));
			int itemPosition = categoryArrayAdapter.getPosition(currentItem
					.getName());
			categoryArrayAdapter.remove(currentItem.getName());
			categoryArrayAdapter.insert(catName.getText().toString(),
					itemPosition);
			categoryArrayAdapter.notifyDataSetChanged();
			catName.setText("");
			Toast.makeText(getApplicationContext(), "Edit successful /o/",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void removeCategory(View view) {
		catName = (EditText) findViewById(R.id.inputCatName);
		if (categoryNameValidator()) {
			ds = new CategoryDataSource(this);
			//ds.delete(ds.get(catName.getText().toString()));
			categoryArrayAdapter.remove(catName.getText().toString());
			categoryArrayAdapter.notifyDataSetChanged();
			catName.setText("");
			Toast.makeText(getApplicationContext(), "Remove successful =P",
					Toast.LENGTH_SHORT).show();
		}
	}

	public List<Category> getAllCategories() {
		ds = new CategoryDataSource(this);
		List<Category> catList = ds.getAll();
		return catList;
	}

	/*public int getItemId() {
		catName = (EditText) findViewById(R.id.inputCatName);
		ds = new CategoryDataSource(this);
		Category category = ds.get(catName.getText().toString());
		return category.getId();
	}*/

	/*public Category getCurrentItem(String catName) {
		CategoryDataSource cat = new CategoryDataSource(this);
		Category category = cat.getCategory(catName);
		return category;
	}*/

	public String[] listToArray(List<Category> catList) {
		int aux = 0;
		String[] array = new String[catList.size()];
		for (Category cn : catList) {
			array[aux] = cn.getName();
			aux++;
		}
		return array;
	}

	public List<String> convertCategoryList(List<Category> catList) {
		List<String> list = new ArrayList<String>();
		for (Category cn : catList) {
			list.add(cn.getName());
		}
		return list;
	}

	public boolean categoryNameValidator() {
		catName = (EditText) findViewById(R.id.inputCatName);
		if (catName.getText().length() == 0) {
			catName.setError("Please insert a name.");
			return false;
		}
		return true;
	}
}
