package student.socialdog;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private ArrayList<User.UserObject> friendslisted = MainFriendslist.myFriendsList;

    @Override
    public int getItemCount() {
        ArrayList<User.UserObject> friendslistedtest = MainFriendslist.myFriendsList;
        return friendslistedtest.size();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friendslist, parent, false);
        return new FriendViewHolder(view);


    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        User.UserObject friend = friendslisted.get(position);
        holder.display(friend);
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        private final TextView displayedName;
        private final TextView email;
        private final TextView lastWalk;
        private final ImageView profilepic;

        User.UserObject currentfriend = new User.UserObject("","","","","",null,null);

        FriendViewHolder(final View itemView) {
            super(itemView);

            displayedName = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            lastWalk = itemView.findViewById(R.id.lastWalk);
            profilepic = itemView.findViewById(R.id.friendpic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentfriend.displayedName)
                            .setMessage(currentfriend.lastWalk)
                            .show();
                }
            });
        }

        void display(User.UserObject friend) {
            currentfriend = friend;
            displayedName.setText(friend.displayedName);
            email.setText(friend.email);
            lastWalk.setText(friend.lastWalk);
            Glide.with(itemView.getContext()).load(friend.photoURL).into(profilepic);
        }
    }
}