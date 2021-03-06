package org.droidplanner.fragments.mission;

import org.droidplanner.DroidPlannerApp;
import org.droidplanner.drone.variables.mission.Mission;
import org.droidplanner.drone.variables.mission.MissionItem;
import org.droidplanner.drone.variables.mission.waypoints.SpatialCoordItem;
import org.droidplanner.fragments.mission.MissionItemTypes.InvalidItemException;
import org.droidplanner.widgets.spinners.SpinnerSelfSelect;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import org.droidplanner.R;

public abstract class MissionDetailFragment extends DialogFragment implements
		OnItemSelectedListener {

	public interface OnWayPointTypeChangeListener{
		public void onWaypointTypeChanged(MissionItem newItem, MissionItem oldItem);
	}

	protected abstract int getResource();

	protected SpinnerSelfSelect typeSpinner;
	protected AdapterMissionItens commandAdapter;
	protected Mission mission;
	private OnWayPointTypeChangeListener mListener;
	private TextView waypointIndex;

	protected MissionItem item;
	private TextView distanceView;
	private TextView distanceLabelView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(getResource(), null);
		setupViews(view);
        applyProfie(view);
		return view;
	}

	protected void setupViews(View view) {
		typeSpinner = (SpinnerSelfSelect) view
				.findViewById(R.id.spinnerWaypointType);
		commandAdapter = new AdapterMissionItens(this.getActivity(),
				android.R.layout.simple_list_item_1, MissionItemTypes.values());
		typeSpinner.setAdapter(commandAdapter);
		typeSpinner.setOnItemSelectedListener(this);
		waypointIndex = (TextView) view.findViewById(R.id.WaypointIndex);
		Integer temp = mission.getNumber(item);
		waypointIndex.setText( temp.toString());

		distanceView = (TextView) view.findViewById(R.id.DistanceValue);

		distanceLabelView = (TextView) view.findViewById(R.id.DistanceLabel);

		try{
			distanceLabelView.setVisibility(View.VISIBLE);
			distanceView.setText(mission.getDistanceFromLastWaypoint((SpatialCoordItem) item).toString());
		}catch(NullPointerException e){
			// Can fail if distanceView doesn't exists
		}catch (Exception e){
			//Or if the last item doesn't have a coordinate
			//distanceView.setText("");
			//distanceLabelView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mission = ((DroidPlannerApp) getActivity().getApplication()).drone.mission;
		mListener = (OnWayPointTypeChangeListener) activity;
	}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int position,
			long id) {

		MissionItemTypes selected = commandAdapter.getItem(position);
		try {
			MissionItem newItem = selected.getNewItem(getItem());
			if (!newItem.getClass().equals(getItem().getClass())) {
				Log.d("CLASS", "Diferent waypoint Classes");
				mListener.onWaypointTypeChanged(newItem, getItem());
			}
		} catch (InvalidItemException e) {
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	public MissionItem getItem() {
		return item;
	}

	public void setItem(MissionItem item) {
		this.item = item;
	}

    private void applyProfie(View view) {
        final DroidPlannerApp app = (DroidPlannerApp) getActivity().getApplication();
        app.drone.profile.applyMissionViewProfile(view, getResource());
    }

}
