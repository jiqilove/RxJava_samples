package keye.com.rxjavaobserver.fragments.rxbindingFragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import keye.com.rxjavaobserver.R;
import rx.Subscription;
import rx.functions.Action1;

public class SimpleClickFragment extends Fragment {
    private static SimpleClickFragment fragment;
    private ListView mListView;
    private Button mButton;
    private View rootView;
    private List<String> mList;
    private Subscription clickSubscription;

    public SimpleClickFragment() {
        // Required empty public constructor
    }

    //��ȡfragment����
    public static SimpleClickFragment getInstance() {
        if (fragment == null) {
            fragment = new SimpleClickFragment();
        }
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����ģ������
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {//��ʼ���ؼ�
            rootView = inflater.inflate(R.layout.fragment_simple_click, null);
            mListView = (ListView) rootView.findViewById(R.id.mlistview);
            mButton = (Button) rootView.findViewById(R.id.clicks_btn);
        }
        //��ʼ��������,������
        initAdapter();
        initListener();

        return rootView;
    }

    private int i = 0;

    private void initListener() {
        //���������������
        clickSubscription = RxView.clicks(mButton).throttleFirst(600, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Snackbar.make(mButton, "������" + ++i + "���¼�", Snackbar.LENGTH_SHORT).show();
            }
        });

        //��������
        RxView.longClicks(mButton).subscribe(aVoid -> {
            Snackbar.make(mButton, "Longclick", Snackbar.LENGTH_SHORT).show();
        });
        //item�������
        RxAdapterView.itemClicks(mListView)
                .subscribe(position -> {
                    Snackbar.make(mButton, "item click " + position, Snackbar.LENGTH_SHORT).show();
                });
        //item��������
        RxAdapterView.itemLongClicks(mListView)
                .subscribe(position -> {
                    Snackbar.make(mButton, "item longclick " + position, Snackbar.LENGTH_SHORT).show();
                });

    }

    private void initAdapter() {
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mList));
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("item0" + i);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (clickSubscription != null) {
            clickSubscription.unsubscribe();
            clickSubscription = null;
        }

        if (fragment != null) {
            fragment = null;
        }
    }
}
