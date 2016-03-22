package info.chrismcgee.sky.scheduling.beans;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import info.chrismcgee.sky.enums.PrintingCompany;

public class Job implements Serializable {

	/**
	 * Serialization
	 */
	private static final long serialVersionUID = -4626203958095426045L;
	
	// The variables this bean holds.
	private Date shipDate;
	private String jobId;
	private String customerName;
	private String customerPO;
	private Timestamp proofSpecDate;
	private Timestamp jobCompleted;
	private PrintingCompany printingCompany;
	private boolean overruns;
	private List<OrderDetail> orderDetailList;
	private boolean treeExpanded = true;
	
	// All of the getters and setters.
	public Date getShipDate() {
		return shipDate;
	}
	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerPO() {
		return customerPO;
	}
	public void setCustomerPO(String customerPO) {
		this.customerPO = customerPO;
	}
	public Timestamp getProofSpecDate() {
		return proofSpecDate;
	}
	public void setProofSpecDate(Timestamp proofSpecDate) {
		this.proofSpecDate = proofSpecDate;
	}
	public Timestamp getJobCompleted() {
		return jobCompleted;
	}
	public void setJobCompleted(Timestamp jobCompleted) {
		this.jobCompleted = jobCompleted;
	}
	public PrintingCompany getPrintingCompany() {
		return printingCompany;
	}
	public void setPrintingCompany(PrintingCompany printingCompany) {
		this.printingCompany = printingCompany;
	}
	public boolean isOverruns() {
		return overruns;
	}
	public void setOverruns(boolean overruns) {
		this.overruns = overruns;
	}
	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}
	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}
	public boolean isTreeExpanded() {
		return treeExpanded;
	}
	public void setTreeExpanded(boolean treeExpanded) {
		this.treeExpanded = treeExpanded;
	}
	
	// Override the toString() method to always return the job's customer name.
	@Override
	public String toString() {
		return getCustomerName();
	}

}
