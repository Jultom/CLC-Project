package ro.artsoftconsult.myapplication.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ro.artsoftconsult.myapplication.MainActivity;
import ro.artsoftconsult.myapplication.R;

/**
 * Created by hora on 18.07.2017.
 */

public class ListAdapter extends BaseAdapter {

    private final List<Product> productList;
    private final ShoppingList activity;
    private LayoutInflater inflater;

    public ListAdapter (List<Product>productList, ShoppingList fragment){
        this.productList= productList;
        this.activity= fragment;

        inflater= (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView==null){
            holder=new ViewHolder();
            convertView = inflater.inflate(R.layout.list,null);
            init(convertView,holder);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();

        }
        holder.name.setText(productList.get(position).getName());
        holder.price.setText(String.valueOf(productList.get(position).getPrice()));

        return convertView;
    }
    private void init(View convertView, ViewHolder holder)
    {
       holder.name=(TextView)convertView.findViewById(R.id.name_adaptor);
        holder.price=(TextView)convertView.findViewById(R.id.price_adaptor);
    }

    private class ViewHolder{
        public TextView name;
        public TextView price;
    }

}
