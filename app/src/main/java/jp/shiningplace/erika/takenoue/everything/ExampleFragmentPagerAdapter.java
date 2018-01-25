package jp.shiningplace.erika.takenoue.everything;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExampleFragmentPagerAdapter extends FragmentPagerAdapter {
    public ExampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BookFragment.newInstance(android.R.color.transparent);
            case 1:
                return CompletionFragment.newInstance(android.R.color.transparent);
            case 2:
                return HalfwayFragment.newInstance(android.R.color.transparent);
            case 3:
                return StillFragment.newInstance(android.R.color.transparent);
            case 4:
                return ConcernFragment.newInstance(android.R.color.transparent);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "本棚";
            case 1:
                return "読了本";
            case 2:
                return "読書中";
            case 3:
                return "積読本";
            case 4:
                return "気になる";
        }
        return null;
    }

}