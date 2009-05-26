package andromaly.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import andromaly.main.Business.Agent;

public class MainActivity extends Activity {
	public static final int RESET = Menu.FIRST;
	public static final int STOP = Menu.FIRST + 1;
	public static final int WRITE_PROFILE = Menu.FIRST + 2;

	static Agent agent = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// resetAgent();
		

        Button clearTextButton = (Button) findViewById(R.id.clear_text_button);

        clearTextButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) {
        		EditText editText = (EditText) findViewById(R.id.edit_text);
        	    editText.setText("");
        	}
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, RESET, 0, R.string.menu_reset);
		menu.add(0, STOP, 0, R.string.menu_stop);
		menu.add(0, WRITE_PROFILE, 0, R.string.menu_write_profile);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case RESET:
			resetAgent();
			return true;
		case STOP:
			stopAgent();
			return true;
		case WRITE_PROFILE:
			writeProfile();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void writeProfile() {
		agent.backupProfiles();
		//agent.exportProfiles();
	}
		
	private void stopAgent() {
		// if agent thread already active - stop it
		if (agent != null) {
			System.out.println("STOPPING AGENT LOOP");
			agent.stopAgentLoop();
			agent = null;

		} else {
			System.out.println("CANNOT STOP AGENT. AGENT IS NOT RUNNING.");
		}
	}

	private void startAgent() {
		// start new agent thread
		if (agent == null) {
			agent = new Agent(this.getApplicationContext() , this);
			agent.start(); // this is the agent's main loop
			System.out.println("STARTED NEW AGENT");
		} else {
			System.out.println("CANNOT START NEW AGENT. AGENT ALREADY RUNNING.");
		}
	}

	private void resetAgent() {
		// creating feature manager
		FeatureManagerWrapper.setContext(this.getApplicationContext());

		// stopping current agent if exists
		stopAgent();

		// start new agent thread
		startAgent();
	}
}
