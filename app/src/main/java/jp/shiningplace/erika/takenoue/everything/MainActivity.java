package jp.shiningplace.erika.takenoue.everything;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.graphics.Typeface;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private Realm mRealm;
    private int mGenre = 0;
    private ViewPager viewPager;
    private ExampleFragmentPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ManualActivity.class);
                startActivity(intent);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarcodeActivity.class);
                startActivity(intent);
            }
        });

        TextView text = (TextView) findViewById(R.id.toolbar_title);
        text.setTypeface(Typeface.createFromAsset(getAssets(),
                "KochAntiquaZierbuchstaben.ttf"));

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        setViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String content = intentResult.getContents();
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
        }
    }


    private void setViews() {
        setSupportActionBar(toolbar);
        FragmentManager manager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        adapter = new ExampleFragmentPagerAdapter(manager);
        viewPager.setAdapter(adapter);
        setDrawer();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_drawer_navigation);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(select);
    }

    private NavigationView.OnNavigationItemSelectedListener select = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.nav_record) {
                toolbar.setTitle("記録");
                mGenre = 1;
            } else if (id == R.id.nav_lend) {
                toolbar.setTitle("貸出");
                mGenre = 2;
            } else if (id == R.id.nav_borrow) {
                toolbar.setTitle("返却");
                mGenre = 3;
            } else if (id == R.id.nav_setting) {
                toolbar.setTitle("設定");
                mGenre = 4;
                Intent intent = new Intent(MainActivity.this, BackupActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_contact) {
                toolbar.setTitle("ご意見・お問い合わせ");
                mGenre = 4;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        findViewById(R.id.action_search).setOnClickListener(mOnDoneClickListener);
        return true;
    }

    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            choosePrefecture();
        }
    };

    public void choosePrefecture() {
        final String[] items = {"登録日昇順", "登録日降順", "読了日昇順","読了日降順","作者順","出版社順"};
        new android.support.v7.app.AlertDialog.Builder((this))
                .setTitle("並び替え")
                .setItems(items, new DialogInterface.OnClickListener() {
                    Fragment fragment= (Fragment) adapter.getItem(viewPager.getCurrentItem());
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (fragment instanceof BookFragment) {
                            BookFragment bookfrag = (BookFragment) fragment;
                            if (which == 0) {
                                bookfrag.idAsListView();
                            } else if (which == 1) {
                                bookfrag.idDeListView();
                            } else if (which == 2) {
                                bookfrag.dateAsListView();
                            } else if (which == 3) {
                                bookfrag.dateDeListView();
                            } else if (which == 4) {
                                bookfrag.titleListView();
                            } else if (which == 5) {
                                bookfrag.publisherListView();
                            }
                        }
                        if (fragment instanceof CompletionFragment) {
                            CompletionFragment completionfrag = (CompletionFragment) fragment;
                            if (which == 0) {
                                completionfrag.idAsListView();
                            } else if (which == 1) {
                                completionfrag.idDeListView();
                            } else if (which == 2) {
                                completionfrag.dateAsListView();
                            } else if (which == 3) {
                                completionfrag.dateDeListView();
                            } else if (which == 4) {
                                completionfrag.titleListView();
                            } else if (which == 5) {
                                completionfrag.publisherListView();
                            }
                        }
                        if (fragment instanceof HalfwayFragment) {
                            HalfwayFragment halfwayfrag = (HalfwayFragment) fragment;
                            if (which == 0) {
                                halfwayfrag.idAsListView();
                            } else if (which == 1) {
                                halfwayfrag.idDeListView();
                            } else if (which == 2) {
                                halfwayfrag.dateAsListView();
                            } else if (which == 3) {
                                halfwayfrag.dateDeListView();
                            } else if (which == 4) {
                                halfwayfrag.titleListView();
                            } else if (which == 5) {
                                halfwayfrag.publisherListView();
                            }
                        }
                        if (fragment instanceof StillFragment) {
                            StillFragment stillfrag = (StillFragment) fragment;
                            if (which == 0) {
                                stillfrag.idAsListView();
                            } else if (which == 1) {
                                stillfrag.idDeListView();
                            } else if (which == 2) {
                                stillfrag.dateAsListView();
                            } else if (which == 3) {
                                stillfrag.dateDeListView();
                            } else if (which == 4) {
                                stillfrag.titleListView();
                            } else if (which == 5) {
                                stillfrag.publisherListView();
                            }
                        }
                        if (fragment instanceof ConcernFragment) {
                            ConcernFragment concernfrag = (ConcernFragment) fragment;
                            if (which == 0) {
                                concernfrag.idAsListView();
                            } else if (which == 1) {
                                concernfrag.idDeListView();
                            } else if (which == 2) {
                                concernfrag.dateAsListView();
                            } else if (which == 3) {
                                concernfrag.dateDeListView();
                            } else if (which == 4) {
                                concernfrag.titleListView();
                            } else if (which == 5) {
                                concernfrag.publisherListView();
                            }
                        }
                    }
                })
                .show();
    }
}

