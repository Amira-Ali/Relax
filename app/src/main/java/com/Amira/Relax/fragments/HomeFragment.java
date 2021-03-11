package com.Amira.Relax.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;

import com.Amira.Relax.utilities.DatabaseHelper;
import com.Amira.Relax.utilities.GlobalVariables;
import com.Amira.Relax.R;
import com.Amira.Relax.activities.Breathing;
import com.Amira.Relax.activities.ChatPage;
import com.Amira.Relax.activities.CopingWithChange;
import com.Amira.Relax.activities.Journaling;
import com.Amira.Relax.activities.Nutrition;
import com.Amira.Relax.activities.Physical;
import com.Amira.Relax.activities.SelfEsteem;
import com.Amira.Relax.activities.Sleeping;
import com.Amira.Relax.activities.Spiritual;
import com.Amira.Relax.activities.Sport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {
    View root = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        int[] data = databaseHelper.GetFlagsFromDB(GlobalVariables.username);
        AssignSurveyResultsToFlags(data);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView TextGreet = root.findViewById(R.id.greeting);
        TextGreet.setText(GetGreeting());

        ViewWeaknessPointsOrSurveyRow(root);

        LinearLayoutCompat layoutCompat = root.findViewById(R.id.GoToChatBtn);
        layoutCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChatPage = new Intent(getActivity(), ChatPage.class);
                startActivity(ChatPage);
            }
        });

        TextView TakeSurvey = root.findViewById(R.id.TakeSurvey);
        TakeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Physical.class);//physical because it's
                // the first page in survey, from there you click next til you get to last page
                // of survey
                startActivity(intent);
            }
        });
        return root;
    }

    private String GetGreeting() {
        String result = null;
        try {
            Calendar c = Calendar.getInstance();
            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

            if (timeOfDay >= 0 && timeOfDay < 12) {
                result = "Good Morning," + " " + GlobalVariables.username + "!";
            } else if (timeOfDay >= 12 && timeOfDay < 16) {
                result = "Good Afternoon," + " " + GlobalVariables.username + "!";
            } else if (timeOfDay >= 16 && timeOfDay < 21) {
                result = "Good Evening," + " " + GlobalVariables.username + "!";
            } else if (timeOfDay >= 21 && timeOfDay < 24) {
                result = "Good Night," + " " + GlobalVariables.username + "!";
            } else {
                result = "Hello," + " " + GlobalVariables.username + "!";
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    private void AssignSurveyResultsToFlags(int[] data) {
        if (data != null) {
            //Physical_Flag
            if (data[0] == 0) {
                GlobalVariables.Physical_Flag = "N";
            } else {
                if (data[0] > 0 && data[0] < 10) {
                    GlobalVariables.Physical_Flag = "L";
                } else {
                    if (data[0] >= 10 && data[0] < 15) {
                        GlobalVariables.Physical_Flag = "M";
                    } else {
                        if (data[0] >= 15) {
                            GlobalVariables.Physical_Flag = "H";
                        }
                    }
                }
            }

            //Sleep_Flag
            if (data[1] == 0) {
                GlobalVariables.Sleep_Flag = "N";
            } else {
                if (data[1] > 0 && data[1] < 8) {
                    GlobalVariables.Sleep_Flag = "L";
                } else {
                    if (data[1] >= 8 && data[1] < 12) {
                        GlobalVariables.Sleep_Flag = "M";
                    } else {
                        if (data[1] >= 12) {
                            GlobalVariables.Sleep_Flag = "H";
                        }
                    }
                }
            }

            //Behavior_Flag
            if (data[2] == 0) {
                GlobalVariables.Behavior_Flag = "N";
            } else {
                if (data[2] > 0 && data[2] < 17) {
                    GlobalVariables.Behavior_Flag = "L";
                } else {
                    if (data[2] >= 17 && data[2] < 25) {
                        GlobalVariables.Behavior_Flag = "M";
                    } else {
                        if (data[2] >= 25) {
                            GlobalVariables.Behavior_Flag = "H";
                        }
                    }
                }
            }

            //Emotional_Flag
            if (data[3] == 0) {
                GlobalVariables.Emotional_Flag = "N";
            } else {
                if (data[3] > 0 && data[3] < 17) {
                    GlobalVariables.Emotional_Flag = "L";
                } else {
                    if (data[3] >= 17 && data[3] < 25) {
                        GlobalVariables.Emotional_Flag = "M";
                    } else {
                        if (data[3] >= 25) {
                            GlobalVariables.Emotional_Flag = "H";
                        }
                    }
                }
            }
        }
    }

    private void ViewWeaknessPointsOrSurveyRow(View root) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        boolean IsSurvey = databaseHelper.IsSurveyTaken(GlobalVariables.username);
        TableRow SurveyRow = root.findViewById(R.id.SurveyRow);
        TableRow WeaknessPoints = root.findViewById(R.id.WeaknessPoints);
        if (IsSurvey) {
            if ((!"N".equals(GlobalVariables.Physical_Flag)) || (!"N".equals(GlobalVariables.Sleep_Flag))
                    || (!"N".equals(GlobalVariables.Behavior_Flag)) || (!"N".equals(GlobalVariables.Emotional_Flag))) {
                SurveyRow.setVisibility(View.GONE);
                WeaknessPoints.setVisibility(View.VISIBLE);
            } else {
                WeaknessPoints.setVisibility(View.GONE);
                SurveyRow.setVisibility(View.VISIBLE);
            }

            if (!"N".equals(GlobalVariables.Physical_Flag)) {
                AppCompatImageView v = root.findViewById(R.id.physical_logo);
                v.setVisibility(View.VISIBLE);
            }

            if (!"N".equals(GlobalVariables.Sleep_Flag)) {
                AppCompatImageView v = root.findViewById(R.id.sleep_logo);
                v.setVisibility(View.VISIBLE);
            }

            if (!"N".equals(GlobalVariables.Behavior_Flag)) {
                AppCompatImageView v = root.findViewById(R.id.behavior_logo);
                v.setVisibility(View.VISIBLE);
            }

            if (!"N".equals(GlobalVariables.Emotional_Flag)) {
                AppCompatImageView v = root.findViewById(R.id.emotion_logo);
                v.setVisibility(View.VISIBLE);
            }

            //Will return list<string>: Nutrition, Exercise, Sleep and other recommendations titles
            List<String> RecommendationTitles = ConstructRecommendationTitlesBasedOnFlags();

            if (RecommendationTitles.size() == 0) {//took the survey and he's fine
                TextView NoRecommend = root.findViewById(R.id.NoRecommend);
                NoRecommend.setText(R.string.NoRecommend2);
            } else {
                //delete the No Recommend Box
                TableLayout layout = root.findViewById(R.id.RecommendationTable);
                TableRow row = layout.findViewById(R.id.NoRecommendRow);
                TextView txt = root.findViewById(R.id.NoRecommend);
                if (!txt.getText().equals(getString(R.string.help))) {
                    layout.removeView(row);
                }
                CreateRecommendations(layout, RecommendationTitles);
            }
        }
    }

    private List<String> ConstructRecommendationTitlesBasedOnFlags() {
        List<String> FlagList = new ArrayList<>();
        String PhysicalFlag = GlobalVariables.Physical_Flag;
        String SleepFlag = GlobalVariables.Sleep_Flag;
        String EmotionalFlag = GlobalVariables.Emotional_Flag;
        String BehavioralFlag = GlobalVariables.Behavior_Flag;

        switch (PhysicalFlag) {
            case "L":
            case "M":
                FlagList.add("Nutrition");
                FlagList.add("Exercises");
                break;

            case "H":
                CreateSos();
                FlagList.add("Nutrition");
                FlagList.add("Exercises");
                break;
        }

        switch (SleepFlag) {
            case "L":
            case "M":
                FlagList.add("Breathing");
                FlagList.add("Sleeping");
                break;
            case "H":
                CreateSos();
                FlagList.add("Breathing");
                FlagList.add("Sleeping");
                break;
        }

        switch (EmotionalFlag) {
            case "L":
            case "M":
                FlagList.add("Spiritual");
                FlagList.add("Journaling");
                break;
            case "H":
                CreateSos();
                FlagList.add("Spiritual");
                FlagList.add("Journaling");
                break;
        }

        switch (BehavioralFlag) {
            case "L":
            case "M":
                FlagList.add("Coping with changes");
                FlagList.add("Self-esteem");
                break;
            case "H":
                CreateSos();
                FlagList.add("Coping with changes");
                FlagList.add("Self-esteem");
                break;
        }

        return FlagList;
    }

    private void CreateRecommendations(TableLayout layout, List<String> FlagList) {
        //count how many elements does the FlagList have and divide it by 2
        float n = (float) FlagList.size() / 2;
        int LineCount;
        if (((int) n) == n) {
            LineCount = (int) n;
        } else {
            LineCount = (int) n + 1;
        }

        TypedArray array = getResources().obtainTypedArray(R.array.random_imgs);
        TableLayout.LayoutParams RowParams =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams RelativeParams = new TableRow.LayoutParams(0,
                200, 1f);

        TableRow.LayoutParams ImageViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);

        TableRow.LayoutParams TextViewParams =
                new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);

        int k = 0;
        int y = 0;
        for (int i = 0; i < LineCount; i++) {
            //add TableRow
            TableRow row = new TableRow(requireContext());
            row.setLayoutParams(RowParams);

            for (int j = 0; j < 2; j++) {
                if (k < FlagList.size()) {
                    //add RelativeLayout to the ROW
                    AppCompatTextView textView = new AppCompatTextView(requireContext());
                    textView.setLayoutParams(TextViewParams);
                    textView.setTextAppearance(requireContext(), R.style.textview);
                    textView.setPadding(25, 75, 2, 2);
                    textView.setTextColor(Color.WHITE);
                    textView.setText(FlagList.get(k));
                    k++;
                    final String txt = (String) textView.getText();
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            switch (txt) {
                                case "Journaling":
                                    Intent JournalIntent = new Intent(getActivity(),
                                            Journaling.class);
                                    startActivity(JournalIntent);
                                    break;

                                case "Nutrition":
                                    Intent NutritionIntent = new Intent(getActivity(),
                                            Nutrition.class);
                                    startActivity(NutritionIntent);
                                    break;

                                case "Sleeping":
                                    Intent SleepIntent = new Intent(getActivity(),
                                            Sleeping.class);
                                    startActivity(SleepIntent);
                                    break;

                                case "Coping with changes":
                                    Intent ChangeIntent = new Intent(getActivity(),
                                            CopingWithChange.class);
                                    startActivity(ChangeIntent);
                                    break;

                                case "Self-esteem":
                                    Intent SelfSteam = new Intent(getActivity(), SelfEsteem.class);
                                    startActivity(SelfSteam);
                                    break;

                                case "Spiritual":
                                    Intent Spiritual = new Intent(getActivity(),
                                            Spiritual.class);
                                    startActivity(Spiritual);
                                    break;

                                case "Exercises":
                                    Intent Sport = new Intent(getActivity(), Sport.class);
                                    startActivity(Sport);
                                    break;

                                case "Breathing":
                                    Intent Breathing = new Intent(getActivity(), Breathing.class);
                                    startActivity(Breathing);
                                    break;

                                default:
                                    break;
                            }

                        }
                    });

                    AppCompatImageView image = new AppCompatImageView(requireContext());
                    image.setLayoutParams(ImageViewParams);
                    image.setBackgroundResource(array.getResourceId(y, 0));
                    if (y < 3) {
                        y++;
                    } else {
                        y = 0;
                    }

                    RelativeLayout relativeLayout = new RelativeLayout(requireContext());
                    RelativeParams.setMargins(2, 4, 2, 4);
                    relativeLayout.setLayoutParams(RelativeParams);

                    relativeLayout.addView(image);
                    relativeLayout.addView(textView);

                    row.addView(relativeLayout, RelativeParams);

                } else {
                    break;
                }
            }
            layout.addView(row, RowParams);
        }
        array.recycle();
    }

    private void CreateSos() {
        TableRow row = root.findViewById(R.id.NoRecommendRow);
        if (row != null) {
            Drawable Red = ContextCompat.getDrawable(getContext(), R.drawable.gradient_dark_red);
            row.setBackground(Red);
            TextView txt = root.findViewById(R.id.NoRecommend);
            txt.setHeight(150);
            txt.setGravity(Gravity.CENTER);
            txt.setTextSize(16);
            txt.setText(R.string.help);

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCall();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                onCall();
            } else {
                Log.d("TAG", "Call Permission Not Granted");
            }
        }
    }

    public void onCall(){
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:0914981270")));
        }
    }
}