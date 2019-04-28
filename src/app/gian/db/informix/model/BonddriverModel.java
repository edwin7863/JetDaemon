package app.gian.db.informix.model;

import app.gian.db.Model;




public class BonddriverModel extends Model
{
	protected String compcode =null;
	protected String carxname =null;
	protected String carxnubr =null;
	protected String drivernm =null;
	protected String regiuser =null;
	protected String regidate =null;
	protected String modiuser =null;
	protected String modidate =null;
	protected String serialno =null;
	protected String deFlag   =null;
	
	
	/**
	 * @return
	 */
	public String getCarxname() {
		return carxname;
	}

	/**
	 * @return
	 */
	public String getCarxnubr() {
		return carxnubr;
	}

	/**
	 * @return
	 */
	public String getCompcode() {
		return compcode;
	}

	/**
	 * @return
	 */
	public String getDeFlag() {
		return deFlag;
	}

	/**
	 * @return
	 */
	public String getDrivernm() {
		return drivernm;
	}

	/**
	 * @return
	 */
	public String getModidate() {
		return modidate;
	}

	/**
	 * @return
	 */
	public String getModiuser() {
		return modiuser;
	}

	/**
	 * @return
	 */
	public String getRegidate() {
		return regidate;
	}

	/**
	 * @return
	 */
	public String getRegiuser() {
		return regiuser;
	}

	/**
	 * @return
	 */
	public String getSerialno() {
		return serialno;
	}

	/**
	 * @param string
	 */
	public void setCarxname(String string) {
		carxname = string;
	}

	/**
	 * @param string
	 */
	public void setCarxnubr(String string) {
		carxnubr = string;
	}

	/**
	 * @param string
	 */
	public void setCompcode(String string) {
		compcode = string;
	}

	/**
	 * @param string
	 */
	public void setDeFlag(String string) {
		deFlag = string;
	}

	/**
	 * @param string
	 */
	public void setDrivernm(String string) {
		drivernm = string;
	}

	/**
	 * @param string
	 */
	public void setModidate(String string) {
		modidate = string;
	}

	/**
	 * @param string
	 */
	public void setModiuser(String string) {
		modiuser = string;
	}

	/**
	 * @param string
	 */
	public void setRegidate(String string) {
		regidate = string;
	}

	/**
	 * @param string
	 */
	public void setRegiuser(String string) {
		regiuser = string;
	}

	/**
	 * @param string
	 */
	public void setSerialno(String string) {
		serialno = string;
	}

}
