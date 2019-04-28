package app.gian.db.oracle.model;

import app.gian.db.Model;




public class YgBwsOnHandVModel extends Model
{
	protected String orgId                 = null;
	protected String orgCode               = null;	
	protected String OrganizationId        = null;
	protected String inventoryItemId       = null;
	protected String item                  = null;
	protected String onHandQty             = null;
	protected String subinventoryCode      = null;
	
	
	
	
	

	/**
	 * @return
	 */
	public String getInventoryItemId() {
		return inventoryItemId;
	}

	/**
	 * @return
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @return
	 */
	public String getOnHandQty() {
		return onHandQty;
	}

	/**
	 * @return
	 */
	public String getOrganizationId() {
		return OrganizationId;
	}

	/**
	 * @return
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * @return
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @return
	 */
	public String getSubinventoryCode() {
		return subinventoryCode;
	}

	/**
	 * @param string
	 */
	public void setInventoryItemId(String string) {
		inventoryItemId = string;
	}

	/**
	 * @param string
	 */
	public void setItem(String string) {
		item = string;
	}

	/**
	 * @param string
	 */
	public void setOnHandQty(String string) {
		onHandQty = string;
	}

	/**
	 * @param string
	 */
	public void setOrganizationId(String string) {
		OrganizationId = string;
	}

	/**
	 * @param string
	 */
	public void setOrgCode(String string) {
		orgCode = string;
	}

	/**
	 * @param string
	 */
	public void setOrgId(String string) {
		orgId = string;
	}

	/**
	 * @param string
	 */
	public void setSubinventoryCode(String string) {
		subinventoryCode = string;
	}

}
