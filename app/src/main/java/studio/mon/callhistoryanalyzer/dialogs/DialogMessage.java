package studio.mon.callhistoryanalyzer.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import studio.mon.callhistoryanalyzer.R;

public class DialogMessage extends Dialog implements View.OnClickListener {

	public Activity c;
	public Dialog d;
	public TextView tvVN, tvEng;
	public int flag;
	public String msg;
	private TextView tvMsg;

	public DialogMessage(Activity pa, String pmsg) {
		super(pa);
		this.flag = 0;
		this.msg  = pmsg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
		tvMsg = (TextView) findViewById(R.id.tvMsg);
		tvMsg.setText(msg);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
		dismiss();
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
}