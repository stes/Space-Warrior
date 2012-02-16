package sw.client.gui;

import sw.shared.data.entities.shots.IWeapon.WeaponType;

public interface IWeaponChangedListener
{
	public void weaponChanged(WeaponType w);
}
