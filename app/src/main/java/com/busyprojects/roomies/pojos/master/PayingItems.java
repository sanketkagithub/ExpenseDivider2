package com.busyprojects.roomies.pojos.master;

import android.content.Context;

import com.busyprojects.roomies.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanket on 3/11/2018.
 */

public  class PayingItems {
    private static  PayingItems ourInstance;

    private Map<String,String> itemsMap;
   public static PayingItems getInstance() {


       if (ourInstance==null)
       {
           ourInstance = new PayingItems();
       }
        return ourInstance;
    }

    private PayingItems() {
    }




  public  void setPayingItemsMap(Context context)
    {
        itemsMap = new HashMap<>();


        itemsMap.put("flat bean", context.getResources().getString(R.string.flat_bean));
        itemsMap.put("bean", context.getResources().getString(R.string.flat_bean));
        itemsMap.put("long bean", context.getResources().getString(R.string.long_bean));

        itemsMap.put("atta", context.getResources().getString(R.string.atta));
        itemsMap.put("wheat flour", context.getResources().getString(R.string.atta));



        itemsMap.put("baigun", context.getResources().getString(R.string.baigun));
        itemsMap.put("brinjal", context.getResources().getString(R.string.baigun));
        itemsMap.put("egg plant", context.getResources().getString(R.string.baigun));
        itemsMap.put("eggplant", context.getResources().getString(R.string.baigun));
        itemsMap.put("Ashgourd", context.getResources().getString(R.string.baigun));
        itemsMap.put("aubergine", context.getResources().getString(R.string.baigun));


        itemsMap.put("beetrut", context.getResources().getString(R.string.beetrut));
        itemsMap.put("beetroot", context.getResources().getString(R.string.beetrut));
        itemsMap.put("bitroot", context.getResources().getString(R.string.beetrut));
        itemsMap.put("bitrut", context.getResources().getString(R.string.beetrut));


        itemsMap.put("beer", context.getResources().getString(R.string.beer));
        itemsMap.put("wine", context.getResources().getString(R.string.beer));


        itemsMap.put("chicken biryani", context.getResources().getString(R.string.beriyani));
        itemsMap.put("mutton biryani", context.getResources().getString(R.string.beriyani));
        itemsMap.put("biryani", context.getResources().getString(R.string.beriyani));
        itemsMap.put("beriyani", context.getResources().getString(R.string.beriyani));



        itemsMap.put("Ladies Finger", context.getResources().getString(R.string.bhindi));
        itemsMap.put("Lady's Finger", context.getResources().getString(R.string.bhindi));
        itemsMap.put("okra", context.getResources().getString(R.string.bhindi));
        itemsMap.put("okro", context.getResources().getString(R.string.bhindi));
        itemsMap.put("bhindi", context.getResources().getString(R.string.bhindi));
        itemsMap.put("bhendi", context.getResources().getString(R.string.bhindi));



        itemsMap.put("Black pepper", context.getResources().getString(R.string.black_pepper));
        itemsMap.put("pepper", context.getResources().getString(R.string.black_pepper));


        itemsMap.put("bread", context.getResources().getString(R.string.bread));
itemsMap.put("biscuit", context.getResources().getString(R.string.biscuit));
itemsMap.put("burger", context.getResources().getString(R.string.burger));
itemsMap.put("capsicum", context.getResources().getString(R.string.capsicum));
itemsMap.put("carrot", context.getResources().getString(R.string.carrot));
itemsMap.put("chicken", context.getResources().getString(R.string.chicken));
itemsMap.put("chicken masala", context.getResources().getString(R.string.chicken_masala));
itemsMap.put("cigarete", context.getResources().getString(R.string.cigarete));
itemsMap.put("coconut", context.getResources().getString(R.string.coconut));
itemsMap.put("colddrink", context.getResources().getString(R.string.colddrink));
itemsMap.put("corainder powder", context.getResources().getString(R.string.corainder_powder));
itemsMap.put("corn flour", context.getResources().getString(R.string.corn_flour));
itemsMap.put("cucumber", context.getResources().getString(R.string.cucumber));
itemsMap.put("curry", context.getResources().getString(R.string.curry));
itemsMap.put("curry leaves", context.getResources().getString(R.string.curry_leaves));
itemsMap.put("cylinder", context.getResources().getString(R.string.cylinder));
itemsMap.put("dahi", context.getResources().getString(R.string.dahi));
itemsMap.put("dal", context.getResources().getString(R.string.dal));
itemsMap.put("dates", context.getResources().getString(R.string.dates));
itemsMap.put("dhaniya", context.getResources().getString(R.string.dhaniya));
itemsMap.put("drumstick", context.getResources().getString(R.string.drumstick));
itemsMap.put("dustbin", context.getResources().getString(R.string.dustbin));
itemsMap.put("egg", context.getResources().getString(R.string.egg));
itemsMap.put("electric", context.getResources().getString(R.string.electric));
itemsMap.put("fish", context.getResources().getString(R.string.fish));
itemsMap.put("flat bean", context.getResources().getString(R.string.flat_bean));
itemsMap.put("full gobi", context.getResources().getString(R.string.full_gobi));
itemsMap.put("garlic", context.getResources().getString(R.string.garlic));
itemsMap.put("ginger garlic paste", context.getResources().getString(R.string.gg_paste));
itemsMap.put("ginger", context.getResources().getString(R.string.ginger));
itemsMap.put("grapes", context.getResources().getString(R.string.grapes));
itemsMap.put("green chilli", context.getResources().getString(R.string.green_chilli));
itemsMap.put("haldi", context.getResources().getString(R.string.haldi));
itemsMap.put("internet", context.getResources().getString(R.string.internet));
itemsMap.put("jeera", context.getResources().getString(R.string.jeera));
itemsMap.put("jeera powder", context.getResources().getString(R.string.jeera_powder));
itemsMap.put("karela", context.getResources().getString(R.string.karela));
itemsMap.put("lemon", context.getResources().getString(R.string.lemon));
itemsMap.put("long bean", context.getResources().getString(R.string.long_bean));
itemsMap.put("maggi", context.getResources().getString(R.string.maggi));
itemsMap.put("milk", context.getResources().getString(R.string.milk));
itemsMap.put("mushroom", context.getResources().getString(R.string.mushroom));
itemsMap.put("mustard", context.getResources().getString(R.string.mustard));
itemsMap.put("mutter", context.getResources().getString(R.string.mutter));
itemsMap.put("mutton", context.getResources().getString(R.string.mutton));
itemsMap.put("mutton masala", context.getResources().getString(R.string.mutton_masala));
itemsMap.put("oats", context.getResources().getString(R.string.oats));
itemsMap.put("oil", context.getResources().getString(R.string.oil));
itemsMap.put("onion", context.getResources().getString(R.string.onion));
itemsMap.put("paneer", context.getResources().getString(R.string.panner));
itemsMap.put("patta gobi", context.getResources().getString(R.string.patta_gobi));
itemsMap.put("pickel", context.getResources().getString(R.string.pickel));
itemsMap.put("pizza", context.getResources().getString(R.string.pizza));
itemsMap.put("pizza", context.getResources().getString(R.string.pizza));
itemsMap.put("potatoe", context.getResources().getString(R.string.potatoe));
itemsMap.put("prawn", context.getResources().getString(R.string.prawn));
itemsMap.put("rawa", context.getResources().getString(R.string.rawa));
itemsMap.put("red mirch", context.getResources().getString(R.string.red_mirch));
itemsMap.put("red mirch powder", context.getResources().getString(R.string.red_mirch_powder));
itemsMap.put("rice", context.getResources().getString(R.string.rice));
itemsMap.put("roti", context.getResources().getString(R.string.roti));
itemsMap.put("sabji masala", context.getResources().getString(R.string.sabji_masala));
itemsMap.put("salt", context.getResources().getString(R.string.salt));
itemsMap.put("sauce", context.getResources().getString(R.string.sauce));
itemsMap.put("scrotch", context.getResources().getString(R.string.scrotch));
itemsMap.put("sugar", context.getResources().getString(R.string.sugar));
itemsMap.put("tomatoe", context.getResources().getString(R.string.tomatoe));
itemsMap.put("vim", context.getResources().getString(R.string.vim));
itemsMap.put("water", context.getResources().getString(R.string.water));







    }


  public   Map<String,String> getItemsMap()
    {
        return itemsMap;
    }




}
