package app.gian.db.oracle.model;

import app.gian.db.Model;




public class YgBwsCiCancelTempModel extends Model
{
	protected String creationDate          = null;
	protected String importDate            = null;
	protected String importFlag            = null;
	protected String orgId                 = null;
	protected String orgCode               = null;
	protected String orgManageNumber       = null;
	protected String ciNo                  = null;
	protected String plNumber              = null;
	protected String suninventory          = null;
	
	

	/**
	 * @return
	 */
	public String getCiNo() {
		return ciNo;
	}

	/**
	 * @return
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * @return
	 */
	public String getImportDate() {
		return importDate;
	}

	/**
	 * @return
	 */
	public String getImportFlag() {
		return importFlag;
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
	public String getOrgManageNumber() {
		return orgManageNumber;
	}

	/**
	 * @return
	 */
	public String getPlNumber() {
		return plNumber;
	}

	/**
	 * @return
	 */
	public String getSuninventory() {
		return suninventory;
	}

	/**
	 * @param string
	 */
	public void setCiNo(String string) {
		ciNo = string;
	}

	/**
	 * @param string
	 */
	public void setCreationDate(String string) {
		creationDate = string;
	}

	/**
	 * @param string
	 */
	public void setImportDate(String string) {
		importDate = string;
	}

	/**
	 * @param string
	 */
	public void setImportFlag(String string) {
		importFlag = string;
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
	public void setOrgManageNumber(String string) {
		orgManageNumber = string;
	}

	/**
	 * @param string
	 */
	public void setPlNumber(String string) {
		plNumber = string;
	}

	/**
	 * @param string
	 */
	public void setSuninventory(String string) {
		suninventory = string;
	}

}
