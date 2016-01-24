package com.blue.ironarchivev1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
//import android.app.Fragment;
//import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.blue.ironarchivev1.db.RoutineDAO;

public class RoutineFragmentContainer extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private int routineID = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()){
		case R.id.action_settings:
			intent= new Intent(RoutineFragmentContainer.this, SettingsActivity.class);
		    startActivity(intent);
		    return true;
		case R.id.action_help:
			intent = new Intent(RoutineFragmentContainer.this, TutorialActivity.class);
		    startActivity(intent);
		    return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		routineID = extras.getInt("routineID", 1);
		setContentView(R.layout.activity_fragment_container);
		final Configuration mConfiguration = getResources().getConfiguration();
		
		final RoutineDAO routineDAO = new RoutineDAO(this);
		if(!routineDAO.getWorkoutItem(routineID).getName().equals("Default")){
			setTitle(getResources().getString(R.string.app_name) + "   -   " + routineDAO.getWorkoutItem(routineID).getName());
		}
		
		if(mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE){
			final ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			
			OnNavigationListener spinnerNavigationLister = new ActionBar.OnNavigationListener() {
				
				@Override
				public boolean onNavigationItemSelected(int itemPosition, long itemId) {
					// When the given dropdown item is selected, show its contents in the
					// container view.
					ItemsListFragment fragment = new ItemsListFragment(); 
					fragment.setRoutineId(routineID);
					fragment.setListType(itemPosition);
					getSupportFragmentManager().beginTransaction().replace(R.id.landpager, fragment).commit();
					return true;
				}
			};
			
			// Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			//mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
	
			// Set up the ViewPager with the sections adapter.
			//mViewPager = (ViewPager) findViewById(R.id.pager);
			//mViewPager.setAdapter(mSectionsPagerAdapter);
			
			List<String> items = new ArrayList<String>();
			items.add(getString(R.string.title_section1));
			items.add(getString(R.string.title_section2));
			items.add(getString(R.string.title_section3));
			items.add(getString(R.string.title_section4));
			
			ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this, R.layout.workout_list_spinner, items);
			
			actionBar.setListNavigationCallbacks(aAdpt, spinnerNavigationLister);			
		}
		else if(mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT){
				
			// Set up the action bar.
			final ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
			// Create the adapter that will return a fragment for each of the three
			// primary sections of the app.
			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getSupportFragmentManager());
	
			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
	
			// When swiping between different sections, select the corresponding
			// tab. We can also use ActionBar.Tab#select() to do this if we have
			// a reference to the Tab.
			mViewPager
					.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							actionBar.setSelectedNavigationItem(position);
						}
					});
	
			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title defined by
				// the adapter. Also specify this Activity object, which implements
				// the TabListener interface, as the callback (listener) for when
				// this tab is selected.
				actionBar.addTab(actionBar.newTab()
						.setText(mSectionsPagerAdapter.getPageTitle(i))
						.setTabListener(this));
			}
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			ItemsListFragment fragment = new ItemsListFragment(); 
			fragment.setRoutineId(routineID);
			fragment.setListType(position);
		    /*switch(position){
		    case 0:
		         fragment.setListType(0);
		         break;
		    case 1:
		         fragment.setListType(1);
		         break;
		    case 2:
		         fragment.setListType(2);
		         break;
		    case 3:
		         fragment.setListType(3);
		         break;
		    default:
		         throw new IllegalArgumentException("Invalid section number");
		    }*/

		    return fragment;
		}

		@Override
		public int getCount() {
			// Show total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}
}
