package zadanie.contactslist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewContact extends AppCompatActivity {
    @BindView(R.id.name)    EditText  input_name;
    @BindView(R.id.surname) EditText  input_surname;
    @BindView(R.id.number)  EditText  input_number;
    @BindView(R.id.email)   EditText  input_email;
    @BindView(R.id.gender)  EditText  input_gender;
    @BindView(R.id.date)    EditText  input_date;
    @BindView(R.id.imgView) ImageView foto;
    @BindView(R.id.toolbar) Toolbar   toolbar;

    private DatabaseReference mDatabaseReference;

    private static int RESULT_LOAD_IMAGE = 1;
    private Bitmap bitmap;
    private String imageString;
    private List<Contact> list_contacts = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contact);
        ButterKnife.bind(this);

        //Add toolbar
        toolbar.setTitle("Save new contact");
        setSupportActionBar(toolbar);

        //Firebase
        initFirebase();
        mDatabaseReference.child("users").orderByChild("surname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list_contacts.size() > 0)
                    list_contacts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    list_contacts.add(contact);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

    }

    @OnClick(R.id.btnChoose)
    public void chooseFoto(){
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            cursor.close();

            try {
                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            foto.setImageBitmap(bitmap);
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();

            imageString = Base64.encodeToString(b, Base64.DEFAULT);

        }
    }

    @OnClick(R.id.back)
    public void backStep(){
        Intent backList = new Intent(NewContact.this, Contacts.class);
        startActivity(backList);
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            createUser();
        }
        return true;
    }


    private void createUser() {
        String name = input_name.getText().toString();
        String surname = input_surname.getText().toString();
        String number = input_number.getText().toString();
        String email = input_email.getText().toString();
        String gender = input_gender.getText().toString();
        String date = input_date.getText().toString();

        if(TextUtils.isEmpty(name)) {
            input_name.setError("Please fill out the name.");
        }
        else if(TextUtils.isEmpty(surname)) {
            input_surname.setError("Please fill out the surname.");
        }
        else if(TextUtils.isEmpty(number)) {
            input_number.setError("Please fill out the number.");
        }
        else if(TextUtils.isEmpty(email)) {
            input_email.setError("Please fill out the email.");
        }
        else if(TextUtils.isEmpty(date)) {
            input_date.setError("Please fill out the date of birth.");
        }
        else if(TextUtils.isEmpty(gender)) {
            input_gender.setError("Please fill out the gender.");
        }
        else if(imageString==null){
            Toast.makeText(NewContact.this, "Please choose a photo.", Toast.LENGTH_SHORT).show();
        }
        else{
            for (Contact contact : list_contacts) {
                if(name.equals(contact.getName()) && surname.equals(contact.getSurname())){
                    Toast.makeText(NewContact.this, "Contact with the same name already exist.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(number.equals(contact.getNumber())){
                    Toast.makeText(NewContact.this, "Contact with the same number already exist.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Contact contact = new Contact(UUID.randomUUID().toString(), input_name.getText().toString(), input_surname.getText().toString(), input_date.getText().toString(),input_gender.getText().toString(),input_number.getText().toString(), input_email.getText().toString(), imageString);
            mDatabaseReference.child("users").child(contact.getUid()).setValue(contact);
            Intent contactList = new Intent(NewContact.this, Contacts.class);
            startActivity(contactList);
        }



    }


}

