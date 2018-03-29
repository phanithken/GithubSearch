package app.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.activity.ResultDetail;
import app.model.Github;
import com.example.githubdemo.app.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    List<JsonElement> mItems;

    public CardAdapter() {
        super();
        mItems = new ArrayList<JsonElement>();
    }

    public void addData(JsonElement jsonItem) {
        mItems.add(jsonItem);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        JsonObject jsonObject = mItems.get(i).getAsJsonObject();
        viewHolder.searchItem.setText(jsonObject.get("full_name").toString().replace("\"",""));
        System.out.println("In adapter " + jsonObject.get("full_name").toString());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView searchItem;

        public ViewHolder(View itemView) {
            super(itemView);
            searchItem = (TextView) itemView.findViewById(R.id.search_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JsonObject jsonObject = mItems.get(getAdapterPosition()).getAsJsonObject();
                    Intent intent = new Intent(view.getContext(), ResultDetail.class);
                    intent.putExtra("repo", jsonObject.get("full_name").toString().replace("\"",""));
                    view.getContext().startActivity(intent);
                }
            });
        }

    }
}