package andromaly.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import andromaly.main.Business.Agent;
import andromaly.main.Business.FeatureManagerWrapper;
import andromaly.main.Presentation.EditPreferences;

public class MainActivity extends Activity {
	public static final int MENU_WRITE_PROFILE = Menu.FIRST + 2;
	public static final int MENU_PREFERENCES = Menu.FIRST + 3;

	private static Agent _agent = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//init the textBox and clear button
        Button clearTextButton = (Button) findViewById(R.id.clear_text_button);
        clearTextButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		EditText editText = (EditText) findViewById(R.id.edit_text);
        	    editText.setText("");
        	}
        });
        
//        _agent = new Agent(this.getApplicationContext() , this);
//        EditPreferences.setAgent(_agent);
		
        EditPreferences.setMainActivity(this);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_WRITE_PROFILE, 0, R.string.menu_write_profile);
		menu.add(0, MENU_PREFERENCES, 0, "Preferences");
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_WRITE_PROFILE:
			writeProfile();
			return true;
		case MENU_PREFERENCES:
			launchPreferencesMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void launchPreferencesMenu(){
		startActivity(new Intent(this, EditPreferences.class)); 
	}
	
	private void writeProfile() {
		_agent.backupProfiles();
	}
	
	public void stopAgent() {
		// if agent thread already active - stop it
		if (_agent != null) {
			System.out.println("STOPPING AGENT LOOP");
			_agent.stopAgentLoop();
			_agent = null;

		} else {
			System.out.println("CANNOT STOP AGENT. AGENT IS NOT RUNNING.");
		}
	}

	public void startAgent() {
		// start new agent thread
		if (_agent == null) {
			_agent = new Agent(this.getApplicationContext() , this);
			_agent.start(); // this is the agent's main loop
			System.out.println("STARTED NEW AGENT");
		} else {
			System.out.println("CANNOT START NEW AGENT. AGENT ALREADY RUNNING.");
		}
	}

	public void resetAgent() {
		// creating feature manager
		//TODO do we really need this?
		FeatureManagerWrapper.setContext(this.getApplicationContext());

		// stopping current agent if exists
		stopAgent();

		// start new agent thread
		startAgent();
	}
	
	public Agent getAgent(){
		return _agent;
	}
}
