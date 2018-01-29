package com.busyprojects.roomies.testAct;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.busyprojects.roomies.R;
import com.busyprojects.roomies.pojos.CountryCity;
import com.busyprojects.roomies.pojos.ListTabAdapter;
import com.busyprojects.roomies.pojos.TabItems;

public class Tab1ListActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    static List<CountryCity> countryCityList;

   static List<TabItems> tabItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1_list);
        addItems();
        tabLayout = findViewById(R.id.tl_list);
        viewPager = findViewById(R.id.vp_list);

        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }


    class VpAdapter extends FragmentStatePagerAdapter {
        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFrag01.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabItemsList.get(position).getTabTitle();
        }
    }


    public static class MyFrag01 extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static MyFrag01 newInstance(int sectionNumber) {

            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            MyFrag01 fragment = new MyFrag01();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_list01, container, false);

                ListView lv_list01 = rootView.findViewById(R.id.lv_list01);
           // tv_list01.setText(countryCityList.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1).getCountry());

            ListTabAdapter listTabAdapter = new ListTabAdapter(getContext(),
                    tabItemsList.get(getArguments().getInt(ARG_SECTION_NUMBER) - 1)
                            .getCountryCityList());
            lv_list01.setAdapter(listTabAdapter);

            return rootView;
        }


    }


    void addItems() {
        tabItemsList = new ArrayList<>();

        TabItems tabItems = new TabItems();
        tabItems.setTabTitle("pending");

        countryCityList = new ArrayList<>();
        countryCityList.add(new CountryCity("city0", "India"));
        countryCityList.add(new CountryCity("city1", "Dubai"));
        countryCityList.add(new CountryCity("city2", "China"));
        countryCityList.add(new CountryCity("city4", "Usa"));
        countryCityList.add(new CountryCity("city5", "Canada"));
        countryCityList.add(new CountryCity("city6", "London"));
        countryCityList.add(new CountryCity("city7", "Romania"));
        tabItems.setCountryCityList(countryCityList);

        tabItemsList.add(tabItems);



        TabItems tabItems2 = new TabItems();
        tabItems2.setTabTitle("pending");

       List<CountryCity> countryCityList2 = new ArrayList<>();
        countryCityList2.add(new CountryCity("city02", "India2"));
        countryCityList2.add(new CountryCity("city12", "Dubai2"));
        countryCityList2.add(new CountryCity("city22", "China2"));
        countryCityList2.add(new CountryCity("city42", "Usa2"));
        countryCityList2.add(new CountryCity("city52", "Canada2"));
        countryCityList2.add(new CountryCity("city62", "London2"));
        countryCityList2.add(new CountryCity("city72", "Romania2"));
        tabItems2.setCountryCityList(countryCityList2);


        tabItemsList.add(tabItems2);


        TabItems tabItems3 = new TabItems();
        tabItems3.setTabTitle("pending");

        countryCityList = new ArrayList<>();
        countryCityList.add(new CountryCity("city03", "India3"));
        countryCityList.add(new CountryCity("city13", "Dubai3"));
        countryCityList.add(new CountryCity("city23", "China3"));
        countryCityList.add(new CountryCity("city43", "Usa3"));
        countryCityList.add(new CountryCity("city53", "Canada3"));
        countryCityList.add(new CountryCity("city63", "London3"));
        countryCityList.add(new CountryCity("city73", "Romania3"));
        tabItems3.setCountryCityList(countryCityList);

        tabItemsList.add(tabItems3);


    }


}
