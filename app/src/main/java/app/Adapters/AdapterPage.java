package app.Adapters;

import android.util.EventLog;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import app.Fragments.CompetitionFragment;
import app.Fragments.EventFragment;
import app.Fragments.FallaFragment;
import app.Fragments.NewsFragment;

public class AdapterPage extends FragmentPagerAdapter {

    private int numTabs;

    public AdapterPage(FragmentManager fm, int numTabs){
        super(fm);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EventFragment();
            case 1:
                return new FallaFragment();
            case 2:
                return new NewsFragment();
            case 3:
                return new CompetitionFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
