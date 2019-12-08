package student.socialdog;

import java.util.ArrayList;

public class User {

    public static class UserObject {
        public String id;
        public String email;
        public String displayedName;
        public String lastWalk;
        public String photoURL;
        public ArrayList<String> friends;
        public ArrayList<String> dogs;

        public UserObject(String pId, String pemail, String pdisplayedName, String plastWalk, String pphotoURL,
                          ArrayList<String> pfriends, ArrayList<String> pdogs){
            this.id = pId;
            this.email = pemail;
            this.displayedName = pdisplayedName;
            this.lastWalk = plastWalk;
            this.photoURL = pphotoURL;
            this.friends = pfriends;
            this.dogs = pdogs;
        }
    }
}
