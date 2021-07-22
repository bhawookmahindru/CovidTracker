package com.example.bhawook54545434.tracker19;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhawook54545434.tracker19.api.ApiUtilities;
import com.example.bhawook54545434.tracker19.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView totalconfirm, totalactive, totalrecovered, totaldeath, totaltest;
    private TextView todayconfirmed, todayrecoverd, todaydeaths, date;
    private List<CountryData> list;
    private PieChart pieChart;
    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        if (getIntent().getStringExtra("country") != null) {
            country = getIntent().getStringExtra("country");
        }

        init();

        TextView cname = findViewById(R.id.country_name);
        cname.setText(country);

        cname.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CountryActivity.class));
        });

        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCountry().equals(country)) {
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int death = Integer.parseInt(list.get(i).getDeaths());

                        totalactive.setText(NumberFormat.getInstance().format(active));
                        totalconfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalrecovered.setText(NumberFormat.getInstance().format(recovered));
                        totaldeath.setText(NumberFormat.getInstance().format(death));

                        todaydeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        todayconfirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        todayrecoverd.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        totaltest.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel("Confirm", confirm, getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel("Active", active, getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel("Recovered", recovered, getResources().getColor(R.color.green_pie)));
                        pieChart.addPieSlice(new PieModel("Death", death, getResources().getColor(R.color.red_pie)));

                        pieChart.startAnimation();

                    }
                }

            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long millisecond = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        date.setText("Updated at : " + format.format(calendar.getTime()));

    }

    private void init() {
        totalconfirm = findViewById(R.id.total_confirmed);
        totalactive = findViewById(R.id.total_active);
        totalrecovered = findViewById(R.id.total_recovered);
        totaldeath = findViewById(R.id.total_death);
        totaltest = findViewById(R.id.toatl_tested);
        todayconfirmed = findViewById(R.id.today_confirm);
        todayrecoverd = findViewById(R.id.today_recovered);
        todaydeaths = findViewById(R.id.today_deaths);
        pieChart = findViewById(R.id.piechart);
        date = findViewById(R.id.date);
        totaltest = findViewById(R.id.toatl_tested);
    }

}