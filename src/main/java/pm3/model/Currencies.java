package pm3.model;

public class Currencies {
    protected int currencyID;
    protected String currencyName;
    protected int maxAmount;
    protected int weeklyCap;
    
    public Currencies(int currencyID, String currencyName, int maxAmount, int weeklyCap) {
        this.currencyID = currencyID;
        this.currencyName = currencyName;
        this.maxAmount = maxAmount;
        this.weeklyCap = weeklyCap;
    }
    
    public Currencies(int currencyID) {
        this.currencyID = currencyID;
    }
    
    public Currencies(String currencyName, int maxAmount, int weeklyCap) {
        this.currencyName = currencyName;
        this.maxAmount = maxAmount;
        this.weeklyCap = weeklyCap;
    }

	public int getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(int currencyID) {
		this.currencyID = currencyID;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

	public int getWeeklyCap() {
		return weeklyCap;
	}

	public void setWeeklyCap(int weeklyCap) {
		this.weeklyCap = weeklyCap;
	}
    
}