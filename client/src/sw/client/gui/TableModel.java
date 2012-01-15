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

