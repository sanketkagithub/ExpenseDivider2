package com.busyprojects.roomies.testAct;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CountryCity;

public class SwipeActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static ViewPager mViewPager;
    static List<CountryCity> countryCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        countryCityList = new ArrayList<>();

        countryCityList.add(new CountryCity("city0","country0"));
        countryCityList.add(new CountryCity("city1","country1"));
        countryCityList.add(new CountryCity("city2","country2"));
        countryCityList.add(new CountryCity("city3","country3"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            Log.i("sectionNumber","=>"+sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_swipe, container, false);
            TextView city = (TextView) rootView.findViewById(R.id.tv_city);
            TextView country = (TextView) rootView.findViewById(R.id.tv_country);
            TextView but_skip = (TextView) rootView.findViewById(R.id.but_skip);
            city.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            city.setText(countryCityList.get(getArguments().getInt(ARG_SECTION_NUMBER)-1).getCity());
            country.setText(countryCityList.get(getArguments().getInt(ARG_SECTION_NUMBER)-1).getCountry());

            but_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                }
            });
            return rootView;
        }
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return countryCityList.size();
        }
    }
}
