/**
 * 
 */
package org.libermundi.theorcs.domain.jpa.chronicle;

/**
 * A Dice has a diceType and may have a value
 *
 */
public class Dice {

	private Integer diceType;
	private Integer value;
	
	/**
	 * @param diceType
	 * @param value
	 */
	public Dice(Integer diceType, Integer value) {
		super();
		
		this.diceType = diceType;
		this.value = value;

		//Default is a D6
		if(this.diceType==null) {
			this.diceType = 6;
		}
	}
	
	/**
	 * @return the diceType
	 */
	public Integer getDiceType() {
		return diceType;
	}
	/**
	 * @param diceType the diceType to set
	 */
	public void setDiceType(Integer diceType) {
		this.diceType = diceType;
	}
	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return getValue()+"/"+getDiceType();
	}
}
