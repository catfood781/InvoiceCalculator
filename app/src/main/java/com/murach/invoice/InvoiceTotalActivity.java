package com.murach.invoice;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;	//importing the classes for the widgets
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.text.NumberFormat;

public class InvoiceTotalActivity extends Activity
implements OnEditorActionListener
	{
	private EditText subtotalEditText;
	private TextView discountPercentTextView;
	private TextView discountAmountTextView;
	private TextView totalTextView;

	private SharedPreferences savedValues;

	private String billAmountString;
	private float tipPercent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice_total);

		//Getting reference to the widgets
		subtotalEditText = (EditText) findViewById(R.id.subtotalEditText);
		discountPercentTextView = (TextView) findViewById(R.id.discountPercentTextView);
		discountAmountTextView = (TextView) findViewById(R.id.discountAmountTextView);
		totalTextView = (TextView) findViewById(R.id.totalTextView);

		subtotalEditText.setOnEditorActionListener(this);
		savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		//billAmountString = savedValues.getString("subtotalText", "");

		subtotalEditText.setText(savedValues.getString("billAmountString", ""));

		CalculateAndDisplay();

	}

	@Override
	public void onPause()
	{
		Editor editor = savedValues.edit();
		editor.putString("billAmountString", billAmountString);
		editor.putFloat("tipPercent", tipPercent);
		editor.commit();

		super.onPause();
	}

	public void CalculateAndDisplay() {

		//Getting the bill amount
		billAmountString = subtotalEditText.getText().toString();
		float subTotal;
		if(billAmountString.equals(""))
		{
			subTotal = 0;
		}
		else
		{
			subTotal = Float.parseFloat(billAmountString);
		}
		//Calculating tip and total
		float tipPercent;

		if (subTotal < 100)
		{
			tipPercent = 0;
		}
		else if (subTotal < 200)
		{
			tipPercent = .10f;
		}
		else
		{
			tipPercent = .20f;
		}

		float tipAmount = subTotal * tipPercent;
		float totalAmount = subTotal - tipAmount;

		//Displaying the results with formatting
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		discountAmountTextView.setText(currency.format(tipAmount));
		totalTextView.setText(currency.format(totalAmount));

		NumberFormat percent = NumberFormat.getPercentInstance();
		discountPercentTextView.setText(percent.format(tipPercent));
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
	{
		if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
		{
			CalculateAndDisplay();
		}
		return false;
	}
}