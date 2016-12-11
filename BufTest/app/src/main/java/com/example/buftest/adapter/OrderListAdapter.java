package com.example.buftest.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.buftest.MainActivity;
import com.example.buftest.OrderActivity;
import com.example.buftest.R;
import com.example.buftest.model.Menu;
import com.example.buftest.model.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Mark on 12/11/2016.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    ArrayList<Order> orders;
    Context context;

    public OrderListAdapter(ArrayList<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sendTime;
        TextView tableName;
        TextView orderAmount;
        CardView orderCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            //// TODO: 12/11/2016
            sendTime = (TextView) itemView.findViewById(R.id.send_time);
            tableName = (TextView) itemView.findViewById(R.id.table_name);
            orderAmount = (TextView) itemView.findViewById(R.id.order_amount);
            orderCardView = (CardView) itemView.findViewById(R.id.order_card_view);
        }
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String tableName = orders.get(position).getCode().getTableNo();
        holder.tableName.setText(tableName);
        final Map<String, Menu> menus = orders.get(position).getMenus();
        holder.orderAmount.setText(menus.size() + " orders");

        Date sendTime = orders.get(position).getTimestamp();
        Date current = new Date();
        long diff = current.getTime() - sendTime.getTime();
        long diffMinutes = (diff / (60 * 60 * 1000)) + 1;
        if (diffMinutes > 60) {
            holder.sendTime.setText((diffMinutes / 60) + " hour ago");
        } else {
            holder.sendTime.setText(diffMinutes + " minutes ago");
        }

        holder.orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Orders");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(40, 0, 40, 0);

                //[start-loop]
                for (Map.Entry<String, Menu> entry : menus.entrySet()) {
                    TextView listItem = new TextView(context);
                    listItem.setText("- " + entry.getValue().getMenuName() + " x " + entry.getValue().getAmount());
                    layout.addView(listItem);
                }
                //[end-loop]

                alertDialog.setView(layout);
                alertDialog.setNegativeButton("Cancel", null);
                alertDialog.setPositiveButton("Done", null);
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
