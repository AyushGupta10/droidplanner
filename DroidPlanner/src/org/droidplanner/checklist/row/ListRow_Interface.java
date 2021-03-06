package org.droidplanner.checklist.row;

import org.droidplanner.checklist.CheckListItem;

import android.view.View;

public interface ListRow_Interface {
	public interface OnRowItemChangeListener{
		public void onRowItemChanged(View mView, CheckListItem listItem ,boolean isChecked);
		public void onRowItemGetData(CheckListItem listItem , String sysTag);
	}
    public View getView(View convertView);
    public int getViewType();
}
