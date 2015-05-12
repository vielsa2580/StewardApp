package com.example.stewardimperial.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MODELTableOrder implements Comparable<MODELTableOrder>{

	String orderId;
	List<MODELTable> tablePerOrder;
	int seatsTaken;
	String takenStatus;
	String isSelected;
	String joinNormalSplit;
	List<MODELMenuItems> menuItemList;
	List<MODELMenuItems> orderCookedItemList;

	//================================ Constructors=====================================================================================	
	public MODELTableOrder(){
		super();
	}
	
	public MODELTableOrder(MODELTableOrder modelTableOrder){
		super();
		this.orderId = new String(modelTableOrder.getOrderId());
		this.seatsTaken = modelTableOrder.getSeatsTaken();
		this.takenStatus = new String(modelTableOrder.getTakenStatus());
		this.isSelected = new String(modelTableOrder.getIsSelected());
		this.joinNormalSplit = new String(modelTableOrder.getJoinNormalSplit());
		
		tablePerOrder = new ArrayList<MODELTable>();
		
		for (int i = 0; i < modelTableOrder.getTablePerOrder().size(); i++) {
			tablePerOrder.add(new MODELTable(modelTableOrder.getTablePerOrder().get(i)));
		}
	}

	//================================ Getter & Setter==================================================================================


	public List<MODELMenuItems> getOrderCookedItemList() {
		return orderCookedItemList;
	}

	public void setOrderCookedItemList(List<MODELMenuItems> orderCookedItemList) {
		this.orderCookedItemList = orderCookedItemList;
	}
	
	
	public List<MODELMenuItems> getMenuItemList() {
		return menuItemList;
	}

	public void setMenuItemList(List<MODELMenuItems> menuItemList) {
		this.menuItemList = menuItemList;
	}

	
	public String getJoinNormalSplit() {
		return joinNormalSplit;
	}

	public void setJoinNormalSplit(String joinNormalSplit) {
		this.joinNormalSplit = joinNormalSplit;
	}
	
	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getOrderId() {
		return orderId;
	}
	

	public String getTakenStatus() {
		return takenStatus;
	}

	public void setTakenStatus(String takenStatus) {
		this.takenStatus = takenStatus;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<MODELTable> getTablePerOrder() {
		return tablePerOrder;
	}

	public void setTablePerOrder(List<MODELTable> tablePerOrder) {
		this.tablePerOrder = tablePerOrder;
	}

	public int getSeatsTaken() {
		return seatsTaken;
	}

	public void setSeatsTaken(int seatsTaken) {
		this.seatsTaken = seatsTaken;
	}


	//==============================================================================OVERRIDDEN METHODS=============================================================================

	@Override
	public int compareTo(MODELTableOrder another) {


		int comparator = another.getTablePerOrder().get(0).getId();
		return (int)(this.getTablePerOrder().get(0).getId()-comparator);
	}

	public static Comparator<MODELTableOrder> tableIdComparator = new Comparator<MODELTableOrder>() {

		@Override
		public int compare(MODELTableOrder lhs, MODELTableOrder rhs) {

			String one = ""+lhs.getTablePerOrder().get(0).getId();
			String two = ""+rhs.getTablePerOrder().get(0).getId();

			return two.compareTo(one);
		}
	};

}
