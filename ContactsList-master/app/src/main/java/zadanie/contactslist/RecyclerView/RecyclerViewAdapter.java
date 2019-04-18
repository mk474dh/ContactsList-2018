package zadanie.contactslist.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import zadanie.contactslist.Contact;
import zadanie.contactslist.R;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Contact> lstContacts;
    private Bitmap bitmap;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        //binding views for listing contacts
        @BindView(R.id.name) TextView name;
        @BindView(R.id.number) TextView number;
        @BindView(R.id.imgView) ImageView foto;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public RecyclerViewAdapter(List<Contact> lstContacts) {
        this.lstContacts = lstContacts;
    }


    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
        //clicked contact
        Contact contact = lstContacts.get(position);
        //setting data to view from database
        holder.name.setText(contact.getName()+" "+ contact.getSurname());
        holder.number.setText(contact.getNumber());
        //encoding string to bitmamp

        try {
            byte[] encodeByte = Base64.decode(contact.getFoto(), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

        } catch (NullPointerException e) {
            e.getMessage();
        } catch (OutOfMemoryError e) {
            return;
        }

        //set photo
        holder.foto.setImageBitmap(bitmap);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return lstContacts.size();
    }



}
