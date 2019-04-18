package zadanie.contactslist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ShowContact extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private List<Contact> list_contacts = new ArrayList<>();
    private Contact clickedContact;
    private Bitmap bitmap;

    @BindView(R.id.name) TextView name;
    @BindView(R.id.number) TextView number;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.gender) TextView gender;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.imgView) ImageView foto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        ButterKnife.bind(this);

        initFirebase();

        mDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_contacts.size() > 0)
                    list_contacts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    if (contact.getUid().equals((String) getIntent().getExtras().getString("uuid"))) {
                        clickedContact = contact;
                        String iName = clickedContact.getName() + " " + clickedContact.getSurname();
                        String iGender = clickedContact.getGender();
                        String iNumber = clickedContact.getNumber();
                        String iMail = clickedContact.getEmail();
                        String iDate = clickedContact.getDate();

                        try {
                            byte[] encodeByte = Base64.decode(clickedContact.getFoto(), Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                        } catch (NullPointerException e) {
                            e.getMessage();
                        } catch (OutOfMemoryError e) {
                            return;
                        }

                        name.setText(iName);
                        number.setText(iNumber);
                        email.setText(iMail);
                        date.setText(iDate);
                        gender.setText(iGender);
                        foto.setImageBitmap(bitmap);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

         @OnClick(R.id.delete)
         public void deleteUser(){
             if ((String) getIntent().getExtras().getString("uuid") != null) {
            mDatabaseReference.child("users").child((String) getIntent().getExtras().getString("uuid")).removeValue();
            Intent deleteContact = new Intent(ShowContact.this, Contacts.class);
            startActivity(deleteContact);
            }
         }

        @OnClick(R.id.back)
        public void backStep(){
            Intent backList = new Intent(ShowContact.this, Contacts.class);
            startActivity(backList);
        }

        private void initFirebase() {
            FirebaseApp.initializeApp(this);
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mFirebaseDatabase.getReference();
        }



}



