package info.chrismcgee.sky.scheduling.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import info.chrismcgee.sky.enums.PrintType;

public class OrderDetail implements Serializable {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 2052528465672254035L;
	
	// The variables this bean holds.
	private int id;
	private String orderId;
	private String productId;
	private String productDetail;
	private PrintType printType;
	private long numColors;
	private long quantity;
	private Timestamp itemCompleted;
	private int proofNum;
	private Timestamp proofDate;
	private String thumbnail; 
	
	// All of the getters and setters.
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductDetail() {
		return productDetail;
	}
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	public PrintType getPrintType() {
		return printType;
	}
	public void setPrintType(PrintType printType) {
		this.printType = printType;
	}
	public long getNumColors() {
		return numColors;
	}
	public void setNumColors(long numColors) {
		this.numColors = numColors;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public Timestamp getItemCompleted() {
		return itemCompleted;
	}
	public void setItemCompleted(Timestamp itemCompleted) {
		this.itemCompleted = itemCompleted;
	}
	public int getProofNum() {
		return proofNum;
	}
	public void setProofNum(int proofNum) {
		this.proofNum = proofNum;
	}
	public Timestamp getProofDate() {
		return proofDate;
	}
	public void setProofDate(Timestamp proofDate) {
		this.proofDate = proofDate;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	// Override the toString() method to always return the product's Id (N10, ST16, etc.).
	@Override
	public String toString() {
		return getProductId();
	}

}
