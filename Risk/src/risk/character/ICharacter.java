package risk.character;

import risk.util.RiskEnum.RiskColor;

public interface ICharacter {
	
	public RiskColor getColor();
	public String getName();
	public boolean isTurn();
	public short getTurnID();
}
