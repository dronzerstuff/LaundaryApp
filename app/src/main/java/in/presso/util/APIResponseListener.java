package in.presso.util;

public interface APIResponseListener {
	public void onSuccess(Object profile);

	public void onError(String message);
}
