package ro.artsoftconsult.myapplication.shoppinglist;

/**
 * Created by hora on 10.08.2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import ro.artsoftconsult.myapplication.MainActivity;
import ro.artsoftconsult.myapplication.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ShoppingList extends Fragment {
    double total = 0;
    private List<Product> productList = new ArrayList<>();
    private ListAdapter mAdapter;
    private EditText textNou;
    private EditText numarNou;
    private ListView listView;
    private Button btAdd;
    private EditText productName;
    private EditText productPrice;
    private TextView priceTextView;

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View converView = inflater.inflate(R.layout.shoplist_main,container,false);
        prepareProduse(converView);

        listView = (ListView) converView.findViewById(R.id.listview_product_shoplist_main);
        mAdapter= new ListAdapter(productList, ShoppingList.this);
        listView.setAdapter(mAdapter);
        setHasOptionsMenu(false);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                displayAlertDialogModify(position);
                return false;
            }
        });

        return converView;
    }


    private void prepareProduse(View convertView) {
        textNou = (EditText) convertView.findViewById(R.id.add_product_shoplist_main);
        numarNou = (EditText) convertView.findViewById(R.id.add_price_shoplist_main);
        btAdd = (Button) convertView.findViewById(R.id.buton_add_product_shoplist_main);
         priceTextView = (TextView) convertView.findViewById(
                R.id.total_money_shoplist_main);
        numarNou.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addLineToListView();
                }
                return false;
            }
        });


        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addLineToListView();
            }
        });

    }

    private void addLineToListView() {

        double numar;

        if (textNou.getText().toString() != null && !"".equals(textNou.getText().toString())) {
            {
                String newItems = textNou.getText().toString();
                if (numarNou.getText().toString() != null && !"".equals(numarNou.getText().toString()))
                    numar = Double.parseDouble(numarNou.getText().toString());
                else {
                    numar = 0;
                    Toast.makeText(getActivity(), "You haven't entered any price ", Toast.LENGTH_SHORT).show();
                }

                Product product = new Product(newItems, numar);
                productList.add(product);

                textNou.setText("");
                numarNou.setText("");
                total = total + numar;
                priceTextView.setText(String.format("%.2f",total));
                hideSoftKeyboard(getActivity());
                scrollMyListViewToBottom();

            }

        } else {
            Toast.makeText(getActivity(), "You haven't entered any product ", Toast.LENGTH_SHORT).show();

        }

    }


    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }


    public void displayAlertDialogModify(final int position) {
        final double number;
        String name;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertDialogView = inflater.inflate(R.layout.edit_product, null);

//2nd Alert Dialog
        AlertDialog.Builder alertDialogBuilderSuccess = new AlertDialog.Builder(
               getActivity());
        alertDialogBuilderSuccess.setView(alertDialogView);
        alertDialogBuilderSuccess.setTitle("Edit product");

        productName = (EditText) alertDialogView.findViewById(R.id.set_product_name);
        productPrice = (EditText) alertDialogView.findViewById(R.id.set_product_price);

        number = productList.get(position).getPrice();
        name = productList.get(position).getName();

        productName.setText(name);
        productPrice.setText(String.valueOf(number));
        // set dialog message
        alertDialogBuilderSuccess
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {


                                double newNumber;
                                if (productPrice.getText().toString() != null && !"".equals(productPrice.getText().toString())) {
                                    newNumber = Double.parseDouble(productPrice.getText().toString());
                                } else
                                    newNumber = 0;
                                String newName = productName.getText().toString();


                                total = total - number;
                                total = total + newNumber;
                                Product product = new Product(newName, newNumber);
                                productList.set(position, product);

                                priceTextView.setText(String.format("%.2f",total));
                             mAdapter.notifyDataSetChanged();

                            }

                        });


        // create alert dialog
        final AlertDialog alertDialogSuccess = alertDialogBuilderSuccess.create();


        //////////////////////////////////
        //1st Alert
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
               getActivity());
        alertDialogBuilder.setTitle("Modify product");
        // set dialog message
        alertDialogBuilder
                .setMessage("What do you want to do with this product ?")
                .setCancelable(false)
                .setNeutralButton("back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                            }
                        })
                .setPositiveButton("Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {

                                //calling the second alert when it user press the confirm button
                                alertDialogSuccess.show();
                            }
                        })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {


                                total = total - number;
                                productList.remove(position);
                                priceTextView.setText(String.format("%.2f",total));

                                mAdapter.notifyDataSetChanged();
                            }
                        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}


