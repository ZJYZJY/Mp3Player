package com.zjy.mp3player.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * com.hdu.feiniu_driver.adapter
 * Created by 73958 on 2017/3/22.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> tagList = new ArrayList<>();
    private Context context;

    public ViewPagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

        @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //把tag存起来
        tagList.add(makeFragmentName(container.getId(), getItemId(position)));
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        super.destroyItem(container, position, object);
        //把tag删掉
        tagList.remove(makeFragmentName(container.getId(), getItemId(position)));
    }

//    public void update(int item){
//        Fragment fragment = ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(tagList.get(item));
//        if(fragment != null){
//            switch (item) {
//                case 0:
//                    ((DiscoverFragment) fragment).update();
//                    break;
//                case 1:
//                    ((OrderFragment) fragment).update();
//                    break;
//                case 2:
//                    ((MineFragment) fragment).update();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
