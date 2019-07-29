package com.example.foodreview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private RecyclerView mRecyclerView;
    private AdminRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mFoodRecyclerView;
    private AdminFoodRecyclerViewAdapter mFoodAdapter;
    private RecyclerView.LayoutManager mFoodLayoutManager;
    private ArrayList<Restaurant> mRestaurantList;
    private ArrayList<Food> mFoodList;
    private University currentUniversity;
    private String uniName;
    private UniversityManager universityManager;
    private Restaurant currentRestaurant;
    private DatabaseManager dbms;
    private Context context;
    FragmentManager manager;

    private boolean dataChanged = false;

    private Spinner admin_unispinner;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Toolbar toolbar = findViewById(R.id.toolbaradmin);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.admin_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backAction();
            }
        });

        mRestaurantList = new ArrayList<>();
        mFoodList = new ArrayList<>();

        admin_unispinner = findViewById(R.id.admin_unispinner);

        universityManager = UniversityManager.getInstance();
        context = this;
        dbms = DatabaseManager.getInstance(context);
        dbms.updateUniversities();

        ArrayList<University> universityObjects = universityManager.getUniversities();
        for (int x = 0; x < universityObjects.size(); x++) {
            dbms.updateCascade(universityObjects.get(x));
        }

        ArrayList<String> uniNames;
        uniNames = universityManager.getUniNames();

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, uniNames);
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        admin_unispinner.setAdapter(adapterUni);
        admin_unispinner.setOnItemSelectedListener(this);

        currentUniversity = universityObjects.get(0);

        //Both recycler views are on onCreate and they are not created again after this. Food recycler view will be set to invisible
        //and its content is empty until user presses a restaurant from the first recycler view.
        createRestaurantList();
        buildRecyclerView();
        buildFoodRecyclerView();
        mFoodRecyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_admin_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.admin_action_users:
                Intent intent = new Intent(this, AdminUserManager.class);
                intent.putExtra("usernameUserManager", getIntent().getStringExtra("username"));
                startActivity(intent);
                break;
            case R.id.admin_action_newrestaurant:
                Toast.makeText(this, getResources().getText(R.string.newrestaurant), Toast.LENGTH_SHORT).show();
                Fragment adminNewRestaurantFragment = new AdminNewRestaurantFragment();
                frame = findViewById(R.id.adminEditFragmentWindow);
                manager = getSupportFragmentManager();
                FragmentTransaction transactionNewRestaurant = manager.beginTransaction();
                transactionNewRestaurant.replace(R.id.adminEditFragmentWindow, adminNewRestaurantFragment);
                transactionNewRestaurant.commit();
                frame.setVisibility(View.VISIBLE);
                break;
            case R.id.admin_action_newfood:
                Toast.makeText(this, getResources().getText(R.string.newrestaurant), Toast.LENGTH_SHORT).show();
                Fragment adminNewFoodFragment = new AdminNewFoodFragment();
                frame = findViewById(R.id.adminEditFragmentWindow);
                manager = getSupportFragmentManager();
                FragmentTransaction transactionNewFood = manager.beginTransaction();
                transactionNewFood.replace(R.id.adminEditFragmentWindow, adminNewFoodFragment);
                transactionNewFood.commit();
                frame.setVisibility(View.VISIBLE);
                break;
            case R.id.admin_action_savecsv:
                PrintToFile csvWriter = PrintToFile.getInstance();
                csvWriter.executePrint(this, universityManager.getUniversity(uniName));
                break;
            case R.id.admin_action_savejson:
                PrintToFile jsonWriter = PrintToFile.getInstance();
                jsonWriter.writeJSON(this);
                break;
        }
        return true;
    }

    public void removeItem(int position, Restaurant restaurant) {
        mRestaurantList.remove(position);
        mAdapter.notifyItemRemoved(position);
        dbms.deleteRestaurant(restaurant, currentUniversity);
        dataChanged = true;
    }

    public void removeFoodItem(int position, Food food) {
        mFoodList.remove(position);
        mFoodAdapter.notifyItemRemoved(position);
        dbms.deleteFood(food, currentRestaurant);
        dataChanged = true;
    }

    public void createRestaurantList() {
        //currentUniversity = universityManager.getUniversity("LUT-University");
        mRestaurantList.addAll(currentUniversity.getRestaurants());
    }

    //Same food list is used so that the food recycler view can be updated with mFoodAdapter.notifyDataSetChanged()
    //Because of this, no new adapters or recycler views need to be created
    public void createFoodList(Restaurant restaurant) {
        mFoodList.addAll(restaurant.getFoods());
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.adminRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AdminRecyclerViewAdapter(mRestaurantList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdminRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Get a new food list from the restaurant that user pressed. Set first recycler view to invisible and second to visible
                //Update the food recycler view adapter and change title to restaurant name
                mFoodList.clear();
                createFoodList(mRestaurantList.get(position));
                mFoodAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.INVISIBLE);
                mFoodRecyclerView.setVisibility(View.VISIBLE);
                Objects.requireNonNull(getSupportActionBar()).setTitle(mRestaurantList.get(position).getRestaurantName());
                currentRestaurant = mRestaurantList.get(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position, mRestaurantList.get(position));
            }

            @Override
            public void onEditClick(int position) {
                //currentRestaurant = mRestaurantList.get(position);
                String[] restaurantAddress = mRestaurantList.get(position).getRawRestaurantAddress();
                Bundle bundle = new Bundle();
                bundle.putString("restaurantName", mRestaurantList.get(position).getRestaurantName());
                bundle.putString("restaurantAddress", restaurantAddress[0]);
                bundle.putString("restaurantPC", restaurantAddress[1]);
                bundle.putString("restaurantCity", restaurantAddress[2]);
                bundle.putBoolean("restaurantEnabled", mRestaurantList.get(position).getIsEnabled());
                bundle.putInt("restaurantUni", admin_unispinner.getSelectedItemPosition());
                Fragment adminEditFragment = new AdminEditFragment();
                adminEditFragment.setArguments(bundle);
                frame = findViewById(R.id.adminEditFragmentWindow);
                manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.adminEditFragmentWindow, adminEditFragment);
                transaction.commit();
                frame.setVisibility(View.VISIBLE);
            }
        });
    }

    public void buildFoodRecyclerView() {
        mFoodRecyclerView = findViewById(R.id.adminFoodRecyclerView);
        mFoodRecyclerView.setHasFixedSize(true);
        mFoodLayoutManager = new LinearLayoutManager(this);
        mFoodAdapter = new AdminFoodRecyclerViewAdapter(mFoodList);

        mFoodRecyclerView.setLayoutManager(mFoodLayoutManager);
        mFoodRecyclerView.setAdapter(mFoodAdapter);


        mFoodAdapter.setOnItemClickListener(new AdminFoodRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onDeleteClick(int position) {
                removeFoodItem(position, mFoodList.get(position));
            }

            @Override
            public void onEditClick(int position) {
                Bundle bundle = new Bundle();
                String foodPrice = "" + mFoodList.get(position).getFoodPrice();
                bundle.putString("foodName", mFoodList.get(position).getFoodName());
                bundle.putString("foodPrice", foodPrice);
                bundle.putString("foodDate", mFoodList.get(position).getDate());
                bundle.putString("foodUni", admin_unispinner.getSelectedItem().toString());
                bundle.putInt("selectedFood", position);
                Fragment adminFoodEditFragment = new AdminFoodEditFragment();
                adminFoodEditFragment.setArguments(bundle);
                frame = findViewById(R.id.adminEditFragmentWindow);
                manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.adminEditFragmentWindow, adminFoodEditFragment);
                transaction.commit();
                frame.setVisibility(View.VISIBLE);
            }
        });
    }

    //Actually cancel click in fragments
    public void continueClick(View view) {
        Toast.makeText(this, getResources().getText(R.string.cancelled), Toast.LENGTH_SHORT).show();
        frame.setVisibility(View.INVISIBLE);
    }

    public void saveNewRestaurant(View view) {
        AdminNewRestaurantFragment adminNewRestaurantFragment = (AdminNewRestaurantFragment) manager.findFragmentById(R.id.adminEditFragmentWindow);
        String newRestaurantName = adminNewRestaurantFragment.getNewRestaurantName();
        String newRestaurantAddress = adminNewRestaurantFragment.getNewRestaurantAddress();
        String newRestaurantPC = adminNewRestaurantFragment.getNewRestaurantPC();
        String newRestaurantCity = adminNewRestaurantFragment.getNewRestaurantCity();
        String[] newRestaurantAddressArray = {newRestaurantAddress, newRestaurantPC, newRestaurantCity};
        String newRestaurantUni = adminNewRestaurantFragment.getNewRestaurantUni();
        int newRestUniId = universityManager.getUniversity(newRestaurantUni).getUniId();

        if (newRestaurantName.equals("") || newRestaurantAddress.equals("") || newRestaurantPC.equals("") || newRestaurantCity.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.admin_fillempty), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getText(R.string.newrestaurant) + newRestaurantName, Toast.LENGTH_SHORT).show();
            frame.setVisibility(View.INVISIBLE);
            dbms.setNewRestaurant(newRestaurantAddressArray, newRestaurantName, newRestUniId);

            ArrayList<University> universityObjects = universityManager.getUniversities();
            for (int x = 0; x < universityObjects.size(); x++) {
                dbms.updateCascade(universityObjects.get(x));
            }

            mRestaurantList.clear();
            createRestaurantList();
            mAdapter.notifyDataSetChanged();
            dataChanged = true;
        }


    }

    public void saveNewFood(View view) {
        AdminNewFoodFragment adminNewFoodFragment = (AdminNewFoodFragment) manager.findFragmentById(R.id.adminEditFragmentWindow);
        String newFoodName = adminNewFoodFragment.getNewFoodName();
        float newFoodPrice = adminNewFoodFragment.getNewFoodPrice();
        String newFoodDate = adminNewFoodFragment.getNewFoodDate();
        String newFoodUni = adminNewFoodFragment.getNewFoodUni();
        String newFoodRest = adminNewFoodFragment.getNewFoodRest();
        int newFoodRestId = universityManager.getUniversity(newFoodUni).getRestaurant(newFoodRest).getRestaurantId();
        String s = Float.toString(newFoodPrice);

        if (newFoodName.equals("") || newFoodDate.equals("") || s.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.admin_fillempty), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getText(R.string.newrestaurant) + newFoodName, Toast.LENGTH_SHORT).show();
            frame.setVisibility(View.INVISIBLE);
            dbms.setNewFood(newFoodName, newFoodPrice, newFoodRestId, newFoodDate);

            dbms.updateUniversities();
            ArrayList<University> universityObjects = universityManager.getUniversities();
            for (int x = 0; x < universityObjects.size(); x++) {
                dbms.updateCascade(universityObjects.get(x));
            }


            mRestaurantList.clear();
            mRestaurantList.addAll(universityManager.getUniversity(uniName).getRestaurants());
            mFoodList.clear();
            mFoodList.addAll(universityManager.getUniversity(uniName).getRestaurant(newFoodRest).getFoods());
            mFoodAdapter.notifyDataSetChanged();
            dataChanged = true;
        }

    }

    public void saveEditedRestaurant(View view) {
        AdminEditFragment adminEditFragment = (AdminEditFragment) manager.findFragmentById(R.id.adminEditFragmentWindow);
        String editRestaurantName = adminEditFragment.getEditRestaurantName();
        String editRestaurantAddress = adminEditFragment.getEditRestaurantAddress();
        String editRestaurantPC = adminEditFragment.getEditRestaurantPC();
        String editRestaurantCity = adminEditFragment.getEditRestaurantCity();
        String[] newRestaurantAddressArray = {editRestaurantAddress, editRestaurantPC, editRestaurantCity};
        String editRestaurantUni = adminEditFragment.getEditRestaurantUni();
        boolean editRestaurantIsEnabled = adminEditFragment.getEditRestaurantIsEnabled();
        int editRestUniId = universityManager.getUniversity(editRestaurantUni).getUniId();
        String editRestaurantOldName = adminEditFragment.getEditRestaurantOldName();

        if (editRestaurantName.equals("") || editRestaurantAddress.equals("") || editRestaurantPC.equals("") || editRestaurantCity.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.admin_fillempty), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getText(R.string.editrestaurant) + editRestaurantName, Toast.LENGTH_SHORT).show();
            frame.setVisibility(View.INVISIBLE);
            dbms.modifyRestaurantData( universityManager.getUniversity(editRestaurantUni).getRestaurant(editRestaurantOldName), newRestaurantAddressArray, editRestaurantName, editRestUniId, editRestaurantIsEnabled);

            ArrayList<University> universityObjects = universityManager.getUniversities();
            for (int x = 0; x < universityObjects.size(); x++) {
                dbms.updateCascade(universityObjects.get(x));
            }

            mRestaurantList.clear();
            createRestaurantList();
            mAdapter.notifyDataSetChanged();
            dataChanged = true;
        }

    }

    public void saveEditedFood(View view) {
        AdminFoodEditFragment adminEditFragment = (AdminFoodEditFragment) manager.findFragmentById(R.id.adminEditFragmentWindow);
        String editFoodName = adminEditFragment.getEditFoodName();
        float editFoodPrice = adminEditFragment.getEditFoodPrice();
        String editFoodDate = adminEditFragment.getEditFoodDate();
        String editFoodUni = adminEditFragment.getFoodUni();
        String s = Float.toString(editFoodPrice);
        int k = adminEditFragment.getSelectedFood();

        if (editFoodName.equals("") || editFoodDate.equals("") || s.equals("")) {
            Toast.makeText(this, getResources().getText(R.string.admin_fillempty), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getText(R.string.editfood) + editFoodName, Toast.LENGTH_SHORT).show();
            frame.setVisibility(View.INVISIBLE);

            dbms.modifyFoodData(universityManager.getUniversity(editFoodUni).getRestaurant(currentRestaurant.getRestaurantName()).getFoods().get(k), editFoodName, editFoodPrice, editFoodDate);

            ArrayList<University> universityObjects = universityManager.getUniversities();
            for (int x = 0; x < universityObjects.size(); x++) {
                dbms.updateCascade(universityObjects.get(x));
            }

            mRestaurantList.clear();
            mRestaurantList.addAll(universityManager.getUniversity(uniName).getRestaurants());
            mFoodList.clear();
            createFoodList(universityManager.getUniversity(uniName).getRestaurant(currentRestaurant.getRestaurantName()));
            mFoodAdapter.notifyDataSetChanged();
            dataChanged = true;
        }
    }



    @Override
    public void onBackPressed() {
        backAction();
    }


    //If user clicks back arrow or back button following actions will happen. If there is food recycler view open,
    //that will close and if user is in the admin activity main page, admin activity will close
    private void backAction() {
        if (mFoodRecyclerView.getVisibility() == View.VISIBLE) {
            mFoodRecyclerView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.admin_title));
        }
        else {
            Intent intent = new Intent();
            if (dataChanged) {
                setResult(RESULT_OK, intent);
            }
            else {
                setResult(RESULT_CANCELED, intent);
            }
            finish();
        }
    }


    //University spinner listeners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mRestaurantList.clear();
        uniName = parent.getItemAtPosition(position).toString();
        currentUniversity = universityManager.getUniversity(uniName);
        mRestaurantList.clear();
        createRestaurantList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

