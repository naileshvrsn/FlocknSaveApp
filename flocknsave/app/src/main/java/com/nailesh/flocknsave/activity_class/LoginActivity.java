package com.nailesh.flocknsave.activity_class;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.nailesh.flocknsave.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.header_menu,menu);

        menu.findItem(R.id.nav_menu_login).setVisible(false);
        menu.findItem(R.id.nav_menu_register).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
