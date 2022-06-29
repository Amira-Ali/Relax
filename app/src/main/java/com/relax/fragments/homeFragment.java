package com.relax.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.relax.R;
import com.relax.activities.chatPage;
import com.relax.activities.recommendation_Breathing;
import com.relax.activities.recommendation_Coping;
import com.relax.activities.recommendation_Journaling;
import com.relax.activities.recommendation_Nutrition;
import com.relax.activities.recommendation_SelfEsteem;
import com.relax.activities.recommendation_Sleeping;
import com.relax.activities.recommendation_Spiritual;
import com.relax.activities.recommendation_Sport;
import com.relax.activities.surveyPhysical;
import com.relax.utilities.dbHelper;
import com.relax.utilities.globalVariables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class homeFragment extends Fragment {
    View root = null;
    String userName = globalVariables.userName;
    boolean IsSurvey = globalVariables.IsSurvey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariables.backURL = "Home";
        final dbHelper dbHelper = new dbHelper(getActivity());
        if (IsSurvey) {
            dbHelper.getUserFlags(globalVariables.userID);
        }
        dbHelper.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        TextView TextGreet = root.findViewById(R.id.greeting);
        TextGreet.setText(GetGreeting());

        ViewWeakPointsOrTakeSurvey(root);

        LinearLayoutCompat layoutCompat = root.findViewById(R.id.GoToChatBtn);
        layoutCompat.setOnClickListener(v -> {
            globalVariables.backURL = "Home";
            Intent ChatPage = new Intent(getActivity(), chatPage.class);
            startActivity(ChatPage);
        });

        TextView TakeSurvey = root.findViewById(R.id.TakeSurvey);
        TakeSurvey.setOnClickListener(v -> {
            globalVariables.backURL = "Home";
            Intent intent = new Intent(getActivity(), surveyPhysical.class);//physical because it's the first page in survey, from there you click next til you get to last page  of survey
            startActivity(intent);
        });
        return root;
    }

    private String GetGreeting() {
        String result = null;
        try {
            Calendar c = Calendar.getInstance();
            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

            if (timeOfDay < 12) {
                result = "Good Morning," + " " + userName + "!";
            } else if (timeOfDay < 16) {
                result = "Good Afternoon," + " " + userName + "!";
            } else {
                result = "Good Evening," + " " + userName + "!";
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    private void ViewWeakPointsOrTakeSurvey(View root) {

        TableRow SurveyRow = root.findViewById(R.id.SurveyRow);
        TableRow WeaknessPoints = root.findViewById(R.id.WeaknessPoints);

        if (IsSurvey) {
            SurveyRow.setVisibility(View.GONE);
            WeaknessPoints.setVisibility(View.VISIBLE);
        } else {
            WeaknessPoints.setVisibility(View.GONE);
            SurveyRow.setVisibility(View.VISIBLE);
        }

        TableLayout layout = root.findViewById(R.id.RecommendationTable);
        TableRow row = layout.findViewById(R.id.NoRecommendRow);
        TextView txt = root.findViewById(R.id.NoRecommend);
        if (!txt.getText().equals(getString(R.string.help))) {
            layout.removeView(row);
        }
        CreateRecommendations(layout);
    }

    private void CreateRecommendations(TableLayout layout) {

        if ("H".contains(globalVariables.physicalFlag) || "H".contains(globalVariables.sleepFlag) || "H".contains(globalVariables.emotionalFlag) || "H".contains(globalVariables.behaviorFlag)) {
            CreateSos();
        }

        List<String> RecommendTitle = new ArrayList<>();
        RecommendTitle.add("Nutrition");
        RecommendTitle.add("Exercises");
        RecommendTitle.add("Breathing");
        RecommendTitle.add("Sleeping");
        RecommendTitle.add("Spiritual");
        RecommendTitle.add("Journaling");
        RecommendTitle.add("Coping with changes");
        RecommendTitle.add("Self-esteem");

        //RecommendTitle.size divide it by 2
        int LineCount = 4;

        TypedArray array = getResources().obtainTypedArray(R.array.random_imgs);
        TableLayout.LayoutParams RowParams =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams RelativeParams = new TableRow.LayoutParams(0,
                200, 1f);

        TableRow.LayoutParams ImageViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT);

        TableRow.LayoutParams TextViewParams =
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT);

        int k = 0;
        int y = 0;
        for (int i = 0; i < LineCount; i++) {
            //add TableRow
            TableRow row = new TableRow(requireContext());
            row.setLayoutParams(RowParams);

            for (int j = 0; j < 2; j++) {
                if (k < RecommendTitle.size()) {
                    //add RelativeLayout to the ROW
                    AppCompatTextView textView = new AppCompatTextView(requireContext());
                    textView.setLayoutParams(TextViewParams);
                    textView.setTextAppearance(requireContext(), R.style.textView);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(Color.WHITE);
                    textView.setText(RecommendTitle.get(k));
                    k++;

                    final String txt = (String) textView.getText();

                    textView.setOnClickListener(arg0 -> {
                        Intent intent = null;
                        switch (txt) {
                            case "Journaling":
                                intent = new Intent(getActivity(), recommendation_Journaling.class);
                                break;

                            case "Nutrition":
                                intent = new Intent(getActivity(), recommendation_Nutrition.class);
                                break;

                            case "Sleeping":
                                intent = new Intent(getActivity(), recommendation_Sleeping.class);
                                break;

                            case "Coping with changes":
                                intent = new Intent(getActivity(), recommendation_Coping.class);
                                break;

                            case "Self-esteem":
                                intent = new Intent(getActivity(), recommendation_SelfEsteem.class);
                                break;

                            case "Spiritual":
                                intent = new Intent(getActivity(), recommendation_Spiritual.class);
                                break;

                            case "Exercises":
                                intent = new Intent(getActivity(), recommendation_Sport.class);
                                break;

                            case "Breathing":
                                intent = new Intent(getActivity(), recommendation_Breathing.class);
                                break;

                            default:
                                break;
                        }
                        globalVariables.backURL = "Home";
                        startActivity(intent);

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
            Drawable Red = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_dark_red);
            row.setBackground(Red);
            TextView txt = root.findViewById(R.id.NoRecommend);
            txt.setHeight(150);
            txt.setTextSize(16);
            txt.setText(R.string.help);

            txt.setOnClickListener(v -> onCall());
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    onCall();
                }
            });

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            globalVariables.backURL = "Home";
            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:0914981270")));
        } else {
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE);
        }
    }
}