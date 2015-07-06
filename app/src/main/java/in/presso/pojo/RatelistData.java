package in.presso.pojo;

import java.io.Serializable;

public class RatelistData implements Serializable
{
	private String title;
	private String amount;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
