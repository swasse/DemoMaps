package be.ehb.demomaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import be.ehb.demomaps.model.Hoofdstad;

public class HoofdstadDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoofdstad_detail);

        TextView tvstadsnaam = findViewById(R.id.tv_details_hoofdstad);

        Hoofdstad selectedCity = (Hoofdstad) getIntent().getSerializableExtra("stad");

        tvstadsnaam.setText(selectedCity.getCityName());
    }
}
