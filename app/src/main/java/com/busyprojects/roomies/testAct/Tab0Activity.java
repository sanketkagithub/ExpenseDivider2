package com.busyprojects.roomies.testAct;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CountryCity;

public class Tab0Activity extends AppCompatActivity {
    TabLayout tl;
    ViewPager vp;

    static List<CountryCity> countryCityList;
    ViewPagerCustomAdapter viewPagerCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab0);

        addItems();
        tl = findViewById(R.id.tl_sides);
        vp = findViewById(R.id.vp_sides_lcr);

        for (int i = 0; i < countryCityList.size(); i++) {
            tl.addTab(tl.newTab().setText(countryCityList.get(i).getCity()));
        }



        viewPagerCustomAdapter = new ViewPagerCustomAdapter(getSupportFragmentManager(),tl.getTabCount());
        vp.setAdapter(viewPagerCustomAdapter);

        tl.setupWithViewPager(vp);

    }

    void addItems() {
        countryCityList = new ArrayList<>();

        countryCityList.add(new CountryCity("city0", "India"));
        countryCityList.add(new CountryCity("city1", "Dubai"));
        countryCityList.add(new CountryCity("city2", "China"));
        countryCityList.add(new CountryCity("city4", "Usa"));
        countryCityList.add(new CountryCity("city5", "Canada"));
        countryCityList.add(new CountryCity("city6", "London"));
        countryCityList.add(new CountryCity("city7", "Romania"));

    }


    class ViewPagerCustomAdapter extends FragmentStatePagerAdapter {

        Fragment fragment = null;

        int numTabs;
        public ViewPagerCustomAdapter(FragmentManager fm,int numTabs) {
            super(fm);
            this.numTabs=numTabs;
        }

        @Override
        public Fragment getItem(int position) {

//            for (int i = 0; i < countryCityList.size(); i++) {
//                fragment = MyFrag.newInstance(position + 1);
//                break;
//            }
            fragment = MyFrag.newInstance(position + 1);

            return fragment;

        }

        @Override
        public int getCount() {
            return numTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return countryCityList.get(position).getCity();
        }


    }


    public static class MyFrag extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public MyFrag() {
        }


        public static MyFrag newInstance(int sectionNumber) {
            MyFrag fragment = new MyFrag();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            Log.i("sectionNumber", "=>" + sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab0, container, false);
            TextView tv_tab0 = (TextView) rootView.findViewById(R.id.tv_tab0);
            // TextView country = (TextView) rootView.findViewById(R.id.tv_country);
            // TextView but_skip = (TextView) rootView.findViewById(R.id.but_skip);
           //  tv_tab0.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

             tv_tab0.setText(countryCityList.get(getArguments().getInt(ARG_SECTION_NUMBER)-1).getCountry());
            ///city.setText(countryCityList.get(getArguments().getInt(ARG_SECTION_NUMBER)-1).getCity());
            // country.setText(countryCityList.get(getArguments().getInt(ARG_SECTION_NUMBER)-1).getCountry());

            return rootView;
        }
    }


}



