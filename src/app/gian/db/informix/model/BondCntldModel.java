package app.gian.db.informix.model;

import app.gian.db.Model;




public class BondCntldModel extends Model
{
	protected String repttype = null;
	protected String bondnubr = null;
	protected String refnubr  = null;
	protected String cntlitem = null;
	protected String compcode = null;
	
	
	
	/**
	 * @return
	 */
	public String getBondnubr() {
		return bondnubr;
	}

	/**
	 * @return
	 */
	public String getCntlitem() {
		return cntlitem;
	}

	/**
	 * @return
	 */
	public String getRefnubr() {
		return refnubr;
	}

	/**
	 * @return
	 */
	public String getRepttype() {
		return repttype;
	}

	/**
	 * @param string
	 */
	public void setBondnubr(String string) {
		bondnubr = string;
	}

	/**
	 * @param string
	 */
	public void setCntlitem(String string) {
		cntlitem = string;
	}

	/**
	 * @param string
	 */
	public void setRefnubr(String string) {
		refnubr = string;
	}

	/**
	 * @param string
	 */
	public void setRepttype(String string) {
		repttype = string;
	}

	/**
	 * @return
	 */
	public String getCompcode() {
		return compcode;
	}

	/**
	 * @param string
	 */
	public void setCompcode(String string) {
		compcode = string;
	}

}
