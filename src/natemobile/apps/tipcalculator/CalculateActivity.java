package natemobile.apps.tipcalculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Calculate Activity
 * 
 * Tip Calculator App main activity
 * @author nkemavaha
 *
 */
public class CalculateActivity extends ActionBarActivity {

	/** Initial % value */
	float INIT_PERCENT_VALUE = 15.00f;
	
	///////////////////////////
	/// UI elements
	///////////////////////////
	
	/** Seek bar for tip percentage */
	SeekBar percentSeekBar;
	
	/** Seek bar for how many people we split the bill. */
	SeekBar peopleSeekBar;
	
	/** Textview for showing percent value user select */
	TextView tvPercentValueShow;
	
	/** Textview for showing # of people we are going to split cost with*/
	TextView tvSplitValueShow;
	
	/** Input from user for amount of $$ going to calculate tip for*/
	EditText etAmountValueText;
	
	/** Textview for showing final result */
	TextView tvResult;
	
	//////////////////////////
	/// Data fields
	/////////////////////////
	
	/** Percentage value (i.e. from 0 to 100) */
	int percentValue;
	
	/** # of people we are going to split (i.e. from 1 to infinity) */
	int splitValue;
	
	/** Price/Cost/Amount*/
	float amountValue;
	
	/** Tip amount value */
	float resultTipValue;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate);
		
		// Setup UI elements reference
		percentSeekBar = (SeekBar) findViewById( R.id.sbarPercent );
		peopleSeekBar = (SeekBar) findViewById( R.id.sbSplitPeople );
		
		tvPercentValueShow = (TextView) findViewById( R.id.tvPercentValue );
		tvSplitValueShow = (TextView) findViewById( R.id.tvSplitPeopleValue );
		
		etAmountValueText = (EditText) findViewById( R.id.eTextAmount );
		
		tvResult = (TextView) findViewById( R.id.tvResult );
		
		percentValue = 0;
		splitValue = 1;
		amountValue = 0;
		resultTipValue = 0;
		
		setupUIListeners();
		
	}
	
	/**
	 * Setup UI elements listeners
	 */
	public void setupUIListeners() {
		setupEditTextListeners();
		setupSeekBarsListeners();
	}
	
	/**
	 * Setup EditText Listeners 
	 */
	private void setupEditTextListeners() {
		// This is for input edit-text field
		if ( etAmountValueText != null ) {
			etAmountValueText.addTextChangedListener( new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					
					String num = s.toString();
					// Checking in case if user delete all value in the Editable field.
					if ( num != null && num.isEmpty() == false) {
						amountValue = Float.parseFloat( s.toString() );
					} else {
						amountValue = 0;
					}

					updateActualAndDisplayedResult();
				}
			});
		}	
	}
	
	/** Setup SeekBars Listeners */
	private void setupSeekBarsListeners() {

		// This is for percentage seek bar
		if ( percentSeekBar != null ) {
			// Initialize starting value
			tvPercentValueShow.setText( "15%" );
			percentSeekBar.setProgress( (int) INIT_PERCENT_VALUE );
			percentValue = percentSeekBar.getProgress();
			tvPercentValueShow.setText( percentValue + "%");
			updateActualAndDisplayedResult();
			
			// Setup listener
			percentSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					if ( tvPercentValueShow != null ) {
						tvPercentValueShow.setText( progress +"%" );
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					percentValue = seekBar.getProgress();
					tvPercentValueShow.setText( percentValue +"%" );
					
					updateActualAndDisplayedResult();
				}

			});	
		}
		
		
		// This is for percentage seek bar
		if ( peopleSeekBar != null ) {
			tvSplitValueShow.setText( "1 person" );
			
			peopleSeekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					if ( tvSplitValueShow != null ) {
						tvSplitValueShow.setText( getStringPersonUnitBasedOnNumber( progress ) );
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {	
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					updateSplitValue();
					tvSplitValueShow.setText( getStringPersonUnitBasedOnNumber( splitValue ) );
					
					updateActualAndDisplayedResult();
					
				}

			});	
		}	
	}
	
	/**
	 * Helper function to recalculate and update actual and displayed value for tip amount.
	 */
	private void updateActualAndDisplayedResult() {
		resultTipValue = calculateTip( amountValue, percentValue, splitValue);
		tvResult.setText( "Tip: $" + String.format("%.2f", resultTipValue) + " per person." );	
	}
	
	/**
	 * Helper function to determine a proper unit(s) based on the given progress/unit
	 * @param progress
	 * @return String containing number and its proper noun unit.
	 */
	private String getStringPersonUnitBasedOnNumber( int progress ) {
		String msg = "1 person";
		if ( progress > 1) {
			msg = progress + " people";
		}
		
		return msg;
	}
	
	/**
	 * Helper function to help getting latest value of # of people will be splited tip
	 */
	private void updateSplitValue() {
		splitValue = peopleSeekBar.getProgress();	
		if ( splitValue < 1 ) {
			splitValue = 1;
		}
	}
	
	////////////////////////////
	/// Core functionalities
	///////////////////////////
	
	/**
	 * Calculate a tip amount per person based on the given amount and percent
	 * @param amount		Amount
	 * @param percent		Percent from 0 to 100;
	 * @param totalPeople	Total number of people to split
	 * @return Tip amount per person
	 */
	private float calculateTip( float amount, int percent, int totalPeople ) {
		float result = (float) ((amount * (percent / 100.0 )) / totalPeople);
		return result;
	}

	//////////////////////////
	/// Override methods
	//////////////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
