package com.dungnb.gem.realmdatabaseexample;

import android.app.Dialog;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RealmChangeListener<Realm> {

  private static final String TAG = "DungNB";
  private Realm mRealm;
  private RecyclerView recyclerView;
  private PersonAdapter personAdapter;
  private EditText mPersonNameEdt;
  private EditText mDogNameEdt;
  private EditText mDogAgeEdt;
  private Dialog dialog;
  private TextView mCancelBtn;
  private TextView mOkBtn;
  private FloatingActionButton mFabAdd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Realm.init(this);
    mRealm = Realm.getDefaultInstance();
    mRealm.addChangeListener(this);
    addControls();
    addEvents();
  }

  private void addControls() {
    recyclerView = findViewById(R.id.main_rcv);
    recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
    mFabAdd = findViewById(R.id.main_fab_add);
    RealmResults<Person> personRealmResults = mRealm.where(Person.class).findAll();
    personAdapter = new PersonAdapter(this, personRealmResults, true);
    recyclerView.setAdapter(personAdapter);
  }


  private void addEvents() {
    mFabAdd.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.main_fab_add:
        showDialogAdd();
        break;
      case R.id.dialog_add_btn_cancel:
        if (dialog != null)
          dialog.dismiss();
        break;
      case R.id.dialog_add_btn_ok:
        handleBtnOk();
        break;
      default:
        break;
    }
  }

  public void showDialogAdd() {
    dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    LayoutInflater inflater = getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_add, null);
    mPersonNameEdt = view.findViewById(R.id.dialog_add_edt_person_name);
    mDogNameEdt = view.findViewById(R.id.dialog_add_edt_dog_name);
    mDogAgeEdt = view.findViewById(R.id.dialog_add_edt_dog_age);
    mCancelBtn = view.findViewById(R.id.dialog_add_btn_cancel);
    mOkBtn = view.findViewById(R.id.dialog_add_btn_ok);
    mCancelBtn.setOnClickListener(this);
    mOkBtn.setOnClickListener(this);
    dialog.setContentView(view);


    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    lp.gravity = Gravity.CENTER;

    dialog.getWindow().setAttributes(lp);

    dialog.show();
  }

  private void handleBtnOk() {
    String person_name = mPersonNameEdt.getText().toString();
    String dog_name = mDogNameEdt.getText().toString();
    String dog_age_str = mDogAgeEdt.getText().toString();
    if (TextUtils.isEmpty(person_name) || TextUtils.isEmpty(dog_name) || TextUtils.isEmpty(dog_age_str))
      Toast.makeText(this, "Data is empty !", Toast.LENGTH_SHORT).show();
    else {
      int dog_age_int = Integer.valueOf(dog_age_str);
      addDatabase(person_name, dog_name, dog_age_int);
      dialog.dismiss();
    }
  }

  private void addDatabase(String person_name, String dog_name, int dog_age) {
    mRealm.beginTransaction();
    Dog dog = new Dog(dog_name, dog_age);
    Person person = mRealm.createObject(Person.class, UUID.randomUUID().toString());
    person.setName(person_name);
    person.getDogs().add(dog);
    mRealm.copyToRealm(person);
    mRealm.commitTransaction();

    personAdapter.notifyDataSetChanged();
    Log.e(TAG, "addDatabase: " + mRealm.where(Person.class).findAll().toString());
  }

  @Override
  public void onChange(Realm realm) {
    personAdapter.notifyDataSetChanged();
  }
}
