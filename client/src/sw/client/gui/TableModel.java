/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011-2012 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.client.gui;

import javax.swing.table.AbstractTableModel;

class TableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 6220705373064394932L;

	private String[] _headers = { "Server", "Players/Max" };

	@Override
	public int getColumnCount()
	{
		return _headers.length;
	}

	@Override
	public String getColumnName(int col)
	{
		return _headers[col];
	}

	@Override
	public int getRowCount()
	{
		return _servers.size();
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		switch (col)
		{
			case 0:
				return _servers.get(row).getServerName();
			case 1:
				return _servers.get(row).getMaxPlayers() + "/"
						+ _servers.get(row).getNumPayers();
			default:
				return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
}

