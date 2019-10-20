package student.socialdog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class FixturesFragment extends Fragment {

    private AppCompatActivity mainActivity;

    public FixturesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fixtures, container, false);

        return rootView;
    }

    private void closefragment() {
        getActivity().getFragmentManager().popBackStack();
    }


}
