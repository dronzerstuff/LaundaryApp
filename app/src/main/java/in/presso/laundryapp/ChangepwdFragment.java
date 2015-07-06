package in.presso.laundryapp;

import in.presso.util.APICallTaskForPost;
import in.presso.util.APIResponseListener;
import in.presso.util.FireToast;
import in.presso.util.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChangepwdFragment extends Fragment implements OnClickListener {

	private static Context mContext;
	private static View mView;
	EditText editCurrentpwd, editNewpwd, editconfirmnewpwd;
	Button btn_save;

	public ChangepwdFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_changepwd,
				container, false);
		mView = rootView;
		mContext = rootView.getContext();
		init();
		return rootView;
	}

	private void init() {
		mView.findViewById(R.id.btnMenu).setOnClickListener(this);

		editCurrentpwd = (EditText) mView.findViewById(R.id.editCurrentpwd);
		editNewpwd = (EditText) mView.findViewById(R.id.editNewpwd);
		editconfirmnewpwd = (EditText) mView
				.findViewById(R.id.editconfirmnewpwd);

		btn_save = (Button) mView.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnMenu:
			((HomeActivity) mContext).openDrawer();
			break;
		case R.id.btn_save:
			if ((editCurrentpwd.getText().toString().length() > 0)) {
				if ((editNewpwd.getText().toString().length() > 0)) {
					if ((editconfirmnewpwd.getText().toString().length() > 0)) {
						if (editNewpwd.getText().toString()
								.equals(editconfirmnewpwd.getText().toString())) {
							APICallTaskForPost apiCallTask = new APICallTaskForPost(
									mContext, listener, null, true);
							apiCallTask
									.execute("http://www.presso.in/service.svc/UpdatePassword/"
											+ editNewpwd.getText().toString()
											+ "/"
											+ SharedPrefUtils
													.getKey_id(mContext));
							System.out
									.println("url = "
											+ "http://www.presso.in/service.svc/UpdatePassword/"
											+ editNewpwd.getText().toString()
											+ "/"
											+ SharedPrefUtils
													.getKey_id(mContext));
						} else {
							AlertDialog.Builder alert = new AlertDialog.Builder(
									mContext);
							alert.setMessage("Confirm password must me same as new password.");
							alert.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									});
							alert.show();
							return;
						}
					} else {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								mContext);
						alert.setMessage("Please enter confirm password .");
						alert.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
						alert.show();
						return;
					}
				} else {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							mContext);
					alert.setMessage("Please enter new password.");
					alert.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					alert.show();
					return;
				}
			} else {
				AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
				alert.setMessage("Please enter current password.");
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				alert.show();
				return;
			}
			break;
		}
	}

	APIResponseListener listener = new APIResponseListener() {

		@Override
		public void onSuccess(Object profile) {
			JSONObject object = (JSONObject) profile;
			try {
				if (object.getBoolean("success")) {
					HomeActivity.show_dialog(mContext,
							"Password changed successfully.");
				} else {
					HomeActivity.show_dialog(mContext,
							"Sorry! Unable to change password. Try again.");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onError(String message) {
			FireToast.makeToast(mContext, message);
		}
	};
}
