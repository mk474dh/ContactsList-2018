package zadanie.contactslist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zadanie.contactslist.RecyclerView.RecyclerTouchListener;
import zadanie.contactslist.RecyclerView.RecyclerViewAdapter;


public class Contacts extends AppCompatActivity {
    //binding view,button and progressbar for main screen
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.circular_progress) ProgressBar circular_progress;


    private RecyclerViewAdapter mAdapter;

    private DatabaseReference mDatabaseReference;

    private List<Contact> list_contacts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFirebase();
        addEventFirebaseListener();

        mAdapter = new RecyclerViewAdapter(list_contacts);

        recyclerView.setHasFixedSize(true);

        // vertical RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(Contacts.this, LinearLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //start activity for clicked contact
                final Contact contact = list_contacts.get(position);
                Intent clickContact = new Intent(Contacts.this, ShowContact.class);
                clickContact.putExtra("uuid",contact.getUid());
                startActivity(clickContact);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


    }

    @OnClick(R.id.newContact)
    public void startNewContact(View arg0){
        Intent newContact = new Intent(Contacts.this, NewContact.class);
        startActivity(newContact);
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    private void addEventFirebaseListener() {
        //Progressing
        circular_progress.setVisibility(View.VISIBLE);

        //get users from database sorted by surname
        mDatabaseReference.child("users").orderByChild("surname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_contacts.size() > 0)
                    list_contacts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    list_contacts.add(contact);
                }
            //stop progressing
            circular_progress.setVisibility(View.INVISIBLE);

            mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


