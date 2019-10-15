package student.socialdog;

import android.app.AlertDialog;
import android.media.Image;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonAdapteur extends RecyclerView.Adapter<MonAdapteur.FriendViewHolder> {

    public ArrayList<FriendsObject> friendslisted = MainFriendslist.friendslist;



    @Override
    public int getItemCount() {
        ArrayList<FriendsObject> friendslistedtest = MainFriendslist.friendslist;
        return friendslistedtest.size();
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friendslist, parent, false);
        return new FriendViewHolder(view);


    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        FriendsObject friend = friendslisted.get(position);
        holder.display(friend);
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView lastWalk;
        private final ImageView profilepic;

        public FriendsObject currentfriend = new FriendsObject("","",0);

        public FriendViewHolder(final View itemView) {
            super(itemView);

            name = ((TextView) itemView.findViewById(R.id.name));
            lastWalk = ((TextView) itemView.findViewById(R.id.lastWalk));
            profilepic = ((ImageView) itemView.findViewById(R.id.friendpic));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentfriend.name)
                            .setMessage(currentfriend.lastWalk)
                            .show();
                }
            });
        }

        public void display(FriendsObject friend) {
            currentfriend = friend;
            name.setText(friend.name);
            lastWalk.setText(friend.lastWalk);
            profilepic.setImageResource(friend.profilepic);
        }
    }

    public static class FriendsObject {
        public String name;
        public String lastWalk;
        public int profilepic;

        public FriendsObject(String pname, String plastWalk, int pprofilepic){
            this.name = pname;
            this.lastWalk = plastWalk;
            this.profilepic = pprofilepic;
        }
    }


}