package in.presso.pojo;

import java.io.Serializable;

public class OrderData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private int statusCode;
	private String pickUpdate;
	private String pickUptime;
	private String amount;
	private String deliveryDate;
	private int orderType;
	private boolean isExpress;
	private int noOfItems;
	private int orderId;
	
	
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	private String deliveryTime;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPickUpDate() {
		return pickUpdate;
	}

	public void setPickUpDate(String date) {
		this.pickUpdate = date;
	}
	
	public String getPickUpTime() {
		return pickUptime;
	}

	public void setPickUpTime(String time) {
		this.pickUptime = time;
	}

	/**
	 * @return the deliveryDate
	 */
	public String getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the isExpress
	 */
	public boolean isExpress() {
		return isExpress;
	}

	/**
	 * @param isExpress the isExpress to set
	 */
	public void setExpress(boolean isExpress) {
		this.isExpress = isExpress;
	}

	/**
	 * @return the orderType
	 */
	public int getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return the noOfItems
	 */
	public int getNoOfItems() {
		return noOfItems;
	}

	/**
	 * @param noOfItems the noOfItems to set
	 */
	public void setNoOfItems(int noOfItems) {
		this.noOfItems = noOfItems;
	}
}
