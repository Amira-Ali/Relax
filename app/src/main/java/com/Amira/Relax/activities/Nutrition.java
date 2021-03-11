package com.Amira.Relax.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.Amira.Relax.utilities.FlowTextHelper;
import com.Amira.Relax.R;

public class Nutrition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = findViewById(R.id.btn_home);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Nutrition.this, Home.class);
                startActivity(intent);
            }
        });

        AppCompatImageView oatamel = findViewById(R.id.oatamelImg);
        TextView oatameltxtview =  findViewById(R.id.OatamelText);
        String oatamelstring = getString(R.string.OtamelString);

        Display OatamelDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(oatamelstring, oatamel, oatameltxtview, OatamelDisplay,4);

        AppCompatImageView carbs = findViewById(R.id.carbsImg);
        TextView carbstxtview = findViewById(R.id.ComplexCarbsText);
        String carbsstring = getString(R.string.ComplexCarbsString);

        Display Carbsdisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(carbsstring, carbs, carbstxtview, Carbsdisplay,4);

        AppCompatImageView Simplecarbs = findViewById(R.id.simplecarbsimg);
        TextView simplecarbstxtview = findViewById(R.id.SimpleCarbsText);
        String simplecarbsstring = getString(R.string.SimpleCarbsString);

        Display SimpleCarbsdisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(simplecarbsstring, Simplecarbs, simplecarbstxtview, SimpleCarbsdisplay,4);

        AppCompatImageView OrangeImg = findViewById(R.id.OrangesImg);
        TextView OrangeTxtview = findViewById(R.id.OrangeText);
        String OrangeString = getString(R.string.OrangeString);

        Display OrangeDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(OrangeString, OrangeImg, OrangeTxtview, OrangeDisplay,4);

        AppCompatImageView SpinachImg = findViewById(R.id.SpinachImg);
        TextView SpinachTxtview = findViewById(R.id.SpinachText);
        String SpinachString = getString(R.string.SpinachString);

        Display SpinachDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(SpinachString, SpinachImg, SpinachTxtview, SpinachDisplay,4);

        AppCompatImageView FishImg = findViewById(R.id.FattyFishImg);
        TextView FishTxtview = findViewById(R.id.FattyFishText);
        String FishString = getString(R.string.FattyFishString);

        Display FishDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(FishString, FishImg, FishTxtview, FishDisplay,4);

        AppCompatImageView TeaImg = findViewById(R.id.BlackTeaImg);
        TextView TeaTxtview =  findViewById(R.id.BlackTeaText);
        String TeaString = getString(R.string.BlackTeaString);

        Display TeaDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(TeaString, TeaImg, TeaTxtview, TeaDisplay,4);

        AppCompatImageView PistachiosImg = findViewById(R.id.PistachiosImg);
        TextView PistachiosTxtview = findViewById(R.id.PistachiosText);
        String PistachiosString = getString(R.string.PistachioString);

        Display PistachiosDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(PistachiosString, PistachiosImg, PistachiosTxtview, PistachiosDisplay,4);

        AppCompatImageView AvocadosImg = findViewById(R.id.AvocadosImg);
        TextView AvocadosTxtview = findViewById(R.id.AvocadoText);
        String AvocadosString = getString(R.string.AvocadoString);

        Display AvocadosDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(AvocadosString, AvocadosImg, AvocadosTxtview, AvocadosDisplay,4);

        AppCompatImageView AlmondsImg = findViewById(R.id.AlmondsImg);
        TextView AlmondsTxtview = findViewById(R.id.AlmondsText);
        String AlmondsString = getString(R.string.AlmondString);

        Display AlmondsDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(AlmondsString, AlmondsImg, AlmondsTxtview, AlmondsDisplay,4);

        AppCompatImageView VeggieImg = findViewById(R.id.RawVeggiesImg);
        TextView VeggieTxtview = findViewById(R.id.RawVeggiesText);
        String VeggieString = getString(R.string.RawVeggyString);

        Display VeggieDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(VeggieString, VeggieImg, VeggieTxtview, VeggieDisplay,4);

        AppCompatImageView SnackImg = findViewById(R.id.BedtimeSnackImg);
        TextView SnackTxtview = findViewById(R.id.BedtimeSnackText);
        String SnackString = getString(R.string.BedtimeSnackString);

        Display SnackDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(SnackString, SnackImg, SnackTxtview, SnackDisplay,4);

        AppCompatImageView MilkImg = findViewById(R.id.MilkImg);
        TextView MilkTxtview = findViewById(R.id.MilkText);
        String MilkString = getString(R.string.MilkString);

        Display MilkDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(MilkString, MilkImg, MilkTxtview, MilkDisplay,4);

        AppCompatImageView HerbalImg = findViewById(R.id.StjhonImg);
        TextView HerbalTxtview = findViewById(R.id.StJhonText);
        String HerbalString = getString(R.string.StJhonString);

        Display HerbalDisplay = getWindowManager().getDefaultDisplay();
        FlowTextHelper.tryFlowText(HerbalString, HerbalImg, HerbalTxtview, HerbalDisplay,4);




    }
}