package app.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import app.adapter.CardAdapter;
import app.model.Github;
import app.service.GithubService;
import app.service.ServiceFactory;
import com.example.githubdemo.app.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {

    GithubService service = ServiceFactory.createRetrofitService(GithubService.class, GithubService.SERVICE_ENDPOINT);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Set up Android CardView/RecycleView
         */
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CardAdapter mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);

        EditText searchField = (EditText) findViewById(R.id.search_field);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) mCardAdapter.clear();
                Observable.just(charSequence)
                        .debounce(300, TimeUnit.MILLISECONDS)
                        .filter(x -> x.length() > 1)
                        .map(x -> x.toString())
                        .subscribe(val -> {
                            service.searchRepository(val)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Github>() {
                                        @Override
                                        public void onCompleted() {}

                                        @Override
                                        public void onError(Throwable e) {
                                            Log.e("GithubDemo", e.getMessage() + e.fillInStackTrace());
                                        }

                                        @Override
                                        public void onNext(Github github) {
                                            mCardAdapter.clear();
                                            for (int i=0; i<github.getItems().size(); i++){
                                                mCardAdapter.addData(github.getItems().get(i));
                                            }
                                        }
                                    });
                        });
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

}
