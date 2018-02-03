package jp.shiningplace.erika.takenoue.everything;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ExampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private BookFragment bookFragment;
    private CompletionFragment completionFragment;
    private HalfwayFragment halfwayFragment;
    private StillFragment stillFragment;
    private ConcernFragment concernFragment;
    public ExampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(bookFragment==null){
                    bookFragment=BookFragment.newInstance(android.R.color.transparent);
                }
                return bookFragment;
            case 1:
                if(completionFragment==null){
                    completionFragment=CompletionFragment.newInstance(android.R.color.transparent);
                }
                return completionFragment;
            case 2:
                if(halfwayFragment==null){
                    halfwayFragment=HalfwayFragment.newInstance(android.R.color.transparent);
                }
                return halfwayFragment;
            case 3:
                if(stillFragment==null){
                    stillFragment=StillFragment.newInstance(android.R.color.transparent);
                }
                return stillFragment;
            case 4:
                if(concernFragment==null){
                    concernFragment=ConcernFragment.newInstance(android.R.color.transparent);
                }
                return concernFragment;
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