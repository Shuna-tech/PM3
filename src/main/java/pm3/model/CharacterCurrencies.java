package pm3.model;

public class CharacterCurrencies {
    protected CharacterInfo character;
    protected Currencies currency;
    protected int totalAmountOwned;
    protected int amountThisWeek;
    
    public CharacterCurrencies(CharacterInfo character, Currencies currency, int totalAmountOwned, int amountThisWeek) {
        this.character = character;
        this.currency = currency;
        this.totalAmountOwned = totalAmountOwned;
        this.amountThisWeek = amountThisWeek;
    }

	public CharacterInfo getCharacter() {
		return character;
	}

	public void setCharacter(CharacterInfo character) {
		this.character = character;
	}

	public Currencies getCurrency() {
		return currency;
	}

	public void setCurrency(Currencies currency) {
		this.currency = currency;
	}

	public int getTotalAmountOwned() {
		return totalAmountOwned;
	}

	public void setTotalAmountOwned(int totalAmountOwned) {
		this.totalAmountOwned = totalAmountOwned;
	}

	public int getAmountThisWeek() {
		return amountThisWeek;
	}

	public void setAmountThisWeek(int amountThisWeek) {
		this.amountThisWeek = amountThisWeek;
	}
    
}