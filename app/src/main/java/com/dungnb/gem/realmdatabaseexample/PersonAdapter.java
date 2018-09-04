package com.dungnb.gem.realmdatabaseexample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
  private Context context;
  private RealmResults<Person> personRealmResults;
  private RealmChangeListener listener;

  public PersonAdapter(Context context, RealmResults<Person> personRealmResults) {
    this.context = context;
    this.personRealmResults = personRealmResults;
  }

  public PersonAdapter(Context context, RealmResults<Person> personRealmResults, boolean isAutomaticUpdate) {
    this.context = context;
    this.personRealmResults = personRealmResults;
    if (isAutomaticUpdate) {
      this.listener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
          notifyDataSetChanged();
        }
      };
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(this.context).inflate(R.layout.item, parent, false);
    ViewHolder viewHolder = new ViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = personRealmResults.get(position);
        holder.mPersonNameTv.setText(person.getName());
        holder.mDogNameTv.setText(person.getDogs().get(0).getName());
        holder.mDogAgeTv.setText(person.getDogs().get(0).getAge()+"");
  }

  @Override
  public int getItemCount() {
    if (personRealmResults == null)
      return 0;
    else return personRealmResults.size();
  }

  @Override
  public long getItemId(int position) {
    return super.getItemId(position);
  }

  @Override
  public int getItemViewType(int position) {
    return super.getItemViewType(position);
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mPersonNameTv, mDogNameTv, mDogAgeTv;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      mPersonNameTv = itemView.findViewById(R.id.item_person_name_tv);
      mDogNameTv = itemView.findViewById(R.id.item_dog_name_tv);
      mDogAgeTv = itemView.findViewById(R.id.item_dog_age_tv);
    }
  }
}
