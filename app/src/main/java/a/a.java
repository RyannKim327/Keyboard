package a;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.media.AudioManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import mpop.revii.keyboard.R;


public class a extends InputMethodService implements OnKeyboardActionListener {
	KeyboardView view;
	Keyboard alpha, symbol;
	SharedPreferences preferences;
	boolean caps = false, lock = false;
	AudioManager beep;
	Vibrator lindol;
	@Override
	public View onCreateInputView(){
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		beep = (AudioManager)getSystemService(AUDIO_SERVICE);
		lindol = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		view = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
		alpha = new Keyboard(this, R.xml.alpha);
		symbol = new Keyboard(this, R.xml.symbol);
		
		view.setOnKeyboardActionListener(this);
		view.setKeyboard(alpha);
		view.setPreviewEnabled(false);
		
		return view;
	}
	@Override
	public void onKey(int p1, int[] p2) {
		InputConnection conn = getCurrentInputConnection();
		switch(p1){
			case Keyboard.KEYCODE_DELETE: // Backspace
				conn.deleteSurroundingText(1, 0);
				conn.commitText("", 1);
				if(preferences.getBoolean("isVibrate", true) && lindol.hasVibrator()){
					lindol.vibrate(50);
				}
			break;
			case Keyboard.KEYCODE_DONE: // Enter
				switch(getCurrentInputEditorInfo().imeOptions & EditorInfo.IME_MASK_ACTION){
					case EditorInfo.IME_ACTION_SEARCH: // Search lik in search engines and google apps
						case EditorInfo.IME_ACTION_GO: // URL link
							sendDefaultEditorAction(true);
					break;
					default:
						//conn.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER|(KeyEvent.KEYCODE_SHIFT_LEFT & KeyEvent.KEYCODE_ENTER)));
						conn.commitText(String.valueOf((char)10), 1);
				}
			break;
			case Keyboard.KEYCODE_SHIFT: // Shift
				if(caps){
					lock = true;
					caps = false;
					alpha.setShifted(lock);
					view.invalidateAllKeys();
				}else if(lock){
					lock = false;
					caps = false;
					alpha.setShifted(caps);
					view.invalidateAllKeys();
				}else{
					caps = true;
					alpha.setShifted(caps);
					view.invalidateAllKeys();
				}
			break;
			case -1903: // Change Keys from alphabet ro symbols
				if(view.getKeyboard() == alpha){
					view.setKeyboard(symbol);
					view.invalidateAllKeys();
				}else{
					view.setKeyboard(alpha);
					view.invalidateAllKeys();
				}
			break;
			default:
				char text = (char)p1;
				String str = String.valueOf(text);
				conn.commitText((caps || lock) ? str.toUpperCase() : str.toLowerCase(), 1);
				caps = false;
				alpha.setShifted(caps || lock);
				if(p1 == 32 || p1 == Keyboard.KEYCODE_DONE){
					view.setKeyboard(alpha);
				}
				view.invalidateAllKeys();
				if(preferences.getBoolean("isBeep", true)){
					beep.playSoundEffect(VibrationEffect.EFFECT_HEAVY_CLICK, -1f);
				}
				if(preferences.getBoolean("isVibrate", true) && lindol.hasVibrator()){
					lindol.vibrate(50);
    			}
		}
	}
	@Override
	public void onPress(int p1) {}
	@Override
	public void onRelease(int p1) {}
	@Override
	public void onText(CharSequence p1) {}
	@Override
	public void swipeLeft() {}
	@Override
	public void swipeRight() {}
	@Override
	public void swipeDown() {}
	@Override
	public void swipeUp() {}
}
