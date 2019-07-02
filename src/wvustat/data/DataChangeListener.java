package wvustat.data;

import java.util.EventListener;

public interface DataChangeListener extends EventListener{
	public void tableChanged(DataChangeEvent e);
}
