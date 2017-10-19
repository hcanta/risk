package risk.model.character;

import risk.util.RiskEnum.RiskColor;

public interface ICharacter 
{
	
	public RiskColor getColor();
	public String getName();
	public boolean isTurn();
	public short getTurnID();
	void updateArmiestoBeplaced(int nb);
	void incrementArmies();
	void decrementArmies();
	void setNbArmiesToBePlaced(int nbArmiesToBePlaced);
	int getNbArmiesToBePlaced();
	void addTerritory(String name);
	void removeTerritory(String name);
}
