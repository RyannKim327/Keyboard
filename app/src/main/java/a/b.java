package a;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;
import mpop.revii.keyboard.R;

public class b extends PreferenceActivity {
	Preference setActivate, isUsed;
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setTheme(android.R.style.Theme_DeviceDefault_Light);
		setTitle("Settings: " + getTitle());
		getActionBar().setSubtitle("Developed by RyannKim327");
		addPreferencesFromResource(R.xml.preferences_main);
		setActivate = findPreference("setActivate");
		isUsed = findPreference("isUsed");
		
		setActivate.setOnPreferenceClickListener(new OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1) {
					// Activation of keyboard in IME Keyboard List
					Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					startActivity(intent);
					return false;
				}
			});
		isUsed.setOnPreferenceClickListener(new OnPreferenceClickListener(){
				@Override
				public boolean onPreferenceClick(Preference p1) {
					// Showing IME picker
					((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker();
					return false;
				}
			});
	}
}
