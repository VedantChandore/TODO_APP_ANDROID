package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.TodoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHelper;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {
    private List<TodoModel> list;
    private MainActivity mainActivity;
    private DatabaseHelper myDB;

    public TodoAdapter(DatabaseHelper myDB,MainActivity mainActivity){
        this.myDB=myDB;
        this.mainActivity=mainActivity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TodoModel item= list.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDB.updateStatus(item.getStatus(),1);
                }
                else{
                    myDB.updateStatus(item.getStatus(),0);
                }
            }
        });
    }
    public boolean toBoolean(int num){
        return num!=0;
    }
    public Context getContext(){
        return mainActivity;
    }
    public void setTasks(List<TodoModel> list){
        this.list=list;
        notifyDataSetChanged();
    }
    public void deleteTask(int position){
        TodoModel item=list.get(position);
        myDB.deleteTask(item.getId());
        list.remove(position);
        notifyItemRemoved(position);
    }
    public void editItem(int position){
        TodoModel item= list.get(position);

        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());

        AddNewTask task=new AddNewTask();
        task.setArguments(bundle);
        task.show(mainActivity.getSupportFragmentManager(), task.getTag() );
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.m_checkbox);
        }
    }
}
