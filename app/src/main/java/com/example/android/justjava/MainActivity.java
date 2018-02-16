/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    // global variable that will hold the view for the
    // number of cups of coffee from activity_main.xml
    private TextView quantityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView created at the start of app so that it can be
        // used to help save state later in onSaveInstanceState and
        // onRestoreInstanceState methods
        quantityTextView = findViewById(R.id.quantity_text_view);

        // sets the background color to white
        ScrollView sV= findViewById(R.id.scroll_layout);
        sV.setBackgroundColor(Color.WHITE);

    }

    /**
     * this method plus the onRestoreInstanceState below keeps the
     * state of my variables in the case that the user changes
     * orientation
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("key_string", quantityTextView.getText().toString());
        outState.putInt("quantity_string", quantity);
        super.onSaveInstanceState(outState);
    }

    /**
     * used to save state for orientation change
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantityTextView.setText(savedInstanceState.getString("key_string"));
        quantity = savedInstanceState.getInt("quantity_string");

    }

    /**
     * This method is called when the order button is clicked.
     *
     */
    public void submitOrder(View view) {
        // Intent opens the gmail app for emailing the order
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"afchristian@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.just_java_order));
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * This method is called when the increment button is clicked.
     */
    public void increment(View view) {
        // only allow up to 100
        if (quantity < 10) {
            quantity++;
        }
        else {
            // Toast message: can't go above 100 cups of coffee!
            Toast.makeText(getApplicationContext(), getString(R.string.toast_100), Toast.LENGTH_LONG).show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the decrement button is clicked.
     * If/else used to make sure it doens't have negative cups of coffee
     */
    public void decrement(View view) {
        // this checks to make sure that decrementing the quantity variable
        // won't make it go negative (can't have negative cups of coffee)
        if (quantity > 1) {
            quantity--;
        }
        else {
            // Toast message: can't go below 1 cup of coffee!
            Toast.makeText(this, getString(R.string.toast_1), Toast.LENGTH_LONG).show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int quantityNumber) {
        quantityTextView.setText(String.format("%d", quantityNumber));
    }

    /**
     *
     * @return total price of order
     */
    private int calculatePrice() {
        // base price of a drink
        int pricePerDrink = 5;

        // add a dollar if adding whipped cream
        if (addedWhippedCream()) {
            pricePerDrink += 1;
        }

        // add a dollar if chocolate
        if (addedChocolate()) {
            pricePerDrink += 2;
        }

        return quantity * pricePerDrink;
    }

    /**
     *
     * @return true if whipped cream checkbox checked, else false
     */
    public boolean addedWhippedCream() {
        CheckBox whippedBox = findViewById(R.id.whipped_checkbox);
        return whippedBox.isChecked();
    }

    /**
     *
     * @return true if chocolate checkbox checked, else false
     */
    public boolean addedChocolate() {
        CheckBox chocolateBox = findViewById(R.id.chocolate_checkbox);
        return chocolateBox.isChecked();
    }

    public String customerName() {
        EditText cusName = findViewById(R.id.enter_name);
        return cusName.getText().toString();
    }

    /**
     *
     * @return string with summary of order
     */
    private String createOrderSummary() {
        return getString(R.string.customer_name, customerName()) +
                "\n" + getString(R.string.get_whipped) + " " + addedWhippedCream() +
                "\n" + getString(R.string.get_chocolate) + " " + addedChocolate() +
                "\n" + getString(R.string.total_quantity) + " " + quantity +
                "\n" + getString(R.string.total_price) + calculatePrice() +
                "\n" + getString(R.string.thank_you);
    }
}
