package com.example.foodreview;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReviewRecyclerViewAdapter mAdapter;
    private ArrayList<Review> mReviewList;

    private String username;
    String nickname;
    private DatabaseManager dbms;
    private TextView ownReviews;
    UniversityManager universityManager;
    FrameLayout frame;
    int reviewedFoodId;
    Review currentReview;
    TextView reviewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        dbms = DatabaseManager.getInstance(this);
        universityManager = UniversityManager.getInstance();


        username = getIntent().getStringExtra("username");
        nickname = dbms.getOwnUser(username).getNickname();

        TextView usernameText = findViewById(R.id.profileUsernameText);
        usernameText.setText(nickname);

        mReviewList = new ArrayList<>();

        reviewText = findViewById(R.id.profileReviewText);



        dbms = DatabaseManager.getInstance(this);
        if (dbms.isAdmin(username)) {
            mReviewList = dbms.getAllReviews();
            reviewText.setText(getResources().getString(R.string.review_reviewadmintext));
        }
        else {
            mReviewList = dbms.getReviewsForUser(username);
            reviewText.setText(getResources().getString(R.string.review_reviewtext));
        }

        ownReviews = findViewById(R.id.ownReviews);
        String reviews = " " + mReviewList.size();
        ownReviews.setText(reviews);

        Toolbar toolbar = findViewById(R.id.toolbarreview);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.review_activitytitle));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        buildRecyclerView();


    }
    public void deleteItem(int position, Review review) {
        mReviewList.remove(position);
        mAdapter.notifyItemRemoved(position);
        dbms.deleteReview(review);
        String reviews = " " + mReviewList.size();
        ownReviews.setText(reviews);
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.review_recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ReviewRecyclerViewAdapter(mReviewList, dbms.isAdmin(username));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ReviewRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onEditClick(int position) {
                frame = findViewById(R.id.editReviewFragmentWindow);
                frame.setVisibility(View.VISIBLE);
                ReviewEditReviewFragment reviewEditReviewFragment = new ReviewEditReviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("reviewString", mReviewList.get(position).getReview());
                reviewEditReviewFragment.setArguments(bundle);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.editReviewFragmentWindow, reviewEditReviewFragment);
                transaction.commit();
                reviewedFoodId = mReviewList.get(position).getFoodId();
                currentReview = mReviewList.get(position);
            }

            @Override
            public void onDeleteClick(int position) {
                Review review = mReviewList.get(position);
                deleteItem(position, review);
            }
        });
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    private void closeActivity() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    public void cancelEdit(View view) {
        frame.setVisibility(View.INVISIBLE);
    }

    public void saveEdit(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        ReviewEditReviewFragment revieweditReviewFragment = (ReviewEditReviewFragment) manager.findFragmentById(R.id.editReviewFragmentWindow);
        assert revieweditReviewFragment != null;
        String newReviewString = revieweditReviewFragment.getReviewString();
        float newReviewGrade = revieweditReviewFragment.getReviewGrade();
        String newReviewFood = revieweditReviewFragment.getReviewFoodName();


        if (!newReviewString.equals("")) {
            frame.setVisibility(View.INVISIBLE);
            transaction.detach(revieweditReviewFragment);
            transaction.commit();
            Toast.makeText(this, getResources().getString(R.string.main_foodreviewed) + " " + newReviewFood, Toast.LENGTH_SHORT).show();
            dbms.modifyReviewData(currentReview, newReviewString, newReviewGrade);

            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, getResources().getText(R.string.review_writeafewwords), Toast.LENGTH_SHORT).show();
        }

        mReviewList.clear();
        if (dbms.isAdmin(username)) {
            mReviewList.addAll(dbms.getAllReviews());
        }
        else {
            mReviewList.addAll(dbms.getReviewsForUser(username));
        }
        mAdapter.notifyDataSetChanged();

    }
}
