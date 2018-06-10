package com.appify.ecbt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<ProductViewModel> items;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Geenius store");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mRecyclerView = findViewById(R.id.rv_products);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ProductsAdapter(getApplicationContext(), getProducts());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checkout:
                checkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkout() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    public List<ProductViewModel> getProducts() {

        pDialog.setMessage("Cargando...");
        showDialog();

        items = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Product");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> products, ParseException e) {
                if (e == null) {
                    if (products.isEmpty()) {

                    } else {
                        for (int i = 0; i < products.size(); i++) {

                            String imgUrl = products.get(i).getParseFile("image").getUrl();
                            String name = products.get(i).getString("title");
                            String price = products.get(i).getString("price");

                            items.add(new ProductViewModel(imgUrl, name, price));
                        }
                        mAdapter.notifyDataSetChanged();
                        if (mRecyclerView.getVisibility() == View.GONE) {
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        hideDialog();
                    }
                } else {
                    Log.d("products", "Error: " + e.getMessage());
                }
            }
        });
        return items;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
