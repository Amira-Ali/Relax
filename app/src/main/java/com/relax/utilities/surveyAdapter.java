package com.relax.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.relax.R;

import java.util.ArrayList;

public class surveyAdapter extends ArrayAdapter<Option> {
    private final ArrayList<dataBean> dataBeans;
    public RadioButton Never, AlmostNever, SomeOfTime, MostOfTime, AlmostAlways;
    Context context;
    int layoutResourceId;
    public Option[] data;

    public surveyAdapter(Context context, int layoutResourceId, Option[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        dataBeans = new ArrayList<>();
        for (Option s : data) {
            dataBeans.add(new dataBean(s));
        }
    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent) {

        final Holder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.list_items, parent, false);

            Never = row.findViewById(R.id.Never);
            AlmostNever = row.findViewById(R.id.Almost_never);
            SomeOfTime = row.findViewById(R.id.Some_of_the_time);
            MostOfTime = row.findViewById(R.id.Most_of_the_time);
            AlmostAlways = row.findViewById(R.id.Almost_always);

            //Set tag with row holder class
            holder = new Holder(row);
            holder.question = row.findViewById(R.id.question);
            holder.rg = row.findViewById(R.id.RadioG);

            holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if (checkedId != -1) {
                        Integer pos = (Integer) group.getTag();
                        data[pos].selectedId = checkedId;
                        dataBean dataBean = dataBeans.get(pos);

                        switch (checkedId) {
                            case R.id.Never:
                                dataBean.current = com.relax.utilities.dataBean.OPTION_1;
                                break;
                            case R.id.Almost_never:
                                dataBean.current = com.relax.utilities.dataBean.OPTION_2;
                                break;
                            case R.id.Some_of_the_time:
                                dataBean.current = com.relax.utilities.dataBean.OPTION_3;
                                break;
                            case R.id.Most_of_the_time:
                                dataBean.current = com.relax.utilities.dataBean.OPTION_4;
                                break;
                            case R.id.Almost_always:
                                dataBean.current = com.relax.utilities.dataBean.OPTION_5;
                                break;
                            default:
                                dataBean.current = com.relax.utilities.dataBean.NONE;
                        }
                    }
                }
            });
            row.setTag(holder);
        } else {
            holder = (Holder) row.getTag();
        }

        holder.question.setText(data[position].Question);
        holder.rg.setTag(position);
        if (dataBeans.get(position).current != dataBean.NONE) {
            RadioButton radioButton = (RadioButton) holder.rg.getChildAt(dataBeans.get(position).current);
            radioButton.setChecked(true);
        } else {
            holder.rg.clearCheck();
        }
        return row;
    }

    //get all items attached to adapter
    public Option[] getItems() {
        return data;
    }

}