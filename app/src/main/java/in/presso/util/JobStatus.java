package in.presso.util;

public class JobStatus {
	public static final String[] status = { "Pickup Pending",
			"Pickup Done", "Processing", "Out For Delivery", "Delivered",
			"Delivery Failed", "Cancelled" };

	public static String getStatusMessage(int statusCode) {
		return status[statusCode - 1];
	}
}
