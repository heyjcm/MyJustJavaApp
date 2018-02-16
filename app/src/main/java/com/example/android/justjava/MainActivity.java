/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 * By: Christian Montecillo via Udacity Grow With Google Program:
 * https://www.udacity.com/grow-with-google
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

    // initialize the quantity of cups of coffee to
    // 1 because why would you order 0 cups of coffee?
    // 0 cups of coffee is lame
    int quantity = 1;

    // global variable that will hold the view for the
    // number of cups of coffee from activity_main.xml
    private TextView quantityTextView;
    CheckBox whippedBox;
    CheckBox chocolateBox;
    EditText cusName;

    // used in onSaveInstanceState and onRestoreInstanceState
    // so that there is no chance of typo when calling them
    // in their respective methods within onSaveInstanceState
    // and onRestoreInstanceState
    private final String quantityKey = "quantity_string_key";
    private final String quantityIntKey = "quantity_int_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TextView created at the start of app so that it can be
        // used to help save state later in onSaveInstanceState and
        // onRestoreInstanceState methods
        quantityTextView = findViewById(R.id.quantity_text_view);

        // initializing views in onCreate for efficiency and
        // lessening CPU cycles
        whippedBox = findViewById(R.id.whipped_checkbox);
        chocolateBox = findViewById(R.id.chocolate_checkbox);
        cusName = findViewById(R.id.enter_name);

        // sets the background color to white
        ScrollView sV = findViewById(R.id.scroll_layout);
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
        // saves the value of the quantity string with key key_string
        outState.putString(quantityKey, quantityTextView.getText().toString());
        outState.putInt(quantityIntKey, quantity);
        super.onSaveInstanceState(outState);
    }

    /**
     * used to save state for orientation change
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantityTextView.setText(savedInstanceState.getString(quantityKey));
        quantity = savedInstanceState.getInt(quantityIntKey);

    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Intent opens the gmail app for emailing the order
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"some_email@gmail.com"});
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
        } else {
            // Toast message: can't go above 100 cups of coffee!
            Toast.makeText(getApplicationContext(), getString(R.string.toast_100), Toast.LENGTH_LONG).show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method is called when the decrement button is clicked.
     * If/else used to make sure it doesn't have negative cups of coffee
     */
    public void decrement(View view) {
        // this checks to make sure that decrementing the quantity variable
        // won't make it go negative (can't have negative cups of coffee)
        if (quantity > 1) {
            quantity--;
        } else {
            // Toast message: can't go below 1 cup of coffee!
            Toast.makeText(this, getString(R.string.toast_1), Toast.LENGTH_LONG).show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int quantityNumber) {
        // show the quantity; have to use String.format
        // because quantityNumber is an int but needs to be
        // displayed as a String
        quantityTextView.setText(String.format("%d", quantityNumber));
    }

    /**
     * @return total price of order
     */
    private int calculatePrice() {
        // base price of a drink
        int pricePerDrink = 5;

        // add a dollar if whipped cream box ticked
        if (addedWhippedCream()) {
            pricePerDrink += 1;
        }

        // add two dollars if chocolate box ticked
        if (addedChocolate()) {
            pricePerDrink += 2;
        }

        // calculation for price to be paid
        return quantity * pricePerDrink;
    }

    /**
     * @return true if whipped cream checkbox checked, else false
     */
    public boolean addedWhippedCream() {
        // whippedBox initialized in onCreate method
        return whippedBox.isChecked();
    }

    /**
     * @return true if chocolate checkbox checked, else false
     */
    public boolean addedChocolate() {
        // chocolateBox initialized in onCreate method
        return chocolateBox.isChecked();
    }

    public String customerName() {
        // EditText cusName = findViewById(R.id.enter_name);
        return cusName.getText().toString();
    }

    /**
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
