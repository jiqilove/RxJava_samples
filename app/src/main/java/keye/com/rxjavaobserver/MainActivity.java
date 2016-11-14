package keye.com.rxjavaobserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import keye.com.rxjavaobserver.bean.Author;
import keye.com.rxjavaobserver.bean.DataUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_RxJava = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //��ʼ��Logger
        Logger.init(TAG_RxJava).logLevel(LogLevel.FULL);

//        test01();
//        test02();
//        test03();
//        test04();
        test07();
    }

    /**
     * ʹ�� Observable.create() ����������һ�� Observable ��Ϊ�������¼���������
     */
    public void test01() {

        //����Observable
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello World!");
                subscriber.onNext("Hello JiKeXueYuan");
                subscriber.onCompleted();
            }
        });

        //����Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("RxJava -->> onCompleted()!");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.d("RxJava -->> onNext()" + s);
            }
        };

        //����
        observable.subscribe(observer);

    }

    /**
     * Observable����from()
     */
    public void test02() {

        String[] array = new String[]{"Hello World!", "Hello KeYe!", "Hello jikexueyuan"};
        List<String> list = new ArrayList<>();
        //����Observable
        Observable<String> observable = Observable.from(array);

        //����Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("RxJava -->> onCompleted()!");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                list.add(s);
                Logger.d("RxJava -->> onNext()" + s);
            }
        };

        observable.subscribe(observer);

    }

    /**
     * Observable������just()
     */
    public void test03() {

        //����Observable
        Observable<String> observable = Observable.just("Hello", "RxJava", "jikexuey");

        //����Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.d("RxJava -->> onCompleted()!");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Logger.d("RxJava -->> onNext()" + s);
            }
        };

        observable.subscribe(observer);

    }


    /**
     * subscribe()�������ص�
     */
    public void test04() {
        //����Observable
        Observable<String> observable = Observable.just("Hello", "RxJava", "jikexuey");

        //2:�������ص��ӿ�
        Action1 onNextAction = new Action1() {
            @Override
            public void call(Object o) {
                String str = (String) o;
                Logger.d("RxJava:onNextAction:call(Object o):" + str);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.d("RxJava:onErrorAction:call(Throwable throwable):" + throwable.getMessage());
            }
        };


        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Logger.d("RxJava:onCompletedAction:call()");
            }
        };

        observable.subscribe(onNextAction);

    }

    public void textX() {
        String[] array = new String[]{"Hello", "JiKeXueYuan", "RxJava", "World"};
        List<String> list = new ArrayList<>();

        //RxJava
        Observable.from(array).subscribe((s) ->
                list.add(s)
        );

        //Java
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

    }



    /**
     * Map()�任������
     */
    public void text05() {
        Integer[] array = new Integer[]{1, 2, 3, 4};

        Observable.from(array)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer.toString();
                    }
                }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("RxJava", "Map()-->>>" + s);
            }
        });

    }

    /**
     * FlatMap()������
     */
    public void test06() {
        Observable.from(DataUtils.getData())
                .flatMap((Func1<Author, Observable<?>>) author -> {
                    Log.d("RxJava", author.name);
                    return Observable.from(author.Articles);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(article -> {
                    Log.d("RxJava-->>", article.toString());
                });
    }

    /**
     * Schedulers��API
     */
    public void test07() {
        Observable.from(DataUtils.getData2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("RxJava", String.valueOf(integer));
                    }
                });

    }


    //�����б�
    String[] array = new String[]{"����", "���", "ʯ��ׯ", "����"};

    //List����
    List<SceneryInfo> SceneryInfos = new ArrayList() {
    };

    //���о�����
    class SceneryInfo {
    }

    //ģ���������󣬷���SceneryInfo
    private SceneryInfo getSceneryInfo(String city) {

        return new SceneryInfo();
    }


    public void s() {
        Observable.from(array)
                .flatMap(new Func1<String, Observable<SceneryInfo>>() {
                    @Override
                    public Observable<SceneryInfo> call(String s) {
                        return Observable.just(getSceneryInfo(s));
                    }
                })
                .subscribeOn(Schedulers.io())//����������IO�߳�ִ��
                .observeOn(AndroidSchedulers.mainThread()).//����ui�����߳�ִ��
                subscribe(new Subscriber<SceneryInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(SceneryInfo weatherInfo) {
                //��Ӿ�������
                SceneryInfos.add(weatherInfo);
            }
        });
    }

}
