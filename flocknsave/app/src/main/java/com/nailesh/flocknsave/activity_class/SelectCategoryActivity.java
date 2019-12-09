package com.nailesh.flocknsave.activity_class;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.nailesh.flocknsave.R;

public class SelectCategoryActivity extends AppCompatActivity {

    private String personType;

    private FirebaseAuth mAuth;

    private CardView agriculture_contracting, animal_health, construction, dairy_shed_supplies,
            electricity, fencing, fertiliser, finance, fuel, safety_and_clothing, stockfeed,
            vehicles, water_and_drainage, weed_and_pest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        setup();

    }

    private void setup() {

        personType = getIntent().getStringExtra("personType");

        mAuth = FirebaseAuth.getInstance();

        agriculture_contracting = findViewById(R.id.category_agriculture_contracting);
        animal_health = findViewById(R.id.category_animal_health);
        construction = findViewById(R.id.category_construction);
        dairy_shed_supplies = findViewById(R.id.category_dairy_shed_supplies);
        electricity = findViewById(R.id.category_electricity);
        fencing = findViewById(R.id.category_fencing);
        fertiliser = findViewById(R.id.category_fertiliser);
        finance = findViewById(R.id.category_finance);
        fuel = findViewById(R.id.category_fuel);
        safety_and_clothing = findViewById(R.id.category_safety_and_clothing);
        stockfeed = findViewById(R.id.category_stockfeed);
        vehicles = findViewById(R.id.category_vehicles);
        water_and_drainage = findViewById(R.id.category_water_and_drainage);
        weed_and_pest = findViewById(R.id.category_weed_and_pest);

        agriculture_contracting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Agriculture Contracting"); }});
        animal_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Animal Health");
            }
        });
        construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Construction");
            }
        });
        dairy_shed_supplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Dairy Shed Supplies");
            }
        });
        electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProducts("Electricity");
            }
        });
        fencing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProducts("Fencing");
            }
        });
        fertiliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProducts("Fertiliser");
            }
        });
        finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProducts("Finance");
            }
        });
        fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Fuel, Oils and Lubricants");
            }
        });
        safety_and_clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Safety and Clothing");
            }
        });
        stockfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProducts("Stockfeed");
            }
        });
        vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProducts("Vehicles");
            }
        });
        water_and_drainage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Water and Drainage");
            }
        });
        weed_and_pest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { displayProducts("Weed and Pest");
            }
        });
    }

    private void displayProducts(String pCategory) {

        Intent intent = new Intent(getApplicationContext(), SearchActivity.class)
                .putExtra("category", pCategory)
                .putExtra("personType",personType)
                .putExtra("updateProduct",false);
        startActivity(intent);
        this.finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu, menu);

        // change toolbar button if user is logged in
        if (!personType.equals("User")) {
            menu.findItem(R.id.nav_menu_login).setVisible(false);
            menu.findItem(R.id.nav_menu_logout).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.nav_menu_logout:
                mAuth.signOut();
                Intent logout = new Intent(this, LoginActivity.class);
                startActivity(logout);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
