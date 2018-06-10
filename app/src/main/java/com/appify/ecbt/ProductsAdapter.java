package com.appify.ecbt;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    public static final String CART = "cart";
    public static final String ITEMS = "items";

    private List<ProductViewModel> items;
    private Context context;

    public ProductsAdapter(Context context, List<ProductViewModel> productsList) {
        this.context = context;
        this.items = productsList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        ProductViewHolder vh = new ProductViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int position) {
        final ProductViewModel item = items.get(position);
        productViewHolder.tv_title.setText(item.getTitle());
        productViewHolder.tv_price.setText("$" + String.valueOf(item.getPrice()));

        Picasso.with(productViewHolder.iv_image.getContext())
                .load(item.getImgUrl())
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(productViewHolder.iv_image);

        productViewHolder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject itemObject = new JSONObject();
                try {
                    itemObject.put("title", item.getTitle());
                    itemObject.put("price", item.getPrice());
                    itemObject.put("imgUrl", item.getImgUrl());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SharedPreferences cart = context.getSharedPreferences(CART, 0);
                String items = cart.getString(ITEMS, "[]");

                try {
                    JSONArray itemsArray = new JSONArray(items);
                    itemsArray.put(itemObject);

                    SharedPreferences.Editor editor = cart.edit();
                    editor.putString(ITEMS, itemsArray.toString());
                    editor.apply();

                    Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        protected TextView tv_title;
        protected TextView tv_price;
        protected ImageView iv_image;
        protected Button btn_add_to_cart;

        public ProductViewHolder(View v) {
            super(v);
            tv_title = v.findViewById(R.id.tv_title);
            tv_price = v.findViewById(R.id.tv_price);
            iv_image = v.findViewById(R.id.iv_image);
            btn_add_to_cart = v.findViewById((R.id.btn_add_to_cart));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
